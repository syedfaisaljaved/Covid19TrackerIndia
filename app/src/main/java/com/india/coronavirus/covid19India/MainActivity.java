package com.india.coronavirus.covid19India;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.india.coronavirus.covid19India.state.State;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new MainFragment());

    }

    private void loadFragment(Fragment fragment) {

        if(fragment instanceof MainFragment){
            Log.e("check", "sending bundle");

            //sending bundle received from Splash screen to Mainfragment
            List<State> stateList = (List<State>) getIntent().getSerializableExtra(Constants.STATE_LIST);
            State state = (State) getIntent().getSerializableExtra(Constants.ALL_INDIA);

            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.STATE_LIST, (Serializable) stateList);
            bundle.putSerializable(Constants.ALL_INDIA,state);
            fragment.setArguments(bundle);

        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment,fragment.getClass().getSimpleName());
        fragmentTransaction.commit();

    }

}
