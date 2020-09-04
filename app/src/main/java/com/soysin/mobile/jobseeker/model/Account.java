package com.soysin.mobile.jobseeker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accounts")
public class Account {
    public int getAccountRole() {
        return accountRole;
    }

    @ColumnInfo(name = "updated_at")
    private String updatedAt;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Account(int id, String token, long accountId, int accountRole, String nameImageProfile,String updatedAt) {
        this.updatedAt= updatedAt;
        this.token = token;
        this.id = id;
        this.accountId = accountId;
        this.accountRole = accountRole;
        this.nameImageProfile = nameImageProfile;
    }

    public String getNameImageProfile() {
        return nameImageProfile;
    }

    public String getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

    @ColumnInfo(name = "account_token")
    private String token;
    @ColumnInfo(name = "name_image_profile")
    private String nameImageProfile;

    @PrimaryKey(autoGenerate = false)
    private int id;

    @ColumnInfo(name = "account_id")
    private long accountId;

    @ColumnInfo(name = "account_role")
    private int accountRole;

    public long getAccountId() {
        return accountId;
    }
}
