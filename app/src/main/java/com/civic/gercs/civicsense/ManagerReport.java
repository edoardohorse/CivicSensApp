package com.civic.gercs.civicsense;

import android.content.Context;
import android.util.Log;

import com.civic.gercs.civicsense.Sender.Report;


import java.util.ArrayList;
import java.util.List;



public class ManagerReport {
    public List<Report> reports = null;
    public ArrayList<Report.ReportModel> reportModels = null;

    OnImportDoneEventListener mImportDoneListener = null;

    public interface OnImportDoneEventListener{
        void onImportDone();
    }

    public void setImportDoneListener(OnImportDoneEventListener listener){ mImportDoneListener = listener;}

    public void importReport(List<Report> rep){
        this.reports = rep;

        reportModels = new ArrayList<>(this.reports.size());
        for (Report r: this.reports) {
            this.reportModels.add( r.getModel() );
        }

        if(mImportDoneListener != null){
            mImportDoneListener.onImportDone();
        }
        Log.i(MainActivity.TAG, this.reports.size()+" Report importati");
    }

}
