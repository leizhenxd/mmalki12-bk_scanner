package com.example.malki.bktelematics;


import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.malki.telematics.R;

import java.util.List;

public class HelpAdapter extends ArrayAdapter<String[]>
{
    private Context mContext;
    private int id;
    private List<String []> items;
    customButtonListener customListener;

    public HelpAdapter(Context context, int textViewResourceId , List<String []> list )
    {
        super(context, textViewResourceId, list);

        mContext = context;
        id = textViewResourceId;
        items = list;


    }

    public interface customButtonListener {
        public void onButtonClickListener(String []value);
    }

    public void setCustomButtonListener(customButtonListener listener) {
        this.customListener = listener;
    }

    public View getView(final int position, View v, ViewGroup parent) {

        View view = v;
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();

        if(view == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(id, null);
        }

            //viewHolder.relative = (RelativeLayout) view.findViewById(R.id.helplayout);
            viewHolder.text = (TextView) view.findViewById(R.id.textView52);
            viewHolder.drop = (TextView) view.findViewById(R.id.textView53);
            viewHolder.arrow = (ImageView) view.findViewById(R.id.button21);

            //viewHolder.drop.setHeight(0);
            //viewHolder.relative.addView(viewHolder.drop);




        if(items.get(position) != null ){


            viewHolder.drop = (TextView) view.findViewById(R.id.textView53);
            //if(viewHolder.drop != null && !(items.get(position)[0].isEmpty()))
                viewHolder.text.setText(items.get(position)[0]);

            //Log.i("text", items.get(position)[0]);

            //if(viewHolder.arrow != null)
                viewHolder.arrow.setTag(0);

            Log.i("###tag", items.get(position)[0]);
            if(items.get(position)[1].isEmpty()) {
                viewHolder.arrow.setEnabled(false);
                viewHolder.arrow.setVisibility(View.INVISIBLE);
            }
            else {
                viewHolder.drop.setText(items.get(position)[1]);
                viewHolder.arrow.setImageResource(R.drawable.ic_expand);
                viewHolder.arrow.setVisibility(View.VISIBLE);
                viewHolder.arrow.setEnabled(true);
            }

            viewHolder.drop.setVisibility(View.GONE);
            //viewHolder.drop.setHeight(0);
            //viewHolder.relative.removeView(viewHolder.drop);
        }



        viewHolder.arrow.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                if(viewHolder.arrow.getTag().equals(0))
                {
                    //ViewGroup.LayoutParams params = viewHolder.drop.getLayoutParams();
                    //params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    //viewHolder.drop.setLayoutParams(params);

                    //viewHolder.drop = (TextView) view.findViewById(R.id.textView53);
//                    viewHolder.relative.removeView(viewHolder.text);
//                    viewHolder.relative.removeView(viewHolder.arrow);

                    if(viewHolder.drop != null && !(items.get(position)[1].isEmpty()))
                    {
                        //viewHolder.drop = (TextView) view.findViewById(R.id.textView53);
                        //viewHolder.drop.setText(items.get(position)[1]);

                        //viewHolder.relative.addView(viewHolder.drop);

                    }

                    else
                    {
                        //Log.i("")
                    }



//                    for(int i = 0; i < textT.length(); i++)
//                    {
//                        if(i > 0)
//                        {
//                            if(textT.charAt(i-1) == 'n' && textT.charAt(i) == 'l')
//                            {
//
//                            }
//                        }
//                    }
                    //textT = textT.replaceAll("\\\n", );

                    // /viewHolder.drop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//                    viewHolder.relative.addView(viewHolder.text);
//                    viewHolder.relative.addView(viewHolder.arrow);

                    //Log.i("HEIGHT", String.valueOf(params.height));
//                    ViewGroup.LayoutParams params = viewHolder.drop.getLayoutParams();
//                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    viewHolder.drop.setLayoutParams(params);



                    viewHolder.drop.setVisibility(View.VISIBLE);
                    //viewHolder.drop.setHeight(0);
                    viewHolder.arrow.setImageResource(R.drawable.ic_contract);
                    viewHolder.arrow.setTag(1);
                }

                else
                {
                    //viewHolder.relative.removeView(viewHolder.drop);
                    //viewHolder.drop.setHeight(0);
                    //viewHolder.drop.setHeight(0);
                    viewHolder.drop.setVisibility(View.GONE);
                    viewHolder.arrow.setImageResource(R.drawable.ic_expand);
                    viewHolder.arrow.setTag(0);
                }



            }
        });



        return view;
    }

    public class ViewHolder {
        private TextView text;
        private TextView drop;
        private ImageView arrow;
        private RelativeLayout relative;

    }
}
