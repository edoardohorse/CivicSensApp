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
}