package com.baelight.weatherartist.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.baelight.weatherartist.MyApplication;
import com.baelight.weatherartist.R;
import com.baelight.weatherartist.activity.CreateWidgetActivity;

import java.util.Timer;
import java.util.TimerTask;

public class ChooseWidgetFragment extends Fragment {

    private RelativeLayout parentLayout;
    private FloatingActionsMenu menu;

    private RelativeLayout shadowLayer;

    private CardView highLow;
    private CardView current;
    private CardView big;

    private FrameLayout newWidget;
    private CardView newWidgetInside;

    private TextView currentCounty;
    private TextView temp1;
    private TextView publishText;

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences defaultPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        if(defaultPrefs.getBoolean("isCreated",false)){

            currentCounty = (TextView) newWidgetInside.findViewById(R.id.current_county);
            currentCounty.setTextColor(defaultPrefs.getInt("name_color",getResources().getColor(R.color.font_default)));

            temp1 = (TextView) newWidgetInside.findViewById(R.id.temp1);
            temp1.setTextColor(defaultPrefs.getInt("temp_color",getResources().getColor(R.color.font_default)));


            publishText = (TextView) newWidgetInside.findViewById(R.id.publish_text);
            publishText.setTextColor(defaultPrefs.getInt("time_color",getResources().getColor(R.color.font_default)));

            newWidget.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_widget_layout,container,false);

        parentLayout = (RelativeLayout) view.findViewById(R.id.choose_widget);
        menu = (FloatingActionsMenu) view.findViewById(R.id.menu);

        highLow = (CardView) view.findViewById(R.id.high_low).findViewById(R.id.high_low_inside);

        highLow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                SharedPreferences.Editor defaultEditor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                defaultEditor.putString("widget", "high_low");
                defaultEditor.apply();
                Snackbar.make(parentLayout, "Selected!", Snackbar.LENGTH_SHORT).show();
                menu.animate().translationYBy(-80);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        menu.animate().translationYBy(80);
                    }
                }, 1800);

                return true;
            }
        });

        current = (CardView)view.findViewById(R.id.current).findViewById(R.id.current_inside);
        current.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                SharedPreferences.Editor defaultEditor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                defaultEditor.putString("widget","current");
                defaultEditor.apply();
                Snackbar.make(parentLayout,"Selected!",Snackbar.LENGTH_SHORT).show();
                menu.animate().translationYBy(-80);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        menu.animate().translationYBy(80);
                    }
                }, 1800);
                return true;
            }
        });

        big = (CardView)view.findViewById(R.id.big).findViewById(R.id.big_inside);
        big.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                SharedPreferences.Editor defaultEditor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                defaultEditor.putString("widget", "big");
                defaultEditor.apply();
                Snackbar.make(parentLayout, "Selected!", Snackbar.LENGTH_SHORT).show();
                menu.animate().translationYBy(-80);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        menu.animate().translationYBy(80);
                    }
                }, 1800);
                return true;
            }
        });

        newWidget = (FrameLayout) view.findViewById(R.id.new_widget);
        newWidgetInside = (CardView) newWidget.findViewById(R.id.new_widget_inside);
        newWidgetInside.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                SharedPreferences.Editor defaultEditor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                defaultEditor.putString("widget", "newWidget");
                defaultEditor.apply();
                Snackbar.make(parentLayout, "Selected!", Snackbar.LENGTH_SHORT).show();
                menu.animate().translationYBy(-80);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        menu.animate().translationYBy(80);
                    }
                }, 1800);
                return true;
            }
        });


//        view.findViewById(R.id.plain_layout).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor defaultEditor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
//                defaultEditor.putString("widget", "plain");
//                defaultEditor.apply();
//                Snackbar.make(parentLayout, "Selected!", Snackbar.LENGTH_SHORT).show();
//            }
//        });

        view.findViewById(R.id.luck_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(parentLayout, "Have not finished, maybe never :)", Snackbar.LENGTH_SHORT).show();
                menu.animate().translationYBy(-80);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        menu.animate().translationYBy(80);
                    }
                }, 1800);
            }
        });

        view.findViewById(R.id.four).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(parentLayout, "Have not finished, maybe never :)", Snackbar.LENGTH_SHORT).show();
                menu.animate().translationYBy(-80);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        menu.animate().translationYBy(80);
                    }
                }, 1800);
            }
        });

        view.findViewById(R.id.three).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateWidgetActivity.class);
                getActivity().startActivity(intent);
                menu.collapse();
            }
        });

        view.findViewById(R.id.two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(parentLayout, "Have not finished, maybe never :)", Snackbar.LENGTH_SHORT).show();
                menu.animate().translationYBy(-80);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        menu.animate().translationYBy(80);
                    }
                }, 1800);
            }
        });

        view.findViewById(R.id.one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(parentLayout, "Have not finished, maybe never :)", Snackbar.LENGTH_SHORT).show();
                menu.animate().translationYBy(-80);
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        menu.animate().translationYBy(80);
                    }
                }, 1800);
            }
        });

        shadowLayer = (RelativeLayout) view.findViewById(R.id.shadow_layer);
        shadowLayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (shadowLayer.getVisibility() == View.VISIBLE) {
                    menu.collapse();
                    return true;
                }
                return false;
            }
        });

        menu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                if (shadowLayer.getVisibility() == View.INVISIBLE) {
                    shadowLayer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onMenuCollapsed() {
                if (shadowLayer.getVisibility() == View.VISIBLE) {
                    shadowLayer.setVisibility(View.INVISIBLE);
                }
            }
        });


        return view;
    }


}
