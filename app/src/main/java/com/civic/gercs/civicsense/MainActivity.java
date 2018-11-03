package com.civic.gercs.civicsense;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.civic.gercs.civicsense.Sender.Report;

public class MainActivity extends AppCompatActivity implements EventListener{

    public static ManagerReport managerReport;
    public static final String TAG ="Civic";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mFab;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle =  this.getTitle().toString();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mFab = (FloatingActionButton) findViewById(R.id.add_report);

        managerReport = new ManagerReport(this);
        managerReport.fetchReports();

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        final GPSTracker gps = new GPSTracker(this);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                Toast.makeText(getApplicationContext(), "Lat: "+ String.valueOf(latitude)+
                        "  Lng: "+ String.valueOf(longitude), Toast.LENGTH_LONG).show();
            }
        });/*


        managerReport.setImportDoneListener(new ManagerReport.OnImportDoneEventListener() {
            @Override
            public void onImportDone() {
                mRecyclerView.setHasFixedSize(true);


                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);


                mAdapter = new MyAdapter(managerReport);
                mRecyclerView.setAdapter(mAdapter);

//                openReport(managerReport.getReports().get(0));
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserActivity();
            }
        });*/
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
               managerReport.fetchReports();
               break;
           }
           case R.id.action_search:{
               openSearchActivity();
               break;
           }
       }

        return super.onOptionsItemSelected(item);
    }

    @Override
    @TargetApi(16)
    public void openReport(Report report){

        mFab.hide();

        setTitle(report.getAddress());
        ReportFragment fragment = new ReportFragment();
        Bundle b = new Bundle();
        b.putSerializable("report", report);
        fragment.setArguments(b);

        FragmentManager fragmentManager         = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =  fragmentManager.beginTransaction();


        fragmentTransaction.setCustomAnimations(
                R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);

        fragmentTransaction.replace(R.id.main_content, fragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();

    }

    @Override
    public void closeReport(){
        setTitle(mTitle);
        mFab.show();
    }

    public void openSearchActivity(){
        Intent i = new Intent(this, SearchActivity.class);
        startActivityForResult(i, 900);
    }

    public void openUserActivity(){
        Report report = new Report();
//        report.setCity(city);
//        report.setLocation(currentLocation);

        Intent i = new Intent(this, UserReportActivity.class);
        i.putExtra("report", report);
        startActivityForResult(i, 950);
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean isGPSEnabled(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Il GPS è disabilitato.")
                    .setPositiveButton("Abilita", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

            builder.create().show();



        }
        else{
//            Toast.makeText(this,"GPS abilitato",Toast.LENGTH_SHORT).show();
        }

        return enabled;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 900){
            if(resultCode == RESULT_OK){
                managerReport.showReport( (Report) data.getSerializableExtra("report"));
            }
        }
    }
}
