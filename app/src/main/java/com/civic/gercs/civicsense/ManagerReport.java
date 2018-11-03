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

    private OnImportDoneEventListener mImportDoneListener = null;


    // ==== Getter
    public List<Report> getReports() { return reports; }
    public ArrayList<Report.ReportModel> getReportModels() { return reportModels;}


    // === Constructor
    public ManagerReport(Activity a){
        listener = (EventListener) a;

        sender.setFetchReportDoneEventListener(new Sender.OnFetchReportDoneEventListener() {
            @Override
            public void onFetchDone(Report report) {
                openReport(report);
            }
        });
    }


    // === Method
    public void fetchReports(){
        sender.fetchReports(this);
    }

    public void importReport(List<Report> rep){
        this.reports = rep;

        reportModels = new ArrayList<>(this.reports.size());
        for (Report r: this.reports) {
            this.reportModels.add( r.getModel() );
        }

        if(mImportDoneListener != null){
            mImportDoneListener.onImportDone();
        }
        Log.i(TAG, this.reports.size()+" Report importati");
    }

    private void openReport(Report report){

        listener.openReport(report);

        // Call fragment of report
        Log.i(TAG, "Aperto report : "+ report.getAddress());
    }


    public void showReport(int position){
        Report report = reports.get(position);
        Report.ReportModel model = reportModels.get(position);

        // If not fetched before
        if(report.getPhotos().isEmpty()) {
            sender.fetchInfoReport(report);
        }
        else{
            openReport(report);
        }
    }

    public void showReport(Report report){
        // If not fetched before
        if(report.getPhotos().isEmpty()) {
            sender.fetchInfoReport(report);
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
        void onImportDone();
    }
    public interface SearchEventListener{
        boolean seachReportByCdt(String c, Report f);
    }

    public void setImportDoneListener(OnImportDoneEventListener listener){      mImportDoneListener     = listener;}

}
