package com.appteve.quitsmok;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

/**
 * Created by appteve on 07/02/2017.
 */

public class AwardsAdapter extends ArrayAdapter implements AppData {

    private final ArrayList<AwardsItem> awardsItems;
    private final Context context;
    SharedPreferences prefs;
    float dateToProgress;
    Date dateEnd;
    float pricePack;


    public AwardsAdapter(Context context, ArrayList<AwardsItem> awardsItems) {
        super(context, R.layout.item_awards, awardsItems);

        this.awardsItems = awardsItems;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item = inflater.inflate(R.layout.item_awards, parent, false);
        TextView categoryName = (TextView) item.findViewById(R.id.awardsHeaderText);
        categoryName.setText(awardsItems.get(position).titles);
        prefs = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        pricePack = prefs.getFloat(USER_PRICE_PACK,0);

        String currence = prefs.getString(USER_CURRENSY,"");

        dateEnd = new Date(prefs.getLong(USER_DATE, 0));

        TextView counter = (TextView) item.findViewById(R.id.counterAwardsText);
        counter.setText(awardsItems.get(position).price + " " + currence);
        float prisew;
        try{
             prisew = Float.parseFloat(awardsItems.get(position).price);
        }catch (NumberFormatException e){
            Toast.makeText(context, "You Not enter Valid Value", Toast.LENGTH_SHORT).show();
             prisew = 1;
        }


        TextView doneText = (TextView)  item.findViewById(R.id.doneText);





        RingProgressBar progress  = (RingProgressBar) item.findViewById(R.id.progressAwards);


        String dtStart = awardsItems.get(position).dates;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dtStart);
            Date d = new Date();
            long diffInMs = d.getTime() - dateEnd.getTime();
            long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
            float priced = 86400 / pricePack;
            float econom = (float)diffInSec  / priced;
            float prog = (econom / prisew) *100;
            int progr = (int) prog;
            if(progr>100){
                Toast.makeText(context, "You Not enter Valid Value", Toast.LENGTH_SHORT).show();
                progr=1;
            }

            if(progr<= 0){
                Toast.makeText(context, "You Not enter Valid Value", Toast.LENGTH_SHORT).show();
                progr =1;
            }
            progress.setProgress(progr);

            if (progr < 100){

                doneText.setText(R.string.progress);


            } else {

                doneText.setText(R.string.done);
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return item;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }


}