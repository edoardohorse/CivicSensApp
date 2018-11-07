package com.civic.gercs.civicsense;


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


import com.civic.gercs.civicsense.Sender.GradeReport;
import com.civic.gercs.civicsense.Sender.Report;

public class ReportFragment extends Fragment {

    private TextView mTextViewDesctription;
    private TextView mTextViewTypeReport;
    private TextView mTextViewGrade;
    private LinearLayout mLinearHistoryReport;
    private Report mReport;
    private LinearLayout linearLayout;
    private static int mSizeHeight = 0;
    private static int mSizeWidth = 0;
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
        mTextViewDesctription = (TextView) view.findViewById(R.id.textview_description);
        mTextViewTypeReport = (TextView) view.findViewById(R.id.textview_type_report);
        mTextViewGrade = (TextView) view.findViewById(R.id.textview_grade);
        mLinearHistoryReport = (LinearLayout) view.findViewById(R.id.linear_history_report);
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
        mTextViewDesctription.setText( mReport.getDescription() );
        mTextViewTypeReport.setText( mReport.getType() );
        switch(mReport.getGrade()){
            case LOW:{mTextViewGrade.setText( "Bassa" ); break;}
            case INTERMEDIATE:{mTextViewGrade.setText( "Media" ); break;}
            case HIGH:{mTextViewGrade.setText( "Alta" ); break;}
        }


        for (Report.History h: mReport.getHistory()){
            View child = getLayoutInflater().inflate(R.layout.layout_history_report, null);
            ((TextView)child.findViewById(R.id.textview_history_date)).setText(h.getDate());
            ((TextView)child.findViewById(R.id.textview_history_note)).setText(h.getNote());

            mLinearHistoryReport.addView(child);
        }




//        mLinearHistoryReport.addView();

    }

    private void populatePhotos(){
        if(mSizeHeight == 0) {
            View parent = (View) linearLayout.getParent();
            mSizeHeight = parent.getLayoutParams().height;
        }


        for(String photo: mReport.getPhotos()) {
            ImageView imageView = new ImageView(getContext());
            ViewGroup.LayoutParams layoutParams  = new ViewGroup.LayoutParams(1000, mSizeHeight);

            linearLayout.addView(imageView, layoutParams);


            GlideApp.with(this)
                    .load(photo)
                    .skipMemoryCache(false)
                    .into(imageView);
        }
    }

}
