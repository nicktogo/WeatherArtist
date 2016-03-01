package com.baelight.weatherartist.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.baelight.weatherartist.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseColorFragment extends DialogFragment {

    private ImageView white;
    private ImageView red;
    private ImageView orange;
    private ImageView yellow;
    private ImageView grey;
    private ImageView blue;
    private ImageView teal;
    private ImageView green;

    private AlertDialog.Builder builder;


    public ChooseColorFragment() {
        // Required empty public constructor
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_choose_color, null);

        white = (ImageView) view.findViewById(R.id.white);
        white.setOnClickListener(new ColorOnClickListener());

        red = (ImageView) view.findViewById(R.id.red);
        red.setOnClickListener(new ColorOnClickListener());

        orange = (ImageView) view.findViewById(R.id.orange);
        orange.setOnClickListener(new ColorOnClickListener());

        yellow = (ImageView) view.findViewById(R.id.yellow);
        yellow.setOnClickListener(new ColorOnClickListener());

        grey = (ImageView) view.findViewById(R.id.grey);
        grey.setOnClickListener(new ColorOnClickListener());

        blue = (ImageView) view.findViewById(R.id.blue);
        blue.setOnClickListener(new ColorOnClickListener());

        teal = (ImageView) view.findViewById(R.id.teal);
        teal.setOnClickListener(new ColorOnClickListener());

        green = (ImageView) view.findViewById(R.id.green);
        green.setOnClickListener(new ColorOnClickListener());

        builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Font color")
                .setView(view);

        return builder.create();
    }

    private class ColorOnClickListener implements View.OnClickListener{

        CreateWidgetActivityFragment fragment = (CreateWidgetActivityFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.create_widget_fragment);


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.white:
                    fragment.changeColor(getResources().getColor(R.color.font_white));
                    dismiss();
                    break;
                case R.id.red:
                    fragment.changeColor(getResources().getColor(R.color.font_red));
                    dismiss();
                    break;
                case R.id.orange:
                    fragment.changeColor(getResources().getColor(R.color.font_orange));
                    dismiss();
                    break;
                case R.id.yellow:
                    fragment.changeColor(getResources().getColor(R.color.font_yellow));
                    dismiss();
                    break;
                case R.id.grey:
                    fragment.changeColor(getResources().getColor(R.color.font_gray));
                    dismiss();
                    break;
                case R.id.green:
                    fragment.changeColor(getResources().getColor(R.color.font_green));
                    dismiss();
                    break;
                case R.id.teal:
                    fragment.changeColor(getResources().getColor(R.color.font_teal));
                    dismiss();
                    break;
                case R.id.blue:
                    fragment.changeColor(getResources().getColor(R.color.font_blue));
                    dismiss();
                    break;
                default:
                    break;
            }
        }
    }
}
