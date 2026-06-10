package com.example.medicationapp.api;

import com.example.medicationapp.dto.SearchItemResponse;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

import com.example.medicationapp.dto.UserLoginRequest;
import com.example.medicationapp.dto.UserRegisterRequest;
import com.example.medicationapp.dto.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import com.example.medicationapp.dto.MedicationDetailResponse;
import com.example.medicationapp.dto.SupplementDetailResponse;

import retrofit2.http.Path;

import com.example.medicationapp.dto.ReviewCreateRequest;
import com.example.medicationapp.dto.ReviewResponse;

import com.example.medicationapp.dto.ReviewUpdateRequest;

import com.example.medicationapp.dto.ReviewHideRequest;

import com.example.medicationapp.dto.MedicationRequest;
import com.example.medicationapp.dto.SupplementRequest;

import retrofit2.http.PATCH;
import retrofit2.http.DELETE;

import retrofit2.http.PUT;

public interface ApiService {

    @POST("api/users/register")
    Call<UserResponse> register(
            @Body UserRegisterRequest request
    );

    @POST("api/users/login")
    Call<UserResponse> login(
            @Body UserLoginRequest request
    );
    @GET("api/items/search")
    Call<List<SearchItemResponse>> searchItems(
            @Query("keyword") String keyword
    );
    @GET("api/items/medications/{id}")
    Call<MedicationDetailResponse> getMedicationDetail(
            @Path("id") Long id
    );

    @GET("api/items/supplements/{id}")
    Call<SupplementDetailResponse> getSupplementDetail(
            @Path("id") Long id
    );
    @GET("api/reviews/medication/{medicationId}")
    Call<List<ReviewResponse>> getMedicationReviews(
            @Path("medicationId") Long medicationId
    );

    @GET("api/reviews/supplement/{supplementId}")
    Call<List<ReviewResponse>> getSupplementReviews(
            @Path("supplementId") Long supplementId
    );

    @POST("api/reviews")
    Call<ReviewResponse> createReview(
            @Body ReviewCreateRequest request
    );

    @DELETE("api/reviews/{reviewId}")
    Call<String> deleteReview(
            @Path("reviewId") Long reviewId,
            @Query("userId") Long userId
    );
    @PUT("api/reviews/{reviewId}")
    Call<ReviewResponse> updateReview(
            @Path("reviewId") Long reviewId,
            @Body ReviewUpdateRequest request
    );
    @GET("api/users/admin/list")
    Call<List<UserResponse>> getUserList(
            @Query("adminId") Long adminId
    );

    @PATCH("api/users/admin/{targetUserId}/role")
    Call<UserResponse> updateUserRole(
            @Path("targetUserId") Long targetUserId,
            @Query("adminId") Long adminId,
            @Query("role") String role
    );

    @PATCH("api/users/admin/{targetUserId}/review-block")
    Call<UserResponse> updateReviewBlocked(
            @Path("targetUserId") Long targetUserId,
            @Query("adminId") Long adminId,
            @Query("blocked") Boolean blocked
    );

    @DELETE("api/users/admin/{targetUserId}")
    Call<String> deleteUserByAdmin(
            @Path("targetUserId") Long targetUserId,
            @Query("adminId") Long adminId
    );
    @GET("api/reviews/admin/list")
    Call<List<ReviewResponse>> getAllReviews(
            @Query("adminId") Long adminId
    );

    @PATCH("api/reviews/admin/{reviewId}/hide")
    Call<ReviewResponse> hideReviewByAdmin(
            @Path("reviewId") Long reviewId,
            @Body ReviewHideRequest request
    );

    @PATCH("api/reviews/admin/{reviewId}/unhide")
    Call<ReviewResponse> unhideReviewByAdmin(
            @Path("reviewId") Long reviewId,
            @Query("adminId") Long adminId
    );

    @DELETE("api/reviews/admin/{reviewId}")
    Call<String> deleteReviewByAdmin(
            @Path("reviewId") Long reviewId,
            @Query("adminId") Long adminId
    );
    @POST("api/items/admin/medications")
    Call<MedicationDetailResponse> addMedication(
            @Query("adminId") Long adminId,
            @Body MedicationRequest request
    );

    @PUT("api/items/admin/medications/{medicationId}")
    Call<MedicationDetailResponse> editMedication(
            @Path("medicationId") Long medicationId,
            @Query("adminId") Long adminId,
            @Body MedicationRequest request
    );

    @DELETE("api/items/admin/medications/{medicationId}")
    Call<String> deleteMedicationByAdmin(
            @Path("medicationId") Long medicationId,
            @Query("adminId") Long adminId
    );

    @POST("api/items/admin/supplements")
    Call<SupplementDetailResponse> addSupplement(
            @Query("adminId") Long adminId,
            @Body SupplementRequest request
    );

    @PUT("api/items/admin/supplements/{supplementId}")
    Call<SupplementDetailResponse> editSupplement(
            @Path("supplementId") Long supplementId,
            @Query("adminId") Long adminId,
            @Body SupplementRequest request
    );

    @DELETE("api/items/admin/supplements/{supplementId}")
    Call<String> deleteSupplementByAdmin(
            @Path("supplementId") Long supplementId,
            @Query("adminId") Long adminId
    );
}