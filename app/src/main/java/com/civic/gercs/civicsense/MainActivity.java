package com.civic.gercs.civicsense;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.civic.gercs.civicsense.Sender.Sender;

public class MainActivity extends AppCompatActivity {

    private Sender sender;
    private ManagerReport managerReport;
    static final String TAG ="Civic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sender  = new Sender();
        managerReport = new ManagerReport();
        sender.fetchReports(managerReport);
//        Log.i(TAG, )

    }

    private void populateReport(){}

    private void importReports(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       switch(id){
           case R.id.action_refresh:{
               sender.fetchReports(managerReport);
           }
       }

        return super.onOptionsItemSelected(item);
    }

}
