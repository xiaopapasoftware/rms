package com.thinkgem.jeesite.modules.common.entity;

public class SelectItem {

    private String id;

    private String name;

    public SelectItem() {
    }

    public SelectItem(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
