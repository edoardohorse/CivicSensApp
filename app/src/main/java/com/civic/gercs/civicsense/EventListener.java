package com.civic.gercs.civicsense;

import com.civic.gercs.civicsense.Sender.Report;

public interface EventListener {
    void openReport(Report report);
    void closeReport();
}
