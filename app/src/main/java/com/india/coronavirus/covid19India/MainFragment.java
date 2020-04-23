package com.india.coronavirus.covid19India;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.india.coronavirus.covid19India.district.District;
import com.india.coronavirus.covid19India.state.State;
import com.india.coronavirus.covid19India.state.StateAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();
    private View mRootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSwipeRefreshLayout = mRootView.findViewById(R.id.swipe_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                parseDistrictWiseList();
            }
        });

        Bundle bundle = getArguments();

        if(bundle==null) {
            parseDistrictWiseList();
        }
        else {
            List<State> stateList = (List<State>) getArguments().getSerializable("stateList");
            State wholeIndia = (State) getArguments().getSerializable("allOverIndia");
            setTotalCasesAndStartAdapter(stateList, wholeIndia);
        }

        return mRootView;
    }


    private void parseDistrictWiseList(){

        mSwipeRefreshLayout.setRefreshing(true);

        Call<String> call = RetrofitInstance.getRetrofitInstance().create(CovidApi.class).getDistrictWiseList();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                List< Pair<String, List<District>>> stateDistrictPairList = new ArrayList<>();

                try {
                    JSONArray array = new JSONArray(response.body());

                    for(int i=0;i<array.length();i++){

                        JSONObject obj = array.getJSONObject(i);
                        String state = obj.getString("state");
                        JSONArray districtData = obj.getJSONArray("districtData");

                        List<District> districtList = new ArrayList<>();
                        for(int j=0;j<districtData.length();j++){

                            JSONObject districtObj = districtData.getJSONObject(j);

                            String districtName = districtObj.getString("district");
                            String  confirmed   = districtObj.getString("confirmed");
                            String delta_confirmed  = districtObj.getJSONObject("delta").getString("confirmed");

                            districtList.add(new District(districtName, confirmed, delta_confirmed));

                        }

                        stateDistrictPairList.add(new Pair(state,districtList));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                    Log.e(TAG,e.toString());
                }

                parseStateWiseList(stateDistrictPairList);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_SHORT).show();
                Log.e(TAG,t.toString());
            }
        });
    }

    private void parseStateWiseList(final List<Pair<String, List<District>>> stateDistrictList) {

        //mSwipeRefreshLayout.setRefreshing(true);

        final List<State> mStateList = new ArrayList<>();
        final State[] allOverIndia = new State[1];

        Call<String> call = RetrofitInstance.getRetrofitInstance().create(CovidApi.class).getStateWiseList();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                mSwipeRefreshLayout.setRefreshing(false);

                mStateList.clear();

                String result = response.body();

                try {

                    JSONArray array = new JSONObject(result).getJSONArray("statewise");

                    for(int i=0;i<array.length();i++) {

                        JSONObject object = array.getJSONObject(i);

                        String name      = object.getString("state");
                        String active    = object.getString("active");
                        String confirmed      = object.getString("confirmed");
                        String deaths    = object.getString("deaths");
                        String recovered     = object.getString("recovered");

                        String updateAt  = object.getString("lastupdatedtime");     // 03/04/2020 19:32:24

                       // String d_act       = object.getJSONObject("delta").getString("active");
                        String d_active = "";
                        String d_confirmed      = object.getString("deltaconfirmed");
                        String d_deaths    = object.getString("deltadeaths");
                        String d_recovered     = object.getString("deltarecovered");


                        if(confirmed.equals("0"))
                            continue;

                        if(i!=0){
                            List<District> districtList = new ArrayList<>();
                            for(int j=0;j<stateDistrictList.size();j++){
                                if(stateDistrictList.get(j).first.equals(name)){
                                    districtList = stateDistrictList.get(j).second;
                                    break;
                                }
                            }
                            mStateList.add(new State(name, active,confirmed,deaths,recovered,updateAt, d_active, d_confirmed,d_deaths,d_recovered,districtList));
                        }
                        else{
                            allOverIndia[0] = new State(name, active,confirmed,deaths,recovered,updateAt, d_active, d_confirmed,d_deaths,d_recovered,null);
                        }
                    }

                    setTotalCasesAndStartAdapter(mStateList,allOverIndia[0]);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),t.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setLastUpdatedProperties(String updateAt) {

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
        int hrs = (int) (diffInSec/(60*60));
        int min = ((int) (diffInSec/(60)))%60;

        String dateTimeToShow = "Last update\n";
        if(hrs!=0)
            dateTimeToShow += hrs+" Hrs ";
        if(min!=0)
            dateTimeToShow += min +" Minutes Ago\n";
        else dateTimeToShow += " Ago\n";

        dateTimeToShow += day+" "+month+", "+time+" IST";


        ((TextView)mRootView.findViewById(R.id.last_updated_at)).setText(dateTimeToShow);

    }

    private void setTotalCasesAndStartAdapter(List<State> stateList, State allOverIndia) {

        setTotalCasesInIndia(allOverIndia);

        Log.e(TAG,"called : startAdapter");

        RecyclerView recyclerView = mRootView.findViewById(R.id.recycler_view_states);
        StateAdapter adapter = new StateAdapter(getActivity(),stateList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        ((TextView)mRootView.findViewById(R.id.states_affected)).setText(stateList.size()+" states/uts affected");

    }


    private void setTotalCasesInIndia(State allOverIndia) {

        TextView total_confirmed        = mRootView.findViewById(R.id.total_conf);
        TextView total_active      = mRootView.findViewById(R.id.total_active);
        TextView total_recovered  = mRootView.findViewById(R.id.total_recovered);
        TextView total_death      = mRootView.findViewById(R.id.total_deaths);

        //animation
        numberCountAnimation(allOverIndia.getConfirmed(),total_confirmed);
        numberCountAnimation(allOverIndia.getActive(),total_active);
        numberCountAnimation(allOverIndia.getRecovered(),total_recovered);
        numberCountAnimation(allOverIndia.getDeaths(),total_death);

        total_confirmed.setText(allOverIndia.getConfirmed());
        total_active.setText(allOverIndia.getActive());
        total_recovered.setText(allOverIndia.getRecovered());
        total_death.setText(allOverIndia.getDeaths());

        ((TextView)mRootView.findViewById(R.id.d_conf)).setText("[+"+allOverIndia.getD_conf()+"]");
        ((TextView)mRootView.findViewById(R.id.d_active)).setText("");
        ((TextView)mRootView.findViewById(R.id.d_recovered)).setText("[+"+allOverIndia.getD_recovered()+"]");
        ((TextView)mRootView.findViewById(R.id.d_death)).setText("[+"+allOverIndia.getD_deaths()+"]");

        setLastUpdatedProperties(allOverIndia.getLastUpdateAt());
    }

    private void numberCountAnimation(String num, final TextView view) {
        final ValueAnimator animator = ValueAnimator.ofInt(0, Integer.parseInt(num));
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setText(valueAnimator.getAnimatedValue().toString());
            }
        });

        animator.start();
    }
}