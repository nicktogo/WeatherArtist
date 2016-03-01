package com.baelight.weatherartist.activity;

import android.content.SharedPreferences;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.baelight.weatherartist.R;
import com.baelight.weatherartist.fragment.ChooseColorFragment;
import com.baelight.weatherartist.fragment.CreateWidgetActivityFragment;

public class CreateWidgetActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_widget);

        setSupportActionBar((Toolbar) findViewById(R.id.creating_toolbar));
        getSupportActionBar().setTitle("Artist");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_widget, menu);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View v = findViewById(R.id.delete);

                if (v != null) {
                    v.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            CreateWidgetActivityFragment fragment = (CreateWidgetActivityFragment) (getSupportFragmentManager().findFragmentById(R.id.create_widget_fragment));
                            View view = fragment.getView();
                            RelativeLayout canvas = (RelativeLayout) view.findViewById(R.id.widget_canvas);
                            RippleDrawable canvasBackground = (RippleDrawable) canvas.getBackground();
                            canvasBackground.setHotspot(canvas.getX() + canvas.getWidth() / 2 + 230, canvas.getY());
                            Log.i("x coordinate is ", canvas.getX() + canvas.getWidth() / 2 + "");
                            canvasBackground.setState(new int[]{android.R.attr.state_pressed,android.R.attr.state_enabled});
                            fragment.clearAll();
                            canvasBackground.setState(new int[]{});
                            return true;
                        }
                    });
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.done){
            SharedPreferences.Editor defaultEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            defaultEditor.putBoolean("isCreated",true);
            defaultEditor.apply();
            finish();
        }

        if(id == R.id.palette){
            DialogFragment chooseColorDialog = new ChooseColorFragment();
            chooseColorDialog.show(getSupportFragmentManager(),"Choose Color");
        }

//        if(id == R.id.delete){
//            CreateWidgetActivityFragment fragment = (CreateWidgetActivityFragment) (getSupportFragmentManager().findFragmentById(R.id.create_widget_fragment));
//            fragment.clearAll();
//        }

        return super.onOptionsItemSelected(item);
    }


}
