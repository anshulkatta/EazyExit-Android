package com.model;

/**
 * Created by Akshay on 16-08-2017.
 */

public class Node {
    String name, level, status, location, ssid, type;

    public Node(){}
    Node(String name, String level, String status, String location, String ssid, String type){
        this.name = name;
        this.level = level;
        this.status = status;
        this.location = location;
        this.ssid = ssid;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Node){
            Node node = (Node) obj;
            if(
                    this.ssid.equals(node.ssid) &&
                    this.name.equals(node.name) &&
                            this.level.equals(node.level) &&
                            this.status.equals(node.status) &&
                            this.location.equals(node.location) &&
                            this.type.equals(node.type)
                    )
                return true;
        }
        return false;
    }
}
