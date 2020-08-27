package com.soysin.mobile.jobseeker.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.soysin.mobile.jobseeker.model.Account;

@Dao
public interface MyDAO {

    @Insert
    public long createAccount(Account account);

    @Delete
    public void deleteAccount(Account account);


    @Query("SELECT * FROM accounts WHERE id ==:accountId")
    public Account getAccount(long accountId);

    @Update
    public void updateAccount(Account account);

}
