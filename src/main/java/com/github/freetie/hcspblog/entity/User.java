package com.github.freetie.hcspblog.entity;

import java.time.Instant;

public class User {
    Integer id;
    String username;
    String password;
    String avatar;
    Instant createdAt;
    Instant updatedAt;

    public User(Integer id, String username, String password, String avatar, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getPassword() {
        return password;
    }
}
