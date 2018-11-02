package com.civic.gercs.civicsense.Sender;


import com.civic.gercs.civicsense.ManagerReport;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sender {
    public Service service;

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

            }
        });
    }


}
