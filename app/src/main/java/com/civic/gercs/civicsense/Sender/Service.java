package com.civic.gercs.civicsense.Sender;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface Service {

    // Fetch all report of a city
    @GET("apiReport/ente/reports")
    Call<Report.ResponseReport> getAllReports();

    @GET("apiReport/report/photos/{reportId}")
    Call<ResponsePhoto> getPhotosByReportId(@Path("reportId") int reportId);

    @Multipart
    @POST("apiReport/report/new")
    Call<Report.ResponseReport> newReport(
            @Part("idCity") int idCity,
            @Part("description") String description,
            @Part("address")    String address,
            @Part("lan") double lan,
            @Part("lng") double lng,
            @Part List<MultipartBody.Part> photos
    );
}