package com.appteve.quitsmok;

import java.util.Date;

/**
 * Created by appteve on 07/02/2017.
 */

public class AwardsItem {

     String titles;
     String price;
     String dates;

    public  AwardsItem (String titles, String price, String dates){
        super();
        this.titles = titles;
        this.price = price;
        this.dates = dates;

    }

    public String getTitles(){
        return titles;
    }

    public void setTitles(String titles){
        this.titles = titles;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String price){
        this.price = price;
    }

    public String getDates(){
        return dates;
    }

    public void setDates(String dates){
        this.dates = dates;
    }
}
