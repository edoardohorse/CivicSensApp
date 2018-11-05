package com.civic.gercs.civicsense;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.civic.gercs.civicsense.Sender.Report;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Report.ReportModel> mDataset;
    private ManagerReport managerReport;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mAddressView;
        public TextView mDescriptionView;
        public View mGrade;
        public MyViewHolder(View v, TextView a, TextView d, View g) {
            super(v);
            mAddressView = a;
            mDescriptionView = d;
            mGrade = g;
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ManagerReport managerReport){
        this.managerReport = managerReport;
        mDataset = managerReport.getReportModels();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        ViewGroup v = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tile_report, parent, false);

        TextView address     = (TextView) v.findViewById(R.id.tile_address);
        TextView description = (TextView) v.findViewById(R.id.tile_description);
        View grade           = (View) v.findViewById(R.id.tile_grade);

        MyViewHolder vh = new MyViewHolder(v, address, description, grade);


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mAddressView.setText(mDataset.get(position).address);
        holder.mDescriptionView.setText(mDataset.get(position).descrition);
        Drawable grade = null;
        switch (mDataset.get(position).grade){
            case HIGH:{ grade = holder.itemView.getContext().getResources().getDrawable(R.drawable.circle_grade_high); break; }
            case INTERMEDIATE:{ grade = holder.itemView.getResources().getDrawable(R.drawable.circle_grade_intermediate); break;}
            case LOW:{ grade = holder.itemView.getResources().getDrawable(R.drawable.circle_grade_low); break;}
        }

        holder.mGrade.setBackgroundDrawable(grade);

        final int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            holder.mGrade.setBackgroundDrawable(grade);
        } else {
            holder.mGrade.setBackground(grade);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managerReport.showReport( position );
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset != null? mDataset.size() : 0;
    }


}













