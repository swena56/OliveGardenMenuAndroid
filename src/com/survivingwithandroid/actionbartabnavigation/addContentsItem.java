package com.survivingwithandroid.actionbartabnavigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by Andy56-MSI on 9/17/13.
 */
public class addContentsItem extends PopupWindow {

   PopupWindow contentsPopUp;
    LinearLayout.LayoutParams params;
    LinearLayout layout;
    TextView textView;

    public void createPopupWindow(final Activity activity)
    {
       //LinearLayout viewGroup = (LinearLayout)activity.findViewById(R.)


        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);


       // View layout = layoutInflater.inflate(R.layout.collect_contents_data,viewGroup);


        contentsPopUp = new PopupWindow(activity);
        layout= new LinearLayout(activity);
        textView = new TextView(activity);

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
       // layout.setOrientation(LinearLayout.VERTICAL);
        textView.setText("Test");
        //layout.addView(textView,params);
        contentsPopUp.setContentView(layout);

    }



}