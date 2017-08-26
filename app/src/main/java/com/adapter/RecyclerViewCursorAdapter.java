package com.adapter;

/*
 * Copyright (C) 2015 sancromeu.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;

/**
 * This cursor Adapter for the RecyclerView allows for proper, per item/item range notifications rather
 * than relying on {@link #notifyDataSetChanged()}. Code inspired on:
 * <ul>
 * <li>android.widget.CursorAdapter</li>
 * <li>https://gist.github.com/skyfishjy/443b7448f59be978bc59</li>
 * <li>https://gist.github.com/Shywim/127f207e7248fe48400b</li>
 * </ul>
 *
 * @author sancomeu
 * @since 2015-09-25
 */

public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Cursor mActiveCursor;
    private boolean mValidCursor;
    private int mSourceRowIdColumn;
    private DataSetObserver mDataSetObserver;
    private Handler mMainLoopHandler = new Handler(Looper.getMainLooper());


    /**
     * Creates a new adapter without an active cursor. Use {@link #swapCursor(Cursor, WithNotify)}
     * to prepare adapter
     */
    public RecyclerViewCursorAdapter() {
        this(null);
    }

    /**
     * Creates a new adapter using provided cursor
     *
     * @param cursor
     */
    public RecyclerViewCursorAdapter(Cursor cursor) {
        mActiveCursor = cursor;
        mValidCursor = cursor != null;
        mSourceRowIdColumn = mValidCursor ? mActiveCursor.getColumnIndex("_id") : -1;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mActiveCursor != null) {
            mActiveCursor.registerDataSetObserver(mDataSetObserver);
        }
    }


    /**
     * Get the active cursor (may be null)
     *
     * @return
     */
    public Cursor getCursor() {
        return mActiveCursor;
    }


    /**
     * The number of items the underlying cursor refers to, or 0 if cursor is invalid
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (mValidCursor && mActiveCursor != null) {
            return mActiveCursor.getCount();
        }
        return 0;
    }


    /**
     * The id of the item in the data source, or 0 is cursor is invalid or data source does not support ids
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        if (mValidCursor && mActiveCursor != null && mActiveCursor.moveToPosition(position)) {
            return mActiveCursor.getLong(mSourceRowIdColumn);
        }
        return 0;
    }


    /**
     * Indicates the data source behind the cursor provides stable ids (check SQlite documentation on
     * ROW_ID for details, as an example)
     *
     * @param hasStableIds
     */
    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }


    /**
     * Subclass will implement the RecyclerView's ViewHolder binding contract. Cursor has been moved to position when this
     * method is invoked.
     *
     * @param viewHolder
     * @param cursor     Ready-to-use cursor
     * @param position   Adapter position
     */
    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor, int position);


    /**
     * Translates RecyclerView.Adapter into CursorAdapter and calls {@link #onBindViewHolder(RecyclerView.ViewHolder, Cursor, int)}
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        onBindViewHolder(viewHolder, mActiveCursor, position);
    }


    /**
     * Swaps in a new Cursor, updates adapter as needed and return old Cursor (to handle as required,
     * for instance, by a CursorLoader). When swap is complete, notifies observers of data changes.
     *
     * @param newCursor The newly created cursor, may be null to indicate disabling the adapter
     * @param operation The type of RecyclerView.Adapter notification. Should be one of:
     *                  {@link #notifyItemRangeRemoved(int, int)}, {@link #notifyItemRemoved(int)},
     *                  {@link #notifyItemInserted(int)}, {@link #notifyItemRangeInserted(int, int)},
     *                  {@link #notifyItemChanged(int)}, {@link #notifyItemRangeChanged(int, int)},
     *                  {@link #notifyItemMoved(int, int)}. If null, will call {@link #notifyDataSetChanged()} (last result,
     *                  as indicated in documentation
     *                  https://developer.android.com/reference/android/support/v7/widget/RecyclerView
     *                  .Adapter.html#notifyDataSetChanged%28%29)
     */
    public Cursor swapCursor(Cursor newCursor, WithNotify operation) {
        if (newCursor == mActiveCursor) {
            return null;
        }
        final Cursor oldCursor = mActiveCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mActiveCursor = newCursor;
        if (mActiveCursor != null) {
            if (mDataSetObserver != null) {
                mActiveCursor.registerDataSetObserver(mDataSetObserver);
            }
            mSourceRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mValidCursor = true;
            notify(operation);
        } else {
            mSourceRowIdColumn = -1;
            mValidCursor = false;
            notify(operation);
        }
        return oldCursor;
    }


    private void notify(WithNotify notification) {
        if (notification != null) {
            notification.run();
        } else {
            mMainLoopHandler.post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }

    }


    /**
     * Provides a {@link #notifyItemChanged(int)} RecyclerView.Adapter to pass into {@link #swapCursor(Cursor, WithNotify)}
     *
     * @param position
     * @return
     */
    public WithNotify withNotifyItemChanged(final int position) {
        return new WithNotify() {
            @Override
            public void run() {
                notifyItemChanged(position);
            }
        };
    }


    /**
     * Provides a {@link #notifyItemRangeChanged(int, int)} RecyclerView.Adapter to pass into {@link #swapCursor(Cursor, WithNotify)}
     *
     * @param positionStart
     * @param itemCount
     * @return
     */
    public WithNotify withNotifyItemRangeChanged(final int positionStart, final int itemCount) {
        return new WithNotify() {
            @Override
            public void run() {
                notifyItemRangeChanged(positionStart, itemCount);
            }
        };
    }


    /**
     * Provides a {@link #notifyItemRemoved(int)} RecyclerView.Adapter to pass into {@link #swapCursor(Cursor, WithNotify)}
     *
     * @param position
     * @return
     */
    public WithNotify withNotifyItemRemoved(final int position) {
        return new WithNotify() {
            @Override
            public void run() {
                notifyItemRemoved(position);
            }
        };
    }


    /**
     * Provides a {@link #notifyItemRangeRemoved(int, int)} RecyclerView.Adapter to pass into {@link #swapCursor(Cursor, WithNotify)}
     *
     * @param positionStart
     * @param itemCount
     * @return
     */
    public WithNotify withNotifyItemRangeRemoved(final int positionStart, final int itemCount) {
        return new WithNotify() {
            @Override
            public void run() {
                notifyItemRangeRemoved(positionStart, itemCount);
            }
        };
    }


    /**
     * Provides a {@link #notifyItemInserted(int)} RecyclerView.Adapter to pass into {@link #swapCursor(Cursor, WithNotify)}
     *
     * @param position
     * @return
     */
    public WithNotify withNotifyItemInserted(final int position) {
        return new WithNotify() {
            @Override
            public void run() {
                notifyItemInserted(position);
            }
        };
    }


    /**
     * Provides a {@link #notifyItemRangeInserted(int, int)} RecyclerView.Adapter to pass into {@link #swapCursor(Cursor, WithNotify)}
     *
     * @param positionStart
     * @param itemCount
     * @return
     */
    public WithNotify withNotifynotifyItemRangeInserted(final int positionStart, final int itemCount) {
        return new WithNotify() {
            @Override
            public void run() {
                notifyItemRangeInserted(positionStart, itemCount);
            }
        };
    }


    /**
     * Provides a {@link #notifyItemMoved(int, int)} RecyclerView.Adapter to pass into {@link #swapCursor(Cursor, WithNotify)}
     *
     * @param fromPosition
     * @param toPosition
     * @return
     */
    public WithNotify withNotifyItemMoved(final int fromPosition, final int toPosition) {
        return new WithNotify() {
            @Override
            public void run() {
                notifyItemMoved(fromPosition, toPosition);
            }
        };
    }


    /**
     * Represents a RecyclerView.Adapter notify closure.
     */
    public interface WithNotify extends Runnable {
    }


    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mValidCursor = true;
            notifyDataSetChanged();
        }


        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mValidCursor = false;
            notifyDataSetChanged();
        }
    }
}