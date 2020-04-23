package com.india.coronavirus.covid19India.state;

import com.india.coronavirus.covid19India.district.District;

import java.io.Serializable;
import java.util.List;

public class State implements Serializable {

    private String name;
    private String active;
    private String confirmed;
    private String deaths;
    private String recovered;

    private String lastUpdateAt;

    private String d_act;
    private String d_conf;
    private String d_deaths;
    private String d_recovered;

    private List<District> districtList;


    public State(String name, String active, String confired,
                 String deaths, String recovered, String lastUpdateAt, String d_act, String d_conf, String d_deaths,
                 String d_covered, List<District> districtList) {
        this.name = name;
        this.active = active;
        this.confirmed = confired;
        this.deaths = deaths;
        this.recovered = recovered;
        this.lastUpdateAt = lastUpdateAt;
        this.d_act = d_act;
        this.d_conf = d_conf;
        this.d_deaths = d_deaths;
        this.d_recovered = d_covered;
        this.districtList = districtList;
    }

    public String getName() {
        return name;
    }

    public String getActive() {
        return active;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getDeaths() {
        return deaths;
    }

    public String getRecovered() {
        return recovered;
    }

    public String getLastUpdateAt() {
        return lastUpdateAt;
    }

    public String getD_act() {
        return d_act;
    }

    public String getD_conf() {
        return d_conf;
    }

    public String getD_deaths() {
        return d_deaths;
    }

    public String getD_recovered() {
        return d_recovered;
    }

    public List<District> getDistrictList() {
        return districtList;
    }
}
