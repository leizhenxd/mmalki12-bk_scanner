package com.example.malki.bkapp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by malki on 9/12/16.
 */

public class CustomListAdapter extends ArrayAdapter <String> {

    private Context mContext;
    private int id;
    private List<String> items ;
    private Typeface type;

    public CustomListAdapter(Context context, int textViewResourceId , List<String> list )
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
        type = Typeface.createFromAsset(context.getAssets(), "EngschriftDIND.otf");
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.textview20);

        if(items.get(position) != null )
        {
//            text.setTextColor(Color.WHITE);
            text.setText(items.get(position));
            text.setTypeface(type);

//            text.setBackgroundColor(Color.RED);
//            int color = Color.argb( 200, 255, 64, 64 );
//            text.setBackgroundColor( color );

        }

        return mView;
    }
}
