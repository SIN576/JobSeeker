package com.soysin.mobile.jobseeker.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accounts")
public class Account {
    public Account(int id,String token,long accountId) {
        this.token = token;
        this.id = id;
        this.accountId = accountId;
    }

    public String getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

    @ColumnInfo(name = "account_token")
    private String token;

    @PrimaryKey(autoGenerate = false)
    private int id;

    @ColumnInfo(name = "account_id")
    private long accountId;

    public long getAccountId() {
        return accountId;
    }
}
