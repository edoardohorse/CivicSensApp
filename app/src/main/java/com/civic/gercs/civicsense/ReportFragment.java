package com.civic.gercs.civicsense;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.civic.gercs.civicsense.Sender.Report;

public class ReportFragment extends Fragment {

    private TextView textview;
    private Report mReport;
    private LinearLayout linearLayout;
    private static int mSize = 0;
    private EventListener listener = null;

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        listener  = (EventListener) getActivity();
        try {
            mReport = (Report) arguments.getSerializable("report");
        }catch (NullPointerException e) {
            e.printStackTrace();
        }

        View view = inflater.inflate(R.layout.fragment_report, container, false);
        linearLayout = (LinearLayout) view.findViewById(R.id.layout_photos);
        textview = (TextView) view.findViewById(R.id.textview_description);

        populateFragment();
        return view;
    }


    @Override
    public void onDetach(){
        super.onDetach();

        if(listener != null){
            listener.closeReport();
        }
    }


    private void populateFragment(){
        populatePhotos();
        textview.setText( mReport.getDescription() );
        textview.setEnabled(false);
    }

    private void populatePhotos(){
        if(mSize == 0) {
            View parent = (View) linearLayout.getParent();
            mSize = parent.getHeight();
        }


        for(String photo: mReport.getPhotos()) {
            ImageView imageView = new ImageView(getContext());
            linearLayout.addView(imageView);


            GlideApp.with(this)
                    .load(photo)
                    .override(mSize)
                    .skipMemoryCache(false)
                    .into(imageView);
        }
    }

}
