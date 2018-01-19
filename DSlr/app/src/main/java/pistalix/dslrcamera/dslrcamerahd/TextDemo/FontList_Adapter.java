package pistalix.dslrcamera.dslrcamerahd.TextDemo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pistalix.dslrcamera.dslrcamerahd.R;


public class FontList_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Typeface> stickers;
    private LayoutInflater inflater;
    TextView txtFont;

    public FontList_Adapter(Context context, ArrayList<Typeface> stickers) {
        this.context = context;
        this.stickers = stickers;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return stickers.size();
    }

    @Override
    public Object getItem(int i) {
        return stickers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_font, null);
        }
        txtFont = (TextView) convertView.findViewById(R.id.menu_item_title);
        txtFont.setTypeface(stickers.get(position));
        System.gc();
        return convertView;
    }

}
