package pistalix.echoeffect.photoeditor.Adpter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import pistalix.echoeffect.photoeditor.R;
import pistalix.echoeffect.photoeditor.View.Glob;


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
                        shareImage(Glob.app_name + " Created By : " + Glob.app_link, imagegallary.get(position));
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



    static class ViewHolder {

        ImageView imgIcon, imgDelete, imgShare;

    }

    public void shareImage(final String title, String path) {
        MediaScannerConnection.scanFile(activity, new String[]{path},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Intent shareIntent = new Intent(
                                Intent.ACTION_SEND);
                        shareIntent.setType("video/*");
                        shareIntent.putExtra(
                                Intent.EXTRA_TEXT, title);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        shareIntent
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        activity.startActivity(Intent.createChooser(shareIntent, "Share Video"));

                    }
                });
    }
}
