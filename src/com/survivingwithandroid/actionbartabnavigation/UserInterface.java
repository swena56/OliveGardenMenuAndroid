package com.survivingwithandroid.actionbartabnavigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andy56-MSI on 9/18/13.
 */
class UserInterface
{

    //View v
    private View v;


    //objects listed by order of use
    private RadioGroup group;
    private RadioButton radioButtonLunch;
    private RadioButton radioButtonDinner;
    private Button saveButton;
    private EditText editTextName;
    private Spinner spinnerPlateSelection;      //xml
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner spinnerCookTimeSelection;      //xml
    private ArrayAdapter<String> spinnerCookTimeAdapter;
    private Spinner spinnerStationSelection;      //xml
    private ArrayAdapter<String> spinnerStationAdapter;
    private List<String> list;
    private List<String> list2CookTime;
    private List<String> listStation;
    private String spinnerPlateSelectionString = "";
    private int indexOfCurrentSpinnerSelection;
    private Activity activity;



    List<String> contents;
    ListView listViewContents;
    ArrayAdapter<String> listViewContentsAdapter;

    List<String> instructions;
    ListView listViewInstructions;
    ArrayAdapter<String> listViewInstructionsAdapter;

    RatingBar skillRating;
    EditText notes;

    public void setindexOfCurrentSpinnerSelection(int i)
    {
        indexOfCurrentSpinnerSelection = i;
    }

    public int getIndexOfCurrentSpinnerSelection()
    {
        return indexOfCurrentSpinnerSelection;
    }

    private void log(String text)
    {
        Log.d("UserInterface", text);
    }

     public void clearForm()
    {
        intializeNameEditBox("");
        intializeSpinnerPlate("Tour Plate");
        intializeSpinnerCookTime("1 Minute");
        intializeSpinnerStation("Window");
        intializeLunchDinnerRadioSelect(1);
        intializeContents(new ArrayList<String>());
        intializeInstructions(new ArrayList<String>());
        intializeSkillRating(1);
        intializeNotes("");
    }

    public void populateFormWithDataModel()
    {
        Log.d("pop","start");
        DatabaseHelper db = new DatabaseHelper(activity);
        log("Population");
        if(db.isThereAEditRequest()==true)
        {

            MenuItem menuItem = new MenuItem();
            menuItem = db.getEditRequestDataModel();
            Log.d("pop","data to pop ->"+menuItem.toString());
                log("Populating form with "+menuItem.toString());

            intializeButtonText("Update");
            intializeNameEditBox(menuItem.getName().toString());
            intializeSpinnerPlate(menuItem.getPlate().toString());
            intializeSpinnerCookTime(menuItem.getCookTime().toString());
            intializeSpinnerStation(menuItem.getStation().toString());
            intializeContents(menuItem.getContents());
            intializeLunchDinnerRadioSelect(1);


            intializeInstructions(menuItem.getInstructions());

            intializeSkillRating(menuItem.getSkillRating());
            intializeNotes(menuItem.getNotesAndTips().toString());

        }
        Log.d("pop","end");
    }


    public UserInterface(View v, Activity act)
{
    this.v = v;
    this.activity = act;
    //intialize with default values

    contents = new ArrayList<String>();
    contents.add("***Click here to add Contents***");
    instructions = new ArrayList<String>();
    instructions.add("***Click here to add Instructions***");

    Log.d("ContentsProblem","before intialization: "+contents.toString());

    intializeNameEditBox("");
    intializeSpinnerPlate("Tour Plate");
    intializeSpinnerCookTime("1 Minute");
    intializeSpinnerStation("Window");
    intializeLunchDinnerRadioSelect(1);
    intializeContents(new ArrayList<String>());
    intializeInstructions(new ArrayList<String>());
    intializeSkillRating(0);
    intializeNotes("");     //intially empty
}

    private void intializeButtonText(String text)
    {
        saveButton = (Button) v.findViewById(R.id.buttonSave);
        saveButton.setText(text);
    }

    private void intializeNameEditBox(String text) {
        editTextName = (EditText) v.findViewById(R.id.editTextMenuItemName);
        editTextName.setText(text);
    }

    public String getName()
    {
        return editTextName.getText().toString();
    }

    public void intializeLunchDinnerRadioSelect(int whichToSelect)
    {
        group = (RadioGroup)v.findViewById(R.id.radioGroup1);

        radioButtonLunch = (RadioButton)v.findViewById(R.id.radio1);
        radioButtonDinner = (RadioButton)v.findViewById(R.id.radio2);

        //set radio button according to parameter
        if(whichToSelect==0)
        {
            group.clearCheck();
            radioButtonLunch.setChecked(true);
        }else
        if(whichToSelect==1)
        {
            group.clearCheck();
            radioButtonDinner.setChecked(true);
        }

        //check for change

        if(group.getCheckedRadioButtonId()==R.id.radio1)
        {
            group.clearCheck();
            radioButtonLunch.setChecked(true);
        }

        if(group.getCheckedRadioButtonId()==R.id.radio2)
        {
            group.clearCheck();
            radioButtonDinner.setChecked(true);
        }


    }


    public void intializeSpinnerPlate(String typeOfplate)
    {
        log("intializeSpinner -load default list -load default adapter");
        list = new ArrayList<String>();
        list.add("Can't Leak");
        list.add("Tour Plate");
        list.add("Dinner Plate");
        list.add("Lunch Plate");
        list.add("Soup Bowl");
        list.add("Bowl");
        list.add("Flatbread Plate");
        list.add("Brodetto Bowl");
        list.add("Boat");
        list.add("Ramakin");

        int selectedID = list.indexOf(typeOfplate);
        log("Type of plate given to Spinner is: "+typeOfplate+" which corresponds to index: "+selectedID);

        this.spinnerPlateSelection = (Spinner) v.findViewById(R.id.spinner);
        this.spinnerAdapter = new ArrayAdapter<String>(
                activity,
                android.R.layout.simple_spinner_item,
                list);

        this.spinnerPlateSelection.setAdapter(spinnerAdapter);
        this.spinnerPlateSelection.setSelection(selectedID);


        spinnerPlateSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                log("spinner was set to index "+i);

                //setSpinnerPlateSelectString(adapterView.getSelectedItem().toString());
                //setindexOfCurrentSpinnerSelection(i);
                log("on Item Click->"+spinnerPlateSelectionString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //setSpinnerPlateSelectString(list.get(0));
            }
        });
    }

    public String getPlate()
    {
        return spinnerPlateSelection.getSelectedItem().toString();
    }

    public void intializeSpinnerCookTime(String cookTimeIntializationStartValue)
    {
        log("intializeSpinner -load default list -load default adapter");
        list2CookTime = new ArrayList<String>();
        list2CookTime.add("1 Minute");
        list2CookTime.add("1-3 Minutes");
        list2CookTime.add("4-6 Minutes");
        list2CookTime.add("7-8 Minutes");

        int selectedID = list2CookTime.indexOf(cookTimeIntializationStartValue);

        this.spinnerCookTimeSelection = (Spinner) v.findViewById(R.id.spinnerCookTime);
        this.spinnerCookTimeAdapter = new ArrayAdapter<String>(
                activity,
                android.R.layout.simple_spinner_item,
                list2CookTime);

        this.spinnerCookTimeSelection.setAdapter(spinnerCookTimeAdapter);
        this.spinnerCookTimeSelection.setSelection(selectedID);
        //spinnerPlateSelection.getSelectedItemId();
        spinnerCookTimeSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                log("spinner was set to index "+i);

                //setSpinnerPlateSelectString(adapterView.getSelectedItem().toString());
                //setindexOfCurrentSpinnerSelection(i);
               // log("on Item Click->"+spinnerPlateSelectionString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //setSpinnerPlateSelectString(list.get(0));
            }
        });
    }
    public String getCookTime()
    {
        return spinnerCookTimeSelection.getSelectedItem().toString();
    }

    public void intializeSpinnerStation(String starterStationIntializationValue)
    {
        log("intialize Station Spinner -load default list -load default adapter");
        listStation= new ArrayList<String>();
        listStation.add("Window");
        listStation.add("Appitizers");
        listStation.add("Grill");
        listStation.add("Saute");
        listStation.add("ProtienPrep");
        listStation.add("VeggiePrep");
        listStation.add("SaucePrep");
        listStation.add("PastaPrep");

        int selectedID = listStation.indexOf(starterStationIntializationValue);


        this.spinnerStationSelection = (Spinner) v.findViewById(R.id.spinnerStation);
        this.spinnerStationAdapter = new ArrayAdapter<String>(
                activity,
                android.R.layout.simple_spinner_item,
                listStation);

        this.spinnerStationSelection.setAdapter(spinnerStationAdapter);
        log("station id used "+selectedID);
        this.spinnerStationSelection.setSelection(selectedID);
    }

    public String getStation()
    {
        return spinnerStationSelection.getSelectedItem().toString();
    }

    public void intializeContents(List<String> list)
    {
        //reset data in list

        List<String> addNumberIndex = new ArrayList<String>();

        if(list.isEmpty())
        {
            contents = new ArrayList<String>();  //used for actual data content

        }else
        {
           contents = list;
          // addNumberIndex = list;
        }

        addNumberIndex.add(0,"***Click here to add Contents***");

        // add numbering to list
        for(int listIndex = 0; listIndex < contents.size(); listIndex++)
        {
            addNumberIndex.add( listIndex+1  + ") "+ contents.get(listIndex));
        }



        listViewContents = (ListView)v.findViewById(R.id.listViewContents);
        //create adapter
        listViewContentsAdapter = new ArrayAdapter<String>(
                activity,
                android.R.layout.simple_gallery_item,
                addNumberIndex);
        Log.d("Database","Contents during intialzation : "+contents.toString());
        listViewContents.setAdapter(listViewContentsAdapter);
                            listViewContents.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                PopupWindow mpopup;
                                View view1;
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    //if the first item on the list is clicked open add window
                                    if(i == 0)
                                    {
                                        Log.d("gatherFormData","add was clicked");

                                        view1 = activity.getLayoutInflater().inflate(R.layout.collect_contents_data, null);
                                        view1.setBackgroundColor(Color.GRAY);

                                        mpopup = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                                        mpopup.setFocusable(true);



                                        mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                                        mpopup.showAtLocation(view1, Gravity.CENTER,Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL+50);

                                        Button cancel = (Button) view1.findViewById(R.id.buttonCancel);
                                        cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                mpopup.dismiss();
                                            }
                                        });
                                        Button saveContents = (Button) view1.findViewById(R.id.buttonSave);
                                        saveContents.setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View view) {
                                                EditText contentsItem = (EditText)view1.findViewById(R.id.editTextContentsItem);
                                                EditText contentsAmount = (EditText)view1.findViewById(R.id.editTextContentsAmount);
                                                DatabaseHelper db = new DatabaseHelper(activity);



                                                if(contentsItem.getText().toString().equals("") || contentsAmount.getText().toString().equals(""))
                                                {

                                                    Toast warning = Toast.makeText(activity,"No contents where add due to empty field(s)",Toast.LENGTH_SHORT);
                                                    warning.show();
                                                }else
                                                {   //Log.d("Database","contents where just saved: "+contentsItem.getText().toString());
                                                    String contentsString = contentsAmount.getText().toString()+" - "+contentsItem.getText().toString();
                                                    addToContentsList(contentsString);
                                                    intializeContents(contents);

                                                }




                                                mpopup.dismiss();
                                            }
                                        });
                                    }
                                    else
                                    {
                                        log("Delete dialog option");
                                        //confirmation to delete
                                        final int contentToRemove = i;
                                        new AlertDialog.Builder(activity)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .setTitle("Delete Confirmation")
                                                .setMessage("Are you sure you want to delete the contents item -> "+contents.get(i))
                                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        contents.remove(contentToRemove-1);
                                                        listViewContents.setAdapter(listViewContentsAdapter);

                                                        intializeContents(contents);
                                                        //Stop the activity
                                                       // activity.this.finish();
                                                    }

                                                })
                                                .setNegativeButton("Cancel", null)
                                                .show();


                                        //delete that item in list
                                    }

                                }
                            });
    }




    private void addToContentsList(String text)
    {
        contents.add(text);
    }

    public List<String> getContents()
    {

        return contents;
    }

    public void intializeInstructions(List<String> list)
    {
        //reset data in list

        List<String> addNumberIndex = new ArrayList<String>();

        if(list.isEmpty())
        {
            instructions = new ArrayList<String>();  //used for actual data content

        }else
        {
            instructions = list;
            // addNumberIndex = list;
        }

        addNumberIndex.add(0,"***Click here to add a Step***");

        // add numbering to list
        for(int listIndex = 0; listIndex < instructions.size(); listIndex++)
        {
            addNumberIndex.add(listIndex + 1 + ") " + instructions.get(listIndex));
        }



        listViewInstructions = (ListView)v.findViewById(R.id.listViewInstructions);
        //create adapter
        listViewInstructionsAdapter = new ArrayAdapter<String>(
                activity,
                android.R.layout.simple_gallery_item,
                addNumberIndex);

        listViewInstructions.setAdapter(listViewInstructionsAdapter);
        listViewInstructions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            PopupWindow mpopup;
            View view1;
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //if the first item on the list is clicked open add window
                if(i == 0)
                {
                    Log.d("gatherFormData", "add was clicked");

                    view1 = activity.getLayoutInflater().inflate(R.layout.collect_instruction_data, null);
                    view1.setBackgroundColor(Color.GRAY);

                    mpopup = new PopupWindow(view1, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                    mpopup.setFocusable(true);



                    mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                    mpopup.showAtLocation(view1, Gravity.CENTER,Gravity.CENTER_HORIZONTAL,Gravity.CENTER_VERTICAL+50);

                    Button cancel = (Button) view1.findViewById(R.id.buttonCancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mpopup.dismiss();
                        }
                    });
                    Button saveContents = (Button) view1.findViewById(R.id.buttonSave);
                    saveContents.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            EditText editTextInstruction = (EditText)view1.findViewById(R.id.editTextInstructionItem);

                            DatabaseHelper db = new DatabaseHelper(activity);



                            if(editTextInstruction.getText().toString().equals(""))
                            {

                                Toast warning = Toast.makeText(activity,"No contents where add due to empty field(s)",Toast.LENGTH_SHORT);
                                warning.show();
                            }else
                            {   //Log.d("Database","contents where just saved: "+contentsItem.getText().toString());
                                String contentsString = editTextInstruction.getText().toString();
                                addToInstructionList(contentsString);
                                intializeInstructions(instructions);

                            }




                            mpopup.dismiss();
                        }
                    });
                }


            }
        });
    }


    private void addToInstructionList(String text)
    {
        instructions.add(text);
    }


    public List<String> getInstructions()
    {
        return instructions;
    }

    public void intializeSkillRating(float rating)
    {
        //rating bar
        skillRating = (RatingBar) v.findViewById(R.id.ratingBar);
        skillRating.setRating(rating);


    }

    public float getSkillRating()
    {
        return (float)skillRating.getRating();
    }


    public void intializeNotes(String text)
    {
        notes = (EditText) v.findViewById(R.id.editTextNotes);
        notes.setText(text);
    }

    public String getNotes()
    {
        return notes.getText().toString();
    }

}
