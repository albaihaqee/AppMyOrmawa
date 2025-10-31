package com.inovarka.myormawa.models;

public class Organization {
    private String id;
    private String name;
    private String category;
    private String description;
    private int memberCount;
    private int priority;

    public Organization() {
    }

    public Organization(String id, String name, String category, String description, int memberCount, int priority) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.memberCount = memberCount;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}