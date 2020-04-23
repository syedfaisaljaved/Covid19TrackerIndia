package com.india.coronavirus.covid19India.district;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.india.coronavirus.covid19India.R;

import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.StateViewHolder> {

    private Context mContext;
    private List<District> mDistrictList;

    public DistrictAdapter(Context mContext, List<District> mDistrictList) {
        this.mContext = mContext;
        this.mDistrictList = mDistrictList;
    }

    @NonNull
    @Override
    public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StateViewHolder(LayoutInflater.from(mContext).inflate(R.layout.single_district_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StateViewHolder holder, int position) {

        District district = mDistrictList.get(position);

        setBackgroundColor(holder, position);

        holder.district.setText(district.getName());

        String conf = district.getConfirmed();
        String d_conf = district.getD_confirmed();

        if(conf.equals("0")) conf = "-";
        holder.confirmed.setText(conf);

        if(!d_conf.equals("0"))
            holder.d_confirmed.setText("+"+d_conf);

    }

    private void setBackgroundColor(StateViewHolder holder, int position) {
        if(position%2==0){
            holder.district.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme));
            holder.confirmed.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme));
            holder.ll_confirm.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme));
        }
        else{
            holder.district.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));
            holder.confirmed.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));
            holder.ll_confirm.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));
        }
    }

    @Override
    public int getItemCount() {
        return mDistrictList.size();
    }

    public class StateViewHolder extends RecyclerView.ViewHolder {

        private TextView district, confirmed;
        private TextView d_confirmed;
        private LinearLayout ll_confirm;


        public StateViewHolder(@NonNull View itemView) {
            super(itemView);

            district = itemView.findViewById(R.id.tv_district);
            confirmed = itemView.findViewById(R.id.tv_confirmed);
            d_confirmed = itemView.findViewById(R.id.d_confirmed);

            ll_confirm = itemView.findViewById(R.id.ll_confirm);

        }
    }
}
