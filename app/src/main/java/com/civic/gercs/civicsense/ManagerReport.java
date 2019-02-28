package com.civic.gercs.civicsense;

import android.app.Activity;
import android.util.Log;

import com.civic.gercs.civicsense.Sender.Report;
import com.civic.gercs.civicsense.Sender.Sender;


import java.util.ArrayList;
import java.util.List;

import static com.civic.gercs.civicsense.MainActivity.TAG;


public class ManagerReport {
    private List<Report> reports = null;
    private ArrayList<Report.ReportModel> reportModels = null;
    private Sender sender = new Sender();
    private EventListener listener;



    private List<Report.TypeReport> listTypeOfReport = null;
    boolean arePhotosLoaded = false;
    boolean isHistoryLoaded = false;
    private OnImportDoneEventListener mImportDoneListener = null;


    // ==== Getter
    public List<Report> getReports() { return reports; }
    public ArrayList<Report.ReportModel> getReportModels() { return reportModels;}
    public List<Report.TypeReport> getListTypeOfReport() { return listTypeOfReport;}

    // === Constructor
    public ManagerReport(Activity a){
        listener = (EventListener) a;

        sender.setFetchReportDoneEventListener(new Sender.OnFetchReportDoneEventListener() {
            @Override
            public void onFetchDone(Report report) {
                if(arePhotosLoaded && isHistoryLoaded) {
                    openReport(report);

                    arePhotosLoaded = false;
                    isHistoryLoaded = false;
                }
            }

            @Override
            public void onFetchPhotosDone(Report report) {
                arePhotosLoaded = true;
            }

            @Override
            public void onFetchHistoryDone(Report report) {
                isHistoryLoaded = true;
            }
        });
    }


    // === Method
    public void fetchTypesOfReport(){ sender.fetchTypesOfReport(this);}

    public void fetchReports(String cityName){
        sender.fetchReports(this, cityName);
    }

    public void importReport(List<Report> rep, Report.ResponseReport responseReport){
        if(rep != null){
            this.reports = rep;
            reportModels = new ArrayList<>(this.reports.size());
            for (Report r: this.reports) {
                this.reportModels.add( r.getModel() );
            }

            Log.i(TAG, "Report importati: "+this.reports.size());
        }
        else{
            Log.i(TAG, "Report importati: 0");
        }




        if(mImportDoneListener != null){
            mImportDoneListener.onImportDone(responseReport.getError(), responseReport.getMessage());
        }
    }

    public void clearReports(){
        this.reports.clear();
        this.reportModels.clear();
        this.listTypeOfReport.clear();
    }

    public void importListTypeOfReport(List<Report.TypeReport> l){
        this.listTypeOfReport = l;
    }

    private void openReport(Report report){



        listener.openReport(report);

        // Call fragment of report
        Log.i(TAG, "Aperto report : "+ report.getAddress());
    }


    public void showReport(int position){
        Report report = reports.get(position);
        Report.ReportModel model = reportModels.get(position);

        this.showReport(report);
    }

    public void showReport(Report report){
        // If not fetched before
        if(report.getPhotos().isEmpty()) {
            sender.fetchPhotosReport(report);
            sender.fetchHistorysReport(report);
        }
        else{
            openReport(report);
        }
    }

    public Report searchReportByCdt(String cdt){
        for(Report rep: reports){
            if(rep.getCdt().equals(cdt)){
                return rep;
            }
        }

        return null;

    }

    // === Interface and its setter
    public interface OnImportDoneEventListener{
        void onImportDone(boolean error, String message);
    }
    public interface SearchEventListener{
        boolean seachReportByCdt(String c, Report f);
    }

    public void setImportDoneListener(OnImportDoneEventListener listener){      mImportDoneListener     = listener;}

}
