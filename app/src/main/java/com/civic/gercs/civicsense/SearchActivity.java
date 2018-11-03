package com.civic.gercs.civicsense;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.civic.gercs.civicsense.Sender.Report;

import static com.civic.gercs.civicsense.MainActivity.TAG;
import static com.civic.gercs.civicsense.MainActivity.managerReport;

public class SearchActivity extends AppCompatActivity{
    private EditText mEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditText = (EditText) findViewById(R.id.editext_search);

    }

    public void search(View v){

        String cdt = mEditText.getText().toString();
        Report reportToFind = managerReport.searchReportByCdt(cdt);

        Intent data = new Intent();
        data.putExtra("report", reportToFind);

        if( reportToFind != null){
            Log.d(TAG, "Report trovato: "+reportToFind.getAddress());
            setResult(RESULT_OK, data);
            finish();
        }
        else {
            hideKeyboard(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_search_title)
                    .setMessage(R.string.dialog_search_text)
                    .setNeutralButton("Capito", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();

            Log.d(TAG, "Report non trovato");
        }


    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
