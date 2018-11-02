package com.civic.gercs.civicsense;

import android.util.Log;

import com.civic.gercs.civicsense.Sender.Report;


import java.util.List;



public class ManagerReport {
    public List<Report> reports = null;

    public void importReport(List<Report> rep){
        this.reports = rep;

        Log.i(MainActivity.TAG, this.reports.size()+" Report importati");
    }

}
