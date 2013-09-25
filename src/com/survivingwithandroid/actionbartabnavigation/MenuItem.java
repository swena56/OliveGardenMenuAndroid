package com.survivingwithandroid.actionbartabnavigation;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy56-MSI on 9/17/13.
 */
public class MenuItem {

    private String name;
    private String stationMenuItemIsMadeAt;

    private List<String> contentsData = new ArrayList<String>();
    private List<String> contentsInstructions = new ArrayList<String>();

    private String editRequest = "false";
    private String plate;
    private float skillRating;
    private String cookTime;
    private String notesAndTips;
    private String preperationStation;

    //sets the name of the Menu Item
    public void setName(String name)
    {
        this.name = name;
    }

    //returns the stationMenuItemIsMadeAt of the Menu Item
    public String getStationMenuItemIsMadeAt()
    {
        return this.stationMenuItemIsMadeAt;
    }

    //sets the name of the Menu Item
    public void setStationMenuItemIsMadeAt(String stationMenuItemIsMadeAt)
    {
        this.stationMenuItemIsMadeAt = stationMenuItemIsMadeAt;
    }

    //returns the name of the Menu Item
    public String getName()
    {
        return this.name;
    }
    //sets the contents of the Menu Item
    public void setAllContents(List<String> list)
    {
        Log.d("MenuItem", "Contents Received by data model->" );
        this.contentsData = list;
    }


    //returns the contents of the Menu Item
    public List<String> getContents()
    {
        return this.contentsData;
    }

    //sets the instructions of the Menu Item
    public void setInstructions(List<String> list)
    {
        this.contentsInstructions = list;
    }

    //returns the instructions of the Menu Item
    public List<String> getInstructions()
    {
        return this.contentsInstructions;
    }

    //sets the preperationStation of the Menu Item
    public void setStation(String station)
    {
        this.preperationStation = station;
    }

    //returns the preperationStation of the Menu Item
    public String getStation()
    {
        return this.preperationStation;
    }

    //sets the plate of the Menu Item
    public void setPlate(String plate)
    {
        this.plate = plate;
    }

    //returns the plate of the Menu Item
    public String getPlate()
    {
        return this.plate;
    }

    //sets the skill rating of the Menu Item
    public void setSkillRating(float skillRating)
    {
        this.skillRating = skillRating;
    }

    //returns the skillRating of the Menu Item
    public float getSkillRating()
    {
        return this.skillRating;
    }

    //sets the Cook Time of the Menu Item
    public void setCookTime(String cookTime)
    {
        this.cookTime = cookTime;
    }

    //returns the cook Time of the Menu Item
    public String getCookTime()
    {
        return this.cookTime;
    }

    //sets the notes of the Menu Item
    public void setNotesAndTips(String notesAndTips)
    {
        this.notesAndTips = notesAndTips;
    }

    //returns the notes of the Menu Item
    public String getNotesAndTips()
    {
        return this.notesAndTips;
    }

    public String toString()
    {
        return "("+this.name+", "
                +this.plate+", "
                +this.cookTime+", "
                +this.preperationStation+", "
                +this.contentsData.toString()+", "
                + this.contentsInstructions.toString()+", "
                + String.valueOf(this.skillRating)+", "
                + this.notesAndTips+", "
                + this.editRequest+", "
                + this.preperationStation+", "
                +")";
    }
    MenuItem()
    {
        contentsData.clear();
        contentsInstructions.clear();
       name = "";
       plate = "Unknown Plate";
       skillRating = 0;
       cookTime = "unknown";
       notesAndTips = "No Notes";
       preperationStation = "Unknown Station";

    }

    MenuItem(String name , String plate , String cookTime, List<String> contents, List<String> instructions, float skillRating, String notes, String editRequest, String station)
    {
        this.name = name;
        this.plate = plate;
        this.cookTime = cookTime;
       this.contentsData = contents;
        this.contentsInstructions = instructions;
        contentsData.clear();
        contentsInstructions.clear();
        this.skillRating = 0;
        this.notesAndTips = notes;
       // this.editRequest = editRequest;
        this.preperationStation = station;



    }
}
