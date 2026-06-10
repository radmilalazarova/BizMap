package com.radmila.businessdirectory.network;

import com.radmila.businessdirectory.model.Company;
import com.radmila.businessdirectory.network.ApiResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {


    @GET("companies.php")
    Call<List<Company>> getAllCompanies();


    @GET("companies.php")
    Call<List<Company>> getCompaniesByCategory(
            @Query("category") String category
    );

    @GET("companies.php")
    Call<List<Company>> searchCompanies(
            @Query("category") String category,
            @Query("search") String searchTerm
    );


    @POST("companies.php")
    Call<ApiResponse> addCompany(@Body Company company);
}