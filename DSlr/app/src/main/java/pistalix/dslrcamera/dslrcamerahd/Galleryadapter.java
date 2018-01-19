package pistalix.dslrcamera.dslrcamerahd;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by PC1 on 10/26/2016.
 */
public class Galleryadapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;
    SparseBooleanArray mSparseBooleanArray;
    ArrayList<String> imagegallary = new ArrayList<String>();
    MediaMetadataRetriever metaRetriever;
    View vi;
    private int imageSize;

    public Galleryadapter(Activity dAct, ArrayList<String> dUrl) {
        activity = dAct;
        this.imagegallary = dUrl;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSparseBooleanArray = new SparseBooleanArray(imagegallary.size());
    }

    public int getCount() {
        return imagegallary.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {

            row = LayoutInflater.from(activity).inflate(R.layout.list_gallary, parent, false);
            holder = new ViewHolder();
            holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
            holder.imgDelete = (ImageView) row.findViewById(R.id.imgDelete);
            holder.imgShare = (ImageView) row.findViewById(R.id.imgShare);
            holder.imgSetAs = (ImageView) row.findViewById(R.id.imgSetAs);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Share...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want Share this?");

                // Setting Icon to Dialog

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        shareImage("https://play.google.com/store/apps/details?id=pistalix.dslrcamera.dslrcamerahd&hl=en", imagegallary.get(position));
                        // Write your code here to invoke YES event
                        // Toast.makeText(dactivity, "You clicked on YES", Toast.LENGTH_SHORT).show();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //  Toast.makeText(dactivity, "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }
        });

        holder.imgSetAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Set Wallpaper...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want Set as Wallpaper this?");

                // Setting Icon to Dialog

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        setWallpaper("Diversity", imagegallary.get(position));
                        // Write your code here to invoke YES event
                        // Toast.makeText(dactivity, "You clicked on YES", Toast.LENGTH_SHORT).show();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //  Toast.makeText(dactivity, "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Delete...");

                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want delete this?");

                // Setting Icon to Dialog

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        File fD = new File(imagegallary.get(position));
//
                        if (fD.exists()) {
//                    Utils.deleteDirectory(fD);
                            fD.delete();
                        }

                        imagegallary.remove(position);

                        notifyDataSetChanged();
                        if (imagegallary.size() == 0) {

                            Toast.makeText(activity, "No Image Found..", Toast.LENGTH_LONG).show();

                        }
                        // Write your code here to invoke YES event
                        // Toast.makeText(dactivity, "You clicked on YES", Toast.LENGTH_SHORT).show();
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //  Toast.makeText(dactivity, "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }
        });


        Glide.with(activity)
                .load(imagegallary.get(position))
                .centerCrop()
                .crossFade()
                .into(holder.imgIcon);


        System.gc();
        return row;
    }

    private void setWallpaper(String diversity, String s) {
        WallpaperManager wallpaperManager
                = WallpaperManager.getInstance(activity);
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // get the height and width of screen
        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        try {
//            int position2 = viewPager.getCurrentItem();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(s, options);
            wallpaperManager.setBitmap(bitmap);

            wallpaperManager.suggestDesiredDimensions(width / 2, height / 2);
            Toast.makeText(activity, "Wallpaper Set", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder {

        ImageView imgIcon, imgDelete, imgShare, imgSetAs;
        TextView txtTitle, txtDuration;

    }

    public void shareImage(final String title, String path) {
        MediaScannerConnection.scanFile(activity, new String[]{path},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Intent shareIntent = new Intent(
                                Intent.ACTION_SEND);
                        shareIntent.setType("video/*");
                        shareIntent.putExtra(
                                Intent.EXTRA_SUBJECT, title);
                        shareIntent.putExtra(
                                Intent.EXTRA_TITLE, title);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        activity.startActivity(Intent.createChooser(shareIntent, "Share Video"));

                    }
                });
    }
}
