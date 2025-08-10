package com.nextcloudlab.kickytime.user.entity;

public enum RoleEnum {
    USER("사용자"),
    ADMIN("관리자");

    private String description;

    private RoleEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
