package com.civic.gercs.civicsense.Sender;


import android.util.Log;
import android.widget.Toast;

import com.civic.gercs.civicsense.ManagerReport;
import static com.civic.gercs.civicsense.MainActivity.TAG;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sender {
    public Service service;
    private OnFetchReportDoneEventListener mFetchReportDoneListener = null;

    public boolean fetchReports(final ManagerReport managerReport, String cityName){
        service = ServiceGenerator.createService();

        //Check if the connection to the server is valid
        if(service == null){
            return false;
        }

        Call<Report.ResponseReport> call = service.getAllReports(cityName);
        call.enqueue(new Callback<Report.ResponseReport>() {
            @Override
            public void onResponse(Call<Report.ResponseReport> call, Response<Report.ResponseReport> response) {
                if(response.isSuccessful()){
                    managerReport.importReport( response.body().getReports(), response.body() );
                }
            }

            @Override
            public void onFailure(Call<Report.ResponseReport> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });

        return true;
    }

    public void fetchPhotosReport(final Report report){
        service = ServiceGenerator.createService();
        Call<ResponsePhoto> call =  service.getPhotosByReportId(report.getId());
        call.enqueue(new Callback<ResponsePhoto>() {
            @Override
            public void onResponse(Call<ResponsePhoto> call, Response<ResponsePhoto> response) {
                if(response.isSuccessful()){
                    report.setPhotos( response.body().getPhotos() );

                    if(mFetchReportDoneListener != null){
                        mFetchReportDoneListener.onFetchPhotosDone(report);
                        mFetchReportDoneListener.onFetchDone(report);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePhoto> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                Log.d(TAG,t.getLocalizedMessage());
                t.printStackTrace();
            }
        });


    }

    public void fetchHistorysReport(final Report report){
        service = ServiceGenerator.createService();
        Call<Report.ResponseHistory> call =  service.getHistoryByReportId(report.getId());
        call.enqueue(new Callback<Report.ResponseHistory>() {
            @Override
            public void onResponse(Call<Report.ResponseHistory> call, Response<Report.ResponseHistory> response) {
                if(response.isSuccessful()){
                    report.setHistory( response.body().getHistory() );

                    if(mFetchReportDoneListener != null){
                        mFetchReportDoneListener.onFetchHistoryDone(report);
                        mFetchReportDoneListener.onFetchDone(report);
                    }
                }
            }

            @Override
            public void onFailure(Call<Report.ResponseHistory> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                Log.d(TAG,t.getLocalizedMessage());
                t.printStackTrace();
            }
        });


    }

    public void fetchTypesOfReport(final ManagerReport managerReport, String cityName) {
        service = ServiceGenerator.createService();
        Call<Report.ResponseTypeReport> call =  service.getAllTypeOfReport(cityName);
        call.enqueue(new Callback<Report.ResponseTypeReport>() {
            @Override
            public void onResponse(Call<Report.ResponseTypeReport> call, Response<Report.ResponseTypeReport> response) {
                if(response.isSuccessful()){
                    managerReport.importListTypeOfReport( response.body().getTypes() );
                    Log.d(TAG,response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<Report.ResponseTypeReport> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                Log.d(TAG,t.getLocalizedMessage());
                t.printStackTrace();
            }
        });


    }


    public interface OnFetchReportDoneEventListener{
        void onFetchDone(Report report);
        void onFetchPhotosDone(Report report);
        void onFetchHistoryDone(Report report);
    }

    public void setFetchReportDoneEventListener( OnFetchReportDoneEventListener listener ){ mFetchReportDoneListener = listener;}


}
