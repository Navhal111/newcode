package pistalix.dslrcamera.dslrcamerahd;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import pistalix.dslrcamera.dslrcamerahd.stickerview.TouchImageView;


/**
 * Created by PC1 on 11/15/2016.
 */
public class ImagefullscreenActivity extends AppCompatActivity {

    private TouchImageView img;
    private ImageView back;
    private String TAG = ImagefullscreenActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);
        img = (TouchImageView) findViewById(R.id.iv_image);
        img.setImageURI(Uri.parse(MyCreation.IMAGEALLARY.get(MyCreation.pos)));
        back = (ImageView) findViewById(R.id.iv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


            }
        });
    }

       @Override
       public void onBackPressed () {

           finish();

       }



   }