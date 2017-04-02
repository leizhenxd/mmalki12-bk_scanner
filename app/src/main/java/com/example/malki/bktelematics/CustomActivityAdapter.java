package com.example.malki.bktelematics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.malki.telematics.R;
import com.example.malki.bktelematics.ActivityLog;


import java.util.List;

public class CustomActivityAdapter extends ArrayAdapter<String []>
{

    private Context mContext;
    private int id;
    private List<String []> items ;
    private Activity ActivityLog;


    customButtonListener customListener;

    public interface customButtonListener {
        public void onButtonClickListener(String []value);
    }

    public void setCustomButtonListener(customButtonListener listener) {
        this.customListener = listener;
    }

    public CustomActivityAdapter(Context context, int textViewResourceId , List <String []> list )
    {
        super(context, textViewResourceId, list);

        mContext = context;
        id = textViewResourceId;
        items = list;


    }

    public View getView(final int position, View v, ViewGroup parent) {
        View view = v;
        final ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        if(view == null) {

            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(id, null);
        }
            viewHolder.dateTitle = (TextView) view.findViewById(R.id.textView47);
            viewHolder.text1 = (TextView) view.findViewById(R.id.textView48);
            viewHolder.text1.setAllCaps(true);
            viewHolder.text2 = (TextView) view.findViewById(R.id.textView50);
            viewHolder.text2.setAllCaps(true);
            viewHolder.text3 = (TextView) view.findViewById(R.id.textView51);
            viewHolder.text3.setAllCaps(true);

            viewHolder.indicator = (ImageView) view.findViewById(R.id.imageView14);
            viewHolder.incomplete = (TextView) view.findViewById(R.id.textView49);

            viewHolder.drop1 = (TextView) view.findViewById(R.id.textView44);
            //drop1.setAllCaps(true);
            viewHolder.drop2 = (TextView) view.findViewById(R.id.textView45);
            //drop2.setAllCaps(true);
            viewHolder.drop3 = (TextView) view.findViewById(R.id.textView46);
            //drop3.setAllCaps(true);
            viewHolder.icon = (ImageView) view.findViewById(R.id.imageView15);

            viewHolder.line = (View) view.findViewById(R.id.line);
            viewHolder.dropdown = (Button) view.findViewById(R.id.button19);
            viewHolder.solution = (Button) view.findViewById(R.id.button20);




        final String[] temp = getItem(position);
        // reference all attributes of activity log custom view

        //dropdown.setOnClickListener(handler);
        //solution.setOnClickListener(handler);


        if(items.get(position) != null )
        {
//            text.setTextColor(Color.WHITE);


            if(items.get(position)[4].length() == 16)
            {
                viewHolder.dateTitle.setText(items.get(position)[4].substring(6, 16));
            }


            viewHolder.text1.setText(items.get(position)[2] + " " + items.get(position)[3] + " " +items.get(position)[5]);
            viewHolder.text2.setText("ID: " + items.get(position)[0]);
            viewHolder.solution.setBackgroundResource(R.drawable.arrow);
            //viewHolder.solution.setVisibility(View.INVISIBLE);
            viewHolder.dropdown.setBackgroundResource(R.drawable.ic_expand);
            viewHolder.dropdown.setTag(0);
            //viewHolder.incomplete.setVisibility(View.GONE);

            viewHolder.drop1.setVisibility(View.GONE);
            viewHolder.drop2.setVisibility(View.GONE);
            viewHolder.drop3.setVisibility(View.GONE);
//            viewHolder.drop1.setHeight(0);
//            viewHolder.drop2.setHeight(0);
//            viewHolder.drop3.setHeight(0);

            viewHolder.solution.setEnabled(false);
            viewHolder.solution.setVisibility(View.GONE);
            viewHolder.icon.setVisibility(View.GONE);

//            viewHolder.solution.getLayoutParams().height = 0;
//            viewHolder.icon.getLayoutParams().height = 0;
            viewHolder.line.setVisibility(View.GONE);





            if(items.get(position)[1].equals("MacConnected"))
            {
                if(items.get(position)[7].equals("Connected"))
                {
                    viewHolder.indicator.setImageResource(R.drawable.a1);

                    if(items.get(position)[4].length() == 16)
                    {
                        viewHolder.drop2.setText("PAIRING COMPLETED: " + items.get(position)[4].substring(0,5));
                    }

                    viewHolder.text3.setTextColor(mContext.getResources().getColor(R.color.colorGreen));
                    viewHolder.text3.setText("PAIRED");
                    //viewHolder.icon.setImageResource(null);
                    viewHolder.icon.setVisibility(View.INVISIBLE);
                    viewHolder.drop1.setText(" ");
                    viewHolder.drop3.setText(" ");
                    viewHolder.solution.setTag(2);
                }

                else if (items.get(position)[7].equals("Disconnected"))
                {
                    if(items.get(position)[4].length() == 16)
                    {
                        viewHolder.drop1.setText("PAIRING INITIATED: " + items.get(position)[4].substring(0, 5));
                    }

                    viewHolder.drop2.setText("Tracker connection not detected");
                    viewHolder.drop3.setText("Please insert tracker into vehicle");
                    viewHolder.drop3.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                    viewHolder.incomplete.setVisibility(View.VISIBLE);
                    viewHolder.icon.setImageResource(R.drawable.activitytracker);
                    viewHolder.solution.setTag(0);
                    viewHolder.indicator.setImageResource(R.drawable.a3);
                    viewHolder.text3.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                    viewHolder.text3.setText("INCOMPLETE");
                }


            }

            else if(items.get(position)[1].equals("MacDetatched"))
            {

                viewHolder.indicator.setImageResource(R.drawable.a3);
                viewHolder.text3.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                viewHolder.text3.setText("INCOMPLETE");

                viewHolder.solution.setTag(1);

                if(items.get(position)[4].length() == 16)
                {
                    viewHolder.drop1.setText("INVALID DISCONNECTION: " + items.get(position)[4].substring(0,5));
                }

                viewHolder.drop2.setText("Tracker unplugged without BKT app");
                viewHolder.drop3.setText("Please use BKT app to complete");
                viewHolder.solution.setEnabled(true);
                viewHolder.drop3.setTextColor(mContext.getResources().getColor(R.color.colorRed));
                viewHolder.incomplete.setVisibility(View.VISIBLE);
                viewHolder.icon.setImageResource(R.drawable.mobile);
                //viewHolder.solution.setVisibility(View.VISIBLE);

            }

            else if(items.get(position)[1].equals("MacDisconnected"))
            {
                viewHolder.text3.setText("UNPAIRED");
                viewHolder.indicator.setImageResource(R.drawable.a2);

                if(items.get(position)[4].length() == 16)
                {
                    viewHolder.drop1.setText("UNPAIRING COMPLETED: " + items.get(position)[4].substring(0,5));
                }

                viewHolder.text3.setTextColor(mContext.getResources().getColor(R.color.colorAmber));
                viewHolder.drop2.setText("Disconnection reason: " + items.get(position)[6]);
                viewHolder.drop3.setText(" ");
                viewHolder.icon.setVisibility(View.INVISIBLE);

                viewHolder.solution.setTag(2);
            }



        }

        viewHolder.dropdown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something

//                if (customListener != null) {
//                    customListener.onButtonClickListener(position,temp);
//                }

                //Log.i("Resource of dropdown", viewHolder.dropdown.getBackground().toString());
                if(viewHolder.dropdown.getTag().equals(0))
                {
                    viewHolder.dropdown.setBackgroundResource(R.drawable.ic_contract);
                    viewHolder.dropdown.setTag(1);
                    viewHolder.drop1.setVisibility(View.VISIBLE);
                    viewHolder.drop2.setVisibility(View.VISIBLE);
                    viewHolder.drop3.setVisibility(View.VISIBLE);

                    viewHolder.line.setVisibility(View.VISIBLE);

                    //


//                    ViewGroup.LayoutParams params = viewHolder.drop1.getLayoutParams();
//                    params.height = mContext.getResources().getDimensionPixelSize(R.dimen.dropdown_height);
//                    viewHolder.drop1.setLayoutParams(params);
//
//                    params = viewHolder.drop2.getLayoutParams();
//                    params.height = mContext.getResources().getDimensionPixelSize(R.dimen.dropdown_height);
//                    viewHolder.drop2.setLayoutParams(params);
//
//                    params = viewHolder.drop3.getLayoutParams();
//                    params.height = mContext.getResources().getDimensionPixelSize(R.dimen.dropdown_height);
//                    viewHolder.drop3.setLayoutParams(params);

                    if(!(viewHolder.solution.getTag().equals(2)))
                    {
                        viewHolder.icon.setVisibility(View.VISIBLE);
//                        params = viewHolder.icon.getLayoutParams();
//                        params.height = mContext.getResources().getDimensionPixelSize(R.dimen.icon);
//                        viewHolder.icon.setLayoutParams(params);

                    }

                    else
                    {
                        viewHolder.icon.setVisibility(View.INVISIBLE);
                    }

                    if(viewHolder.solution.getTag().equals(1))
                    {
                        viewHolder.solution.setVisibility(View.VISIBLE);
//                        params = viewHolder.solution.getLayoutParams();
//                        params.height = mContext.getResources().getDimensionPixelSize(R.dimen.solution_height);
//                        viewHolder.solution.setLayoutParams(params);
                    }


                }

                else if(viewHolder.dropdown.getTag().equals(1))
                {

                    viewHolder.dropdown.setBackgroundResource(R.drawable.ic_expand);
                    viewHolder.dropdown.setTag(0);
                    viewHolder.drop1.setVisibility(View.GONE);
                    viewHolder.drop2.setVisibility(View.GONE);
                    viewHolder.drop3.setVisibility(View.GONE);
//                    viewHolder.drop1.setHeight(0);
//                    viewHolder.drop2.setHeight(0);
//                    viewHolder.drop3.setHeight(0);
                    viewHolder.line.setVisibility(View.GONE);

//                    ViewGroup.LayoutParams params = viewHolder.drop1.getLayoutParams();
//                    params.height = 0;
//                    viewHolder.drop1.setLayoutParams(params);
//
//                    params = viewHolder.drop2.getLayoutParams();
//                    params.height = 0;
//                    viewHolder.drop2.setLayoutParams(params);
//
//                    params = viewHolder.drop3.getLayoutParams();
//                    params.height = 0;
//                    viewHolder.drop3.setLayoutParams(params);


                    viewHolder.solution.setVisibility(View.GONE);
                    viewHolder.icon.setVisibility(View.GONE);

                }

            }
        });

        viewHolder.solution.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
//                if (customListener != null) {
//                    customListener.onButtonClickListener(temp);
//                }

                Intent connect = new Intent(view.getContext(), DisconnectActivity.class);
                connect.putExtra("trackerID", temp[0]);
                connect.putExtra("VIN", temp[5]);
                view.getContext().startActivity(connect);



            }
        });

        return view;
    }

    public class ViewHolder {
        private TextView text1;
        private TextView text2;
        private TextView text3;
        private TextView dateTitle;

        private TextView incomplete;
        private ImageView indicator;

        private Button solution;
        private Button dropdown;
        private View line;
        private ImageView icon;
        private TextView drop1;
        private TextView drop2;
        private TextView drop3;
    }
}
