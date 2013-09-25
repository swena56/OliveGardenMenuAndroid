package com.survivingwithandroid.actionbartabnavigation;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.FrameLayout;

public class MainActivity extends Activity implements TabListener {

	List<Fragment> fragList = new ArrayList<Fragment>();

    List<MenuItem> listOfMenuItems = new ArrayList<MenuItem>();

   // protected void onResume()
   // {
   //     Log.d("MainActivity", "onResume");
   // }


     int filterSpinnerSelection = 0;


    @Override
    protected void onResume() {
        super.onResume();
       // Bundle getData = getIntent().getExtras();
        //int test = getData.getInt("idx");
        //Log.d("MainActivity","onResume"+test);

    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);



        Log.d("MainActivity", "onCreate");
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayHomeAsUpEnabled(false);




			Tab tab = bar.newTab();
			tab.setText("List ");
			tab.setTabListener(this);
            tab.setIcon(R.drawable.list);
			bar.addTab(tab);

            Tab tab2 = bar.newTab();
            tab2.setText("Form ");
           tab2.setTabListener(this);
           tab2.setIcon(R.drawable.form);
            bar.addTab(tab2);

            Tab tab3 = bar.newTab();
            tab3.setText("Test");
            tab3.setTabListener(this);
            tab3.setIcon(R.drawable.question);
            bar.addTab(tab3);

        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayHomeAsUpEnabled(false);

        bar.selectTab(tab);


        Intent in=getIntent();
        Bundle b=in.getExtras();

        if (b == null )
        {
            filterSpinnerSelection = 0;
        }else
        {
            filterSpinnerSelection = b.getInt("selectedFilterOption");
        }

        String screenSwitchRequest = "tab";
        if(b!= null)
        {
           screenSwitchRequest = b.getString("Screen").toString();
        }


        if(screenSwitchRequest.equals("tab"))
        {
            bar.selectTab(tab);
        }else
        if(screenSwitchRequest.equals("tab2"))
        {
            bar.selectTab(tab2);
        }else
        if(screenSwitchRequest.equals("tab3"))
        {
            bar.selectTab(tab3);
        }else
        {
            bar.selectTab(tab);
        }



	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Fragment f = null;
		TabFragment tf = null;



		if (fragList.size() > tab.getPosition())
				fragList.get(tab.getPosition());
		
		if (f == null) {
			tf = new TabFragment();
			Bundle data = new Bundle();
			data.putInt("idx",  tab.getPosition());
            Log.d("onTabSelected","tabSelected:"+tab.getPosition()+", selectedFilter: "+filterSpinnerSelection);
            data.putInt("selectedFilterOption",  filterSpinnerSelection);  //send filter selection to the new activity
			tf.setArguments(data);
			fragList.add(tf);
		}
		else
			tf = (TabFragment) f;
		
		ft.replace(android.R.id.content, tf);
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (fragList.size() > tab.getPosition()) {
			ft.remove(fragList.get(tab.getPosition()));
		}
		
	}

}
