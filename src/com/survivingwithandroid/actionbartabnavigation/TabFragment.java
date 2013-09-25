package com.survivingwithandroid.actionbartabnavigation;

//android imports

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

//java imports

public class TabFragment extends Fragment {
    //index variable of tab used to inflate view
    //-tab 0 - list
    //-tab 1 - form
    //-tab 2 - knowledge testing
	private int index;

    //holds the list data
    List<MenuItem> listOfMenuItems = new ArrayList<MenuItem>();

    //View
    View v;

    //create adapters
    ArrayAdapter<String> listViewAdapter;
    ArrayAdapter<String> listViewInstructionsAdapter;

    //xml components
    EditText editTextName;
    EditText notes;
    Spinner spinnerPlateSelection;
    Spinner spinnerCookTime;
    ListView listViewContents;
    ListView listViewInstructions;
    RatingBar skillRating;
    Button buttonSave;
    Button buttonCancel;
    Button buttonDelete;

    int filterSelectedId = 0;

    Intent intent;

    //user interface
    UserInterface ui;

   // private ViewPager viewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		Bundle data = getArguments();
		index = data.getInt("idx");
        filterSelectedId = data.getInt("selectedFilterOption");

        Log.d("onCreate","getFilterSelectionID: "+filterSelectedId);





    }

    @Override
    public void onPause() {
        super.onPause();
        if(index == 1)
        {
            DatabaseHelper db = new DatabaseHelper(getActivity());
            db.removeEditRequest();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();




    }

    public void fillList(View v, Spinner filterSpinner)
    {
        //create adapter
        myAdapter adapter = new myAdapter();

        //intialize list view
        ListView menuItemListView = (ListView) v.findViewById(R.id.listView1);


        DatabaseHelper db = new DatabaseHelper(getActivity());
        // db.removeEditRequest();  //if drawing list reset edit request



        adapter.clear();
        MenuItem addReferenceMenuItem =  new MenuItem();
        addReferenceMenuItem.setName("Menu Items According To Station");
        addReferenceMenuItem.setStation("--Click to add Menu Item--");
        adapter.add(addReferenceMenuItem);


        Log.d("Database", "Filling list");
        for(int i = 0; i<= db.getSizeOfDB(); i++)
        {
            Log.d("list", "Filling list -> at id: "+i + " dataModel->"+db.getDataModelAtId(i));
            MenuItem tempDataModel = db.getDataModelAtId(i);

            if(tempDataModel != null)
            {


                Log.d("list", "Spinner is set to: "+ filterSpinner.getSelectedItem().toString());
                if(!filterSpinner.getSelectedItem().toString().equals("---No Filter---"))
                {

                    //only add data models that match the filter
                   if(filterSpinner.getSelectedItem().toString().equals(tempDataModel.getStation().toString()))
                   {
                       adapter.add(tempDataModel);
                   }
                }else
                {
                    //add all, no filter
                    adapter.add(tempDataModel);
                }

            }
        }

        //set the adapter
        menuItemListView.setAdapter(adapter);
        menuItemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DatabaseHelper db = new DatabaseHelper(getActivity());

                if((db.isEmpty() == true)|| (i == 0))
                {
                    //switch to form
                    intent = new Intent(getActivity(),MainActivity.class);
                    intent.putExtra("Screen", "tab2");
                    intent.putExtra("idx", 1);
                    startActivity(intent);
                } else
                {

                    //reguest edit
                    db.setEditRequestAtId(i, true);


                    //confirmation to user
                    Toast itemSelected = Toast.makeText(getActivity(), "Data Model Selected: " + db.getEditRequestDataModel().toString(), Toast.LENGTH_LONG);
                    //itemSelected.show();


                    //hide keyboard

                    //switch to form
                    intent = new Intent(getActivity(),MainActivity.class);
                    intent.putExtra("Screen", "tab2");
                    intent.putExtra("idx", 1);
                    startActivity(intent);
                }

            }
        });
        //populate list
        //if list is intially empty add click to add to form option
        if(menuItemListView.getCount() == 0)
        {
            MenuItem dataModel = new MenuItem();
            dataModel.setName("Menu Item List is empty - Click to add Menu Item");
            adapter.add(dataModel);
        }

    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {


        //if selected tab is 0 set fragment to show list
        if(index == 0)
        {

            //inflate view with contents of list fragment xml
		    v = inflater.inflate(R.layout.list_fragment, null);


            //load filter spinner
            final Spinner filterSpinner = (Spinner)v.findViewById(R.id.spinnerFilter);
            List<String> listOfStations = new ArrayList<String>();
            listOfStations.add("---No Filter---");
            listOfStations.add("Window");
            listOfStations.add("Appitizers");
            listOfStations.add("Grill");
            listOfStations.add("Saute");
            listOfStations.add("ProtienPrep");
            listOfStations.add("VeggiePrep");
            listOfStations.add("SaucePrep");
            listOfStations.add("PastaPrep");

            filterSpinner.setAdapter(new ArrayAdapter<String>(
                    getActivity(),
                    android.R.layout.simple_spinner_item,
                    listOfStations));

            DatabaseHelper db = new DatabaseHelper(getActivity());
            filterSpinner.setSelection(db.getFilterSelection());
            //filterSpinner.setSelection(1);

            filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fillList(v, filterSpinner);
                    filterSelectedId = i;
                    DatabaseHelper db = new DatabaseHelper(getActivity());
                    db.setFilterSelection(i);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    fillList(v, filterSpinner);
                }
            });




        }

        //if selected tab is 1 set fragment to form
        if(index == 1)
        {
            //inflate view with contents of list fragment xml
            v = inflater.inflate(R.layout.fragment, null);

            //create user interface
            ui = new UserInterface(v,getActivity());
            ui.populateFormWithDataModel();



            //initalize buttons
            buttonSave = (Button) v.findViewById(R.id.buttonSave);
            //onclick listener for save button
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Save", "Save Button was pressed");
                    //create new data model and populate form

                    DatabaseHelper db = new DatabaseHelper(getActivity());
                    MenuItem dataModel = new MenuItem();
                    dataModel.setName(ui.getName());
                    dataModel.setPlate(ui.getPlate());
                    dataModel.setCookTime(ui.getCookTime());
                    Log.d("Save",ui.getContents().toString()+ " Contents gathered after save button");
                    Log.d("Save","get empty contents->"+ dataModel.getContents());
                    dataModel.setAllContents(ui.getContents());
                    dataModel.setInstructions(ui.getInstructions());
                    dataModel.setSkillRating(ui.getSkillRating());
                    dataModel.setNotesAndTips(ui.getNotes());
                    dataModel.setStation(ui.getStation());
                    Log.d("Save", dataModel.toString() + "- was saved");

                    if(!dataModel.getName().equals(""))
                    {
                        Log.d("Save", "Data base contained datamodel");
                        if(db.isThereAEditRequest()==true)
                        {
                            //update record
                            db.updateDataModel(db.getIdOfRequestedUpdate(),dataModel);

                            ui.clearForm();

                            Toast updateMesage = Toast.makeText(getActivity(),"Menu Item was Updated",Toast.LENGTH_SHORT);
                            updateMesage.show();

                            //switch back to list
                            intent = new Intent(getActivity(),MainActivity.class);
                            intent.putExtra("Screen", "tab1");
                            intent.putExtra("idx", 0);
                            startActivity(intent);

                        }else
                        {


                            Log.d("Save","Data was Saved to DB: "+dataModel.toString());

                                db.add(dataModel);

                                Toast saveSuccessful = Toast.makeText(getActivity(),dataModel.toString()+"- Was Saved.",Toast.LENGTH_SHORT);
                                //saveSuccessful.show();



                            ui.clearForm();


                            //switch back to list
                            intent = new Intent(getActivity(),MainActivity.class);
                            intent.putExtra("Screen", "tab1");
                            intent.putExtra("idx", 0);
                            startActivity(intent);
                        }
                    } else
                    {
                            Toast emptyFields = Toast.makeText(getActivity(),"Empty fields",Toast.LENGTH_SHORT);
                            emptyFields.show();
                    }
                   // Log.d("Save","DataModel obtained: "+dataModel.toString());
                    db.removeEditRequest();

                }
            });


            buttonDelete = (Button)v.findViewById(R.id.buttonDelete);

            DatabaseHelper reguestcheck = new DatabaseHelper(getActivity());
            if(reguestcheck.isThereAEditRequest())
            {
                buttonDelete.setClickable(true);
                buttonDelete.setVisibility(View.VISIBLE);
            }else
            {
                buttonDelete.setClickable(false);
                buttonDelete.setVisibility(View.INVISIBLE);
            }
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHelper db = new DatabaseHelper(getActivity());
                    if(db.isThereAEditRequest())
                    {
                        //deletes item from data base
                        db.deleteItem(db.getEditRequestDataModel());
                        ui.clearForm();
                        db.removeEditRequest();

                        //switch back to list
                        intent = new Intent(getActivity(),MainActivity.class);
                        intent.putExtra("Screen", "tab1");
                        intent.putExtra("idx", 0);
                        startActivity(intent);

                    }
                }
            });

            //if cancel button is pressed
            buttonCancel = (Button) v.findViewById(R.id.buttonCancel);
            buttonCancel.setText("Cancel");
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //remove edit requests
                    DatabaseHelper db = new DatabaseHelper(getActivity());
                    db.removeEditRequest();

                    ui.clearForm();
                    //Toast to user
                    Toast message = Toast.makeText(getActivity(),"Form Cleared",Toast.LENGTH_SHORT);
                    message.show();

                }
            });

        }

        //if selected tab is 2 set fragment to quiz
        if(index == 2)
        {
            v = inflater.inflate(R.layout.quiz_fragment, null);
            ImageView ogPicture = (ImageView)v.findViewById(R.id.imageView);
            ogPicture.setImageResource(R.drawable.oglogo);
            Button reset = (Button)v.findViewById(R.id.buttonResetData);
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseHelper db = new DatabaseHelper(getActivity());
                    db.deleteall();
                    db.addDefaultData();
                }
            });
        }
		return v;
	}

    private class myAdapter extends ArrayAdapter<MenuItem>
    {
        myAdapter() {
            super(getActivity(), R.layout.list_item, listOfMenuItems);
            Log.d("Adapter","Constructor Called");
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {

            View row = convertView;
            listViewItemHolder holder = null;


            if(row == null)
            {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                row = inflater.inflate(R.layout.list_item,null);
                holder = new listViewItemHolder(row);

                    row.setTag(holder);

            }else
            {
                holder = (listViewItemHolder)row.getTag();
            }


            holder.populateForm(listOfMenuItems.get(position));


            return row;
        }
    }  //end of class



    //used to fill the form
    static class listViewItemHolder
    {
        private TextView name = null;
        private TextView station = null;
        private ImageView icon = null;

        listViewItemHolder(View row)
        {
            name = (TextView) row.findViewById(R.id.ListItemName);
            station = (TextView) row.findViewById(R.id.ListItemStation);
            icon = (ImageView) row.findViewById(R.id.imageView);
        }

        void populateForm(MenuItem dataModel)
        {
            name.setText(dataModel.getName());
            station.setText(dataModel.getStation());

            //set icon in data
            if(dataModel.getStation().equals("Unknown Station"))
            {
                icon.setImageResource(R.drawable.unknown);
            }else
            if(dataModel.getStation().equals("Window"))
            {
                icon.setImageResource(R.drawable.window);
            }else
            if(dataModel.getStation().equals("Grill"))
            {
                icon.setImageResource(R.drawable.grill);
            }else
            if(dataModel.getStation().equals("Appitizers"))
            {
                icon.setImageResource(R.drawable.app);
            }else
            if(dataModel.getStation().equals("Saute"))
            {
                icon.setImageResource(R.drawable.saute);
            }else
            if(dataModel.getStation().equals("ProtienPrep"))
            {
                icon.setImageResource(R.drawable.protien);
            }else
            if(dataModel.getStation().equals("VeggiePrep"))
            {
                icon.setImageResource(R.drawable.veggie);
            }else
            if(dataModel.getStation().equals("SaucePrep"))
            {
                icon.setImageResource(R.drawable.sauce);
            }else
            if(dataModel.getStation().equals("PastaPrep"))
            {
                icon.setImageResource(R.drawable.pasta);
            }else
            if(dataModel.getStation().equals(""))
            {
                icon.setImageResource(R.drawable.unknown);
            } else
            if(dataModel.getStation().equals("--Click to add Menu Item--"))
            {
                icon.setImageResource(R.drawable.unknown);
            }




        }
    }

}
