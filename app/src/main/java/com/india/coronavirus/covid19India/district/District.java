package com.india.coronavirus.covid19India.district;

import java.io.Serializable;

public class District implements Serializable {

    private String name;
    private String confirmed;
    private String d_confirmed;

    public District(String name, String confirmed, String d_confirmed) {
        this.name = name;
        this.confirmed = confirmed;
        this.d_confirmed = d_confirmed;
    }

    public String getName() {
        return name;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getD_confirmed() {
        return d_confirmed;
    }
}
