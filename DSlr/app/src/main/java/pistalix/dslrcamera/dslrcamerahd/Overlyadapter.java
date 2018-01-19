package pistalix.dslrcamera.dslrcamerahd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by xyz on 1/11/2017.
 */
public class Overlyadapter extends BaseAdapter {
    private Context context; //context
    private ArrayList<Modal> items;
    private Bitmap mBitmap;

    //public constructor
    public Overlyadapter(Context context, ArrayList<Modal> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_sticker, parent, false);
        }

        // get current item to be displayed
        ImageView img = (ImageView)
                convertView.findViewById(R.id.iv_list_stker);
        int resource = items.get(position).getThumbId();
        mBitmap = BitmapFactory.decodeResource(context.getResources(), resource);
        img.setImageBitmap(mBitmap);

        return convertView;
    }


}
