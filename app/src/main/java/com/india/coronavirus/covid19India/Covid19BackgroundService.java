package com.india.coronavirus.covid19India;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Pair;
import android.widget.RemoteViews;

import com.india.coronavirus.covid19India.district.District;
import com.india.coronavirus.covid19India.state.State;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Covid19BackgroundService extends Service {

    private static final String TAG = "Covid19BackgroundServic";

    private Intent intent;
    public Covid19BackgroundService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;

        RemoteViews views = new RemoteViews("com.india.coronavirus.covid19India", R.layout.covid19_widget);
        AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(intent.getIntExtra("appWidgetId",0), views);
        parseDistrictWiseList();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void parseDistrictWiseList(){

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
                }

                parseStateWiseList(stateDistrictPairList);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void parseStateWiseList(final List<Pair<String, List<District>>> stateDistrictList) {

        final List<State> mStateList = new ArrayList<>();
        final State[] allOverIndia = new State[1];

        Call<String> call = RetrofitInstance.getRetrofitInstance().create(CovidApi.class).getStateWiseList();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

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

                    RemoteViews views = new RemoteViews("com.india.coronavirus.covid19India", R.layout.covid19_widget);

                    views.setTextViewText(R.id.total_conf_widget, allOverIndia[0].getConfirmed());
                    views.setTextViewText(R.id.total_active_widget, allOverIndia[0].getActive());
                    views.setTextViewText(R.id.total_recovered_widget, allOverIndia[0].getRecovered());
                    views.setTextViewText(R.id.total_death_widget, allOverIndia[0].getDeaths());

                    views.setTextViewText(R.id.d_conf_widget, "[+"+allOverIndia[0].getD_conf()+"]");
                    views.setTextViewText(R.id.d_active_widget, allOverIndia[0].getD_act());
                    views.setTextViewText(R.id.d_recovered_widget, "[+"+allOverIndia[0].getD_recovered()+"]");
                    views.setTextViewText(R.id.d_death_widget, "[+"+allOverIndia[0].getD_deaths()+"]");


                    PendingIntent pendingIntentSync = PendingIntent.getService(getApplicationContext(),0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.sync_button,pendingIntentSync);
                    AppWidgetManager.getInstance(getApplicationContext()).updateAppWidget(intent.getIntExtra("appWidgetId",0), views);



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}
