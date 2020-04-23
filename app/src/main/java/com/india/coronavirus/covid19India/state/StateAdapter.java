package com.india.coronavirus.covid19India.state;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.india.coronavirus.covid19India.R;
import com.india.coronavirus.covid19India.district.DistrictAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StateAdapter extends RecyclerView.Adapter<StateAdapter.StateViewHolder> {

    private Context mContext;
    private List<State> mStateList;
    private final String TAG = "StateAdapter";

    public StateAdapter(Context mContext, List<State> mStateList) {
        this.mContext = mContext;
        this.mStateList = mStateList;
    }

    @NonNull
    @Override
    public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StateViewHolder(LayoutInflater.from(mContext).inflate(R.layout.single_state_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final StateViewHolder holder, int position) {

        State state = mStateList.get(position);

        setBackgroundColor(holder, position);

        holder.tv_state.setText(state.getName());

        String confirmed = state.getConfirmed();
        String active = state.getActive();
        String recovered = state.getRecovered();
        String death = state.getDeaths();

        String d_confirmed      = state.getD_conf();
        String d_recovered = state.getD_recovered();
        String d_deaths    = state.getD_deaths();

        if(confirmed.equals("0")) confirmed = "-";
        if(active.equals("0")) active = "-";
        if(recovered.equals("0")) recovered = "-";
        if(death.equals("0")) death = "-";


        holder.tv_conf.setText(confirmed);
        holder.tv_act.setText(active);
        holder.tv_rec.setText(recovered);
        holder.tv_death.setText(death);

        if(!d_confirmed.equals("0")) holder.tv_d_conf.setText("+"+d_confirmed);
        if(!d_recovered.equals("0")) holder.tv_d_recovered.setText("+"+d_recovered);
        if(!d_deaths.equals("0")) holder.tv_d_deaths.setText("+"+d_deaths);


        holder.rv_district.setLayoutManager(new LinearLayoutManager(mContext));
        holder.rv_district.setAdapter(new DistrictAdapter(mContext,state.getDistrictList()));


        holder.tv_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.ll_district_related.getVisibility()==View.GONE) {
                    holder.ll_district_related.setVisibility(View.VISIBLE);
                    holder.iv_arrow.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.arrow_down));
                }
                else {
                    holder.ll_district_related.setVisibility(View.GONE);
                    holder.iv_arrow.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.arrow_right));
                }
            }
        });

        setLastUpdatedTextView(state.getLastUpdateAt(),holder);

    }

    private void setBackgroundColor(StateViewHolder holder, int pos) {
        if(pos%2==0){
            holder.tv_state.setBackgroundColor(ContextCompat.getColor(mContext, R.color.dark_theme));
            holder.tv_conf.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme));
            holder.tv_act.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme));
            holder.tv_rec.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme));
            holder.tv_death.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme));

            holder.ll_confirm.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme));
            holder.ll_recovered.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme));
            holder.ll_deaths.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme));
        }
        else{
            holder.tv_state.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));
            holder.tv_conf.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));
            holder.tv_act.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));
            holder.tv_rec.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));
            holder.tv_death.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));

            holder.ll_confirm.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));
            holder.ll_recovered.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));
            holder.ll_deaths.setBackgroundColor(ContextCompat.getColor(mContext,R.color.dark_theme2));
        }
    }

    @Override
    public int getItemCount() {
        return mStateList.size();
    }

    private void setLastUpdatedTextView(String updateAt, StateViewHolder holder){

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date date = null;
        try {
            date = format.parse(updateAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String formattedDate = date.toString();

        String day   = formattedDate.substring(8,10);
        String month = formattedDate.substring(4,7);
        String time  = formattedDate.substring(11,16);

        long diffInSec = (System.currentTimeMillis() - date.getTime()) / 1000L;
        int days = (int) (diffInSec/(60*60*24));
        int hrs = (int) (diffInSec/(60*60));
        int min = ((int) (diffInSec/(60)))%60;

        String dateTimeToShow = "";

        if(days!=0){
            if(days==1)
                dateTimeToShow = "About "+days +" day ago";
            else
                dateTimeToShow = "About "+days +" days ago";
        }

        else {

            if (hrs != 0)
                dateTimeToShow += hrs + " Hrs ";
            if (min != 0)
                dateTimeToShow += min + " Minutes Ago";
            else dateTimeToShow += " Ago";
        }


        holder.tv_last_update.setText(dateTimeToShow);

    }

    public class StateViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_state, tv_conf, tv_act, tv_rec, tv_death;
        private TextView tv_d_conf, tv_d_recovered, tv_d_deaths;
        private LinearLayout ll_confirm, ll_recovered, ll_deaths, ll_district_related;
        private RecyclerView rv_district;
        private ImageView iv_arrow;
        private TextView tv_last_update;

        public StateViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_state = itemView.findViewById(R.id.tv_state);
            tv_conf  = itemView.findViewById(R.id.tv_confirmed);
            tv_act   = itemView.findViewById(R.id.tv_active);
            tv_rec   = itemView.findViewById(R.id.tv_recovered);
            tv_death = itemView.findViewById(R.id.tv_death);

            tv_d_conf = itemView.findViewById(R.id.d_confirmed);
            tv_d_recovered = itemView.findViewById(R.id.d_recovered);
            tv_d_deaths = itemView.findViewById(R.id.d_death);

            ll_confirm = itemView.findViewById(R.id.ll_confirm);
            ll_recovered = itemView.findViewById(R.id.ll_recovered);
            ll_deaths = itemView.findViewById(R.id.ll_deaths);

            rv_district = itemView.findViewById(R.id.recycler_view_district);
            ll_district_related = itemView.findViewById(R.id.ll_district_related);
            iv_arrow = itemView.findViewById(R.id.arrow);

            tv_last_update = itemView.findViewById(R.id.last_update);
        }
    }


}
