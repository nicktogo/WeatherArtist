package com.baelight.weatherartist.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.baelight.weatherartist.MyApplication;
import com.baelight.weatherartist.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class CreateWidgetActivityFragment extends Fragment {

    //which component is being moved
    private final static int ACTION_TEMP = 0;
    private final static int ACTION_TIME = 1;
    private final static int ACTION_NAME = 2;

    private int currentView = -1;

    private View view;

    private FloatingActionsMenu menu;
    private RelativeLayout shadowLayer;

    //choose the image style
    private FloatingActionButton imageStyle;
    private RelativeLayout styles;
    private RelativeLayout allImages;
    private ImageView imageDisplayed;

    //choose the city-name's position
    private FloatingActionButton positionButton;
    private TextView positionText;

    //choose the time-text's position
    private FloatingActionButton timeButton;
    private TextView timeText;

    //choose the temp position
    private FloatingActionButton tempButton;
    private TextView tempText;

    public CreateWidgetActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_widget, container, false);

        menu = (FloatingActionsMenu) view.findViewById(R.id.add_menu);
        shadowLayer = (RelativeLayout) view.findViewById(R.id.shadow_layer);

        shadowLayer = (RelativeLayout) view.findViewById(R.id.shadow_layer);

        menu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                if(shadowLayer.getVisibility() == View.INVISIBLE){
                    shadowLayer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onMenuCollapsed() {
            }
        });

        imageStyle = (FloatingActionButton) view.findViewById(R.id.weather_style_button);
        imageStyle.setOnClickListener(new MyButtonClickListener());

        styles = (RelativeLayout) view.findViewById(R.id.styles);
        allImages = (RelativeLayout) view.findViewById(R.id.all_images);
        imageDisplayed = (ImageView) view.findViewById(R.id.weather_style_image);

        //将对应style的images显示
        styles.findViewById(R.id.style3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss the styles
                styles.setVisibility(View.GONE);

                //display all images of the chosen style
                allImages.setVisibility(View.VISIBLE);
            }
        });


        allImages.findViewById(R.id.images1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dismiss itself
                allImages.setVisibility(View.GONE);

                //display one image of the allImages
                imageDisplayed.setVisibility(View.VISIBLE);
                shadowLayer.setVisibility(View.INVISIBLE);
            }
        });

        imageDisplayed.setOnTouchListener(new MyOnTouchListener());

        positionButton = (FloatingActionButton) view.findViewById(R.id.city_position_button);
        positionButton.setOnClickListener(new MyButtonClickListener());
        positionText = (TextView) view.findViewById(R.id.city_position_text);
        positionText.setOnTouchListener(new MyOnTouchListener());


        timeButton = (FloatingActionButton) view.findViewById(R.id.time_button);
        timeButton.setOnClickListener(new MyButtonClickListener());
        timeText = (TextView) view.findViewById(R.id.time_text);
        timeText.setOnTouchListener(new MyOnTouchListener());

        tempButton = (FloatingActionButton) view.findViewById(R.id.temp_button);
        tempButton.setOnClickListener(new MyButtonClickListener());
        tempText = (TextView) view.findViewById(R.id.temp_text);
        tempText.setOnTouchListener(new MyOnTouchListener());

        return view;
    }

    public void clearAll(){
        imageDisplayed.setVisibility(View.GONE);
        positionText.setVisibility(View.GONE);
        timeText.setVisibility(View.GONE);
        tempText.setVisibility(View.GONE);
    }

    public View getView(){
        return view;
    }

    public void changeColor(int color){

        SharedPreferences.Editor defaultEditor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
        switch (currentView){
            case ACTION_NAME:
                positionText.setTextColor(color);
                defaultEditor.putInt("name_color",color);
                break;
            case ACTION_TEMP:
                tempText.setTextColor(color);
                defaultEditor.putInt("temp_color", color);
                break;
            case ACTION_TIME:
                timeText.setTextColor(color);
                defaultEditor.putInt("time_color", color);
                break;
            default:
                break;
        }
        defaultEditor.apply();

    }

    private class MyOnTouchListener implements View.OnTouchListener {

        private float x;
        private float y;
        private int _xDelta;
        private int _yDelta;

        @Override
        public boolean onTouch(View view,MotionEvent event) {

            //set currentView
            switch (view.getId()){
                case R.id.city_position_text:
                    currentView = ACTION_NAME;
                    break;
                case R.id.temp_text:
                    currentView = ACTION_TEMP;
                    break;
                case R.id.time_text:
                    currentView = ACTION_TIME;
                    break;
                default:
                    break;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    _xDelta = (int) (event.getRawX() - x);
                    _yDelta = (int) (event.getRawY() - y);
                    view.layout(_xDelta - 50, _yDelta - 300, _xDelta + view.getWidth() - 50, _yDelta + view.getHeight() - 300);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private class MyButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            menu.collapse();

            switch (v.getId()){
                case R.id.city_position_button:
                    positionText.setVisibility(View.VISIBLE);
                    shadowLayer.setVisibility(View.INVISIBLE);
                    break;
                case R.id.weather_style_button:
                    styles.setVisibility(View.VISIBLE);
                    break;
                case R.id.time_button:
                    timeText.setVisibility(View.VISIBLE);
                    shadowLayer.setVisibility(View.INVISIBLE);
                    break;
                case R.id.temp_button:
                    tempText.setVisibility(View.VISIBLE);
                    shadowLayer.setVisibility(View.INVISIBLE);
                    break;
                default:
                    break;
            }
        }
    }
}
