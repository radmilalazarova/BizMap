package com.radmila.businessdirectory.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CompanyDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CompanyEntity> companies);


    @Query("SELECT * FROM companies " +
            "WHERE ',' || categories || ',' LIKE '%,' || :category || ',%' " +
            "ORDER BY name ASC")
    List<CompanyEntity> getByCategory(String category);


    @Query("SELECT * FROM companies " +
            "WHERE ',' || categories || ',' LIKE '%,' || :category || ',%' " +
            "AND name LIKE '%' || :search || '%' " +
            "ORDER BY name ASC")
    List<CompanyEntity> searchInCategory(String category, String search);


    @Query("SELECT * FROM companies")
    List<CompanyEntity> getAll();


    @Query("DELETE FROM companies")
    void deleteAll();
}