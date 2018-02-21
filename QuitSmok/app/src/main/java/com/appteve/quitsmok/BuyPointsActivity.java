package com.appteve.quitsmok;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class BuyPointsActivity extends AppCompatActivity  implements  AppData{

    Button buy10points;
    Button buy20Points;
    Button buy40Points;

    TextView userPointsLabel;

    BillingProcessor bp;
    private boolean readyToPurchase = false;
    private static final String LOG_TAG = "iabv3";
    SharedPreferences prefs;

    float userPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_points);

        buy10points = (Button) findViewById(R.id.buy10pointsBtn);
        buy10points.setText(R.string.buy_10_points);

        buy20Points = (Button) findViewById(R.id.buy20pointsBtn);
        buy20Points.setText(R.string.buy_20_points);

        buy40Points = (Button) findViewById(R.id.buy40pointsBtn);
        buy40Points.setText(R.string.buy_40_points);

        userPointsLabel = (TextView) findViewById(R.id.userPOintsText);

        prefs = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        userPoints = prefs.getFloat(USER_POINT,0);

        userPointsLabel.setText(getResources().getString(R.string.user_point_text)+" " + userPoints);

        ////// billing processor

        if(!BillingProcessor.isIabServiceAvailable(this)) {


        }

        bp = new BillingProcessor(this, GOOGLE_BILLING_KEY, MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {


                if (productId.equals(BUY_10_POINTS)){

                    userPoints = userPoints + 10;
                    prefs.edit().putFloat(USER_POINT,userPoints).apply();
                    userPointsLabel.setText(getResources().getString(R.string.user_point_text)+" " + userPoints);

                    consumeInapp(productId);


                }

                if (productId.equals(BUY_20_POINTS)){

                    userPoints = userPoints + 20;
                    prefs.edit().putFloat(USER_POINT,userPoints).apply();
                    userPointsLabel.setText(getResources().getString(R.string.user_point_text)+" " + userPoints);

                    consumeInapp(productId);


                }

                if (productId.equals(BUY_40_POINTS)){

                    userPoints = userPoints + 40;
                    prefs.edit().putFloat(USER_POINT,userPoints).apply();
                    userPointsLabel.setText(getResources().getString(R.string.user_point_text)+" " + userPoints);

                    consumeInapp(productId);


                }

            }
            @Override
            public void onBillingError(int errorCode, Throwable error) {


            }
            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;

            }
            @Override
            public void onPurchaseHistoryRestored() {
                for(String sku : bp.listOwnedProducts())
                    System.out.println("Owned Managed Product: " + sku);
                for(String sku : bp.listOwnedSubscriptions())
                    System.out.println("Owned Subscription: " + sku);
                // updateTextViews();
            }
        });







    }

    public void buy10Points(View view){

        bp.purchase(BuyPointsActivity.this,BUY_10_POINTS);
    }

    public void buy20Points(View view){

        bp.purchase(BuyPointsActivity.this,BUY_20_POINTS);
    }

    public void buy40Points(View view){

        bp.purchase(BuyPointsActivity.this,BUY_40_POINTS);
    }

    public void consumeInapp (String productId){

        Boolean consumed = bp.consumePurchase(productId);



    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }


}
