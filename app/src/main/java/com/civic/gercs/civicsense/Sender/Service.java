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
    @GET("api/ente/reports")
    Call<Report.ResponseReport> getAllReports();

    @GET("api/report/photos/{reportId}")
    Call<ResponsePhoto> getPhotosByReportId(@Path("reportId") int reportId);

    @GET("api/report/history/{reportId}")
    Call<Report.ResponseHistory> getHistoryByReportId(@Path("reportId") int reportId);

    @GET("api/report/types")
    Call<Report.ResponseTypeReport> getAllTypeOfReport();

    @Multipart
    @POST("api/report/new")
    Call<Report.ResponseNewReport> newReport(
            @Part("city")           String nameCity,
            @Part("description")    String description,
            @Part("address")        String address,
            @Part("lan")            double lan,
            @Part("lng")            double lng,
            @Part("typeReport")     int typeReport,
            @Part("grade")          String grade,
            @Part("email")          String email,
            @Part                   List<MultipartBody.Part> photos
    );
}