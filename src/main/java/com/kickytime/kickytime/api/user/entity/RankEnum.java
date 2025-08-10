package com.kickytime.kickytime.api.user.entity;

public enum RankEnum {
    BEGINER("입문자"),
    INTERMEDIATE("중급자"),
    MASTER("상급자");

    private final String description;

    RankEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
