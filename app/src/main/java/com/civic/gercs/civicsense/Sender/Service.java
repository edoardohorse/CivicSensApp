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
    @GET("api/ente/{cityName}/reports")
    Call<Report.ResponseReport> getAllReports(@Path("cityName") String cityName);
    

    @GET("api/report/photos/{reportId}")
    Call<ResponsePhoto> getPhotosByReportId(@Path("reportId") int reportId);

    @GET("api/report/history/{reportId}")
    Call<Report.ResponseHistory> getHistoryByReportId(@Path("reportId") int reportId);

    @GET("api/report/types/{cityName}")
    Call<Report.ResponseTypeReport> getAllTypeOfReport(@Path("cityName") String cityName);

    @Multipart
    @POST("api/report/new")
    Call<Report.ResponseNewReport> newReport(
            @Part("city")           String nameCity,
            @Part("description")    String description,
            @Part("address")        String address,
            @Part("grade")          String grade,
            @Part("typeReport")     int typeReport,
            //idteam
            @Part("lan")            double lan,
            @Part("lng")            double lng,
            //code
            @Part                   List<MultipartBody.Part> photos,
            @Part("email")          String email
    );
}