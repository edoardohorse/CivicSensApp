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

    public void fetchReports(final ManagerReport managerReport){
        service = ServiceGenerator.createService();
        Call<Report.ResponseReport> call = service.getAllReports();
        call.enqueue(new Callback<Report.ResponseReport>() {
            @Override
            public void onResponse(Call<Report.ResponseReport> call, Response<Report.ResponseReport> response) {
                if(response.isSuccessful()){
                    managerReport.importReport( response.body().getReports() );

                }
            }

            @Override
            public void onFailure(Call<Report.ResponseReport> call, Throwable t) {
                Log.e(TAG,t.getMessage());
            }
        });
    }

    public void fetchInfoReport(final Report report){
        service = ServiceGenerator.createService();
        Call<ResponsePhoto> call =  service.getPhotosByReportId(report.getId());
        call.enqueue(new Callback<ResponsePhoto>() {
            @Override
            public void onResponse(Call<ResponsePhoto> call, Response<ResponsePhoto> response) {
                if(response.isSuccessful()){
                    report.setPhotos( response.body().getPhotos() );

                    if(mFetchReportDoneListener != null){
                        mFetchReportDoneListener.onFetchDone(report);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePhoto> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                Log.i(TAG,t.getLocalizedMessage());
                t.printStackTrace();
            }
        });
    }


    public interface OnFetchReportDoneEventListener{
        void onFetchDone(Report report);
    }

    public void setFetchReportDoneEventListener( OnFetchReportDoneEventListener listener ){ mFetchReportDoneListener = listener;}


}
