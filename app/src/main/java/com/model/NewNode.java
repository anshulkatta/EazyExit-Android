package com.model;

/**
 * Created by Akshay on 25-08-2017.
 */

public class NewNode {
    Node node;
    int position;

    public NewNode(String ssid, int position){
        this.node = new Node();
        this.node.setName(ssid);
        this.node.setSsid(ssid);
        this.node.setType("");
        this.node.setLocation("NEW");
        this.node.setLevel("SECONDARY");
        this.node.setStatus("OFF");

        this.position = position;
    }
}
