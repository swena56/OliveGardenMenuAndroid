package com.survivingwithandroid.actionbartabnavigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 42;

    // Database Name
    private static final String DATABASE_NAME = "database2.db";

    // Contacts table name
    private static final String TABLE_NAME = "MenuItems";
    private static final String TABLE_NAME_CONTENTS = "CONTENTS_TABLE";
    private static final String TABLE_NAME_INSTRUCTIONS = "INSTRUCTIONS_TABLE";
    private static final String TABLE_NAME_APP_DATA = "APP_DATA_TABLE";
    private static final String FILTERSELCETION = "filterSelection";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String COLUMN_1 = "name";
    private static final String COLUMN_2 = "plate";
    private static final String COLUMN_3 = "cookTime";
    private static final String COLUMN_4 = "Contents";
    private static final String COLUMN_5 = "instructions";
    private static final String COLUMN_6 = "skillRating";
    private static final String COLUMN_7 = "notes";
    private static final String COLUMN_9 = "station";
    private static final String COLUMN_8 = "edit_reguest";

    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    Context appContext;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("Database","Database helper called");
        appContext = context;

    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

       // Log.d("Database","Database Created");
        String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," 
        		+ COLUMN_1 + " TEXT, "
                + COLUMN_2 + " TEXT, " 
        		+ COLUMN_3 + " TEXT, " 
        		+ COLUMN_4 + " TEXT, " 
        		+ COLUMN_5 + " TEXT, " 
        		+ COLUMN_6 + " TEXT, "
                + COLUMN_7 + " TEXT, "
                + COLUMN_8 + " TEXT, "
                + COLUMN_9 + " TEXT "+ ")";
        db.execSQL(DATABASE_CREATE);

        String DATABASE_CREATE_CONTENTS = "CREATE TABLE " + TABLE_NAME_CONTENTS + "("
                + KEY_ID + " TEXT, "
                + COLUMN_1 + " TEXT "
               + ")";
        db.execSQL(DATABASE_CREATE_CONTENTS);

        String DATABASE_CREATE_INSTRUCTIONS = "CREATE TABLE " + TABLE_NAME_INSTRUCTIONS + "("
                + KEY_ID + " TEXT, "
                + COLUMN_1 + " TEXT "
                + ")";
        db.execSQL(DATABASE_CREATE_INSTRUCTIONS);

        db.execSQL("CREATE TABLE HAS_LOADED_DEFAULT_DATA ("
                + KEY_ID + " TEXT )");

        db.execSQL("CREATE TABLE "+TABLE_NAME_APP_DATA + "("
                + FILTERSELCETION + " INT(1) )");



    }





    public void addContentsItem(String id, String text)
    {
        log("Contents added -> "+text+ " under id: "+ id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(COLUMN_1, text);

        // Inserting Row
        if (db != null)
        {
            db.insert(TABLE_NAME_CONTENTS, null, values);
        }
        db.close();
    }

    public void addInstructionItem(String id, String text)
    {
        log("Instructions added -> "+text+ " under id: "+ id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(COLUMN_1, text);

        // Inserting Row
        if (db != null)
        {
            db.insert(TABLE_NAME_INSTRUCTIONS, null, values);
        }
        db.close();
    }

    void add(MenuItem dataModel) {

        Log.d("Database","Add: "+dataModel.toString());
        Log.d("Database","Add: "+ dataModel.getContents().toString());
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("Database","got writeable database");

        ContentValues values = new ContentValues();

        values.put(COLUMN_1, dataModel.getName());
        values.put(COLUMN_2, dataModel.getPlate().toString());
        values.put(COLUMN_3, dataModel.getCookTime().toString());
        values.put(COLUMN_4, dataModel.getContents().toString());
        values.put(COLUMN_5, dataModel.getInstructions().toString());
        values.put(COLUMN_6, dataModel.getSkillRating()+"");
        values.put(COLUMN_7, dataModel.getNotesAndTips().toString());
        values.put(COLUMN_8, "false");
        values.put(COLUMN_9, dataModel.getStation().toString());

        Log.d("ContentsProblem", "during add: " + dataModel.getContents().toString());



        // Inserting Row
        if (db != null)
        {
            db.insert(TABLE_NAME, null, values);
        }

        //add contents
        for(int i = 0; i< dataModel.getContents().size(); i ++)
        {
            addContentsItem(dataModel.getName()+" ("+dataModel.getStation()+")",dataModel.getContents().get(i));
        }

        //add instructions
        for(int i = 0; i< dataModel.getInstructions().size(); i ++)
        {
            addInstructionItem(dataModel.getName()+" ("+dataModel.getStation()+")", dataModel.getInstructions().get(i));
        }


        db.close(); // Closing database connection
    }

    private void log(String text)
    {
        Log.d("Database",text);
    }


    public void setFilterSelection(int filterSelectionToSet)
    {
        Log.d("database","setFilterSelection: "+filterSelectionToSet);
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FILTERSELCETION, filterSelectionToSet);


        db.execSQL("DELETE FROM " + TABLE_NAME_APP_DATA);  //delete existing data
        // Inserting Row
        if (db != null)
        {
            db.insert(TABLE_NAME_APP_DATA, null, values);
        }

        db.close();
    }

    public int getFilterSelection()
    {

        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME_APP_DATA ,null);
        c.moveToFirst();
        if(c.getCount() == 0)
        {
            Log.d("database","getFilterSelection: has never been set");
        }
        int filterSelection = c.getColumnIndex(FILTERSELCETION);
        Log.d("database","getFilterSelection: "+filterSelection);
        db.close();
      return filterSelection;
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        Log.d("Database","Upgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INSTRUCTIONS);
        db.execSQL("DROP TABLE IF EXISTS HAS_LOADED_DEFAULT_DATA" );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_APP_DATA);
        // Create tables again
        onCreate(db);




    }

    public List<String> getContents(String id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME_CONTENTS + " WHERE + "+ KEY_ID + " = '"+ id +"'" ,null);
        List<String> list = new ArrayList< String >();
        c.moveToFirst();
        for(int i = 0; i<c.getCount(); i++)
        {

            list.add(c.getString(c.getColumnIndex(COLUMN_1)));
            log("Getting Contents at id "+id+ " contents "+ c.getString(c.getColumnIndex(COLUMN_1)));
            c.moveToNext();
        }

        db.close();

        return list ;
    }

    public List<String> getInstructions(String id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME_INSTRUCTIONS + " WHERE + "+ KEY_ID + " = '"+ id +"'" ,null);
        List<String> list = new ArrayList< String >();
        c.moveToFirst();
        for(int i = 0; i<c.getCount(); i++)
        {

            list.add(c.getString(c.getColumnIndex(COLUMN_1)));
            log("Getting instructions at id "+id+ " instructions "+ c.getString(c.getColumnIndex(COLUMN_1)));
            c.moveToNext();
        }

        db.close();

        return list ;
    }

    public boolean isEmpty()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME ,null);
        c.moveToFirst();

        if (c.getCount() == 0)
        {
            log("data base is empty");
            db.close();
            return true;

        }else
        {
            log("data base has a size of "+ c.getCount()+" return not empty");
            db.close();
            return false;
        }


    }

    public String getIdOfRequestedUpdate()
    {

        log("Get Id of requested update");
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_8 + " = 'true'",null);
        c.moveToFirst();
        log("ID found = " + c.getString(c.getColumnIndex(KEY_ID)));

        String id = c.getString(c.getColumnIndex(KEY_ID));

        db.close();
        return id;
    }

    public void setEditRequestAtId(int id, Boolean trueOrFalse)
    {

    	//creates readable and writeable database
    	SQLiteDatabase db = this.getWritableDatabase();
    	String sql = "";
    	if(trueOrFalse == true)
    	{

    		sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_8 + " = 'true'"
       	    	 + " WHERE " + KEY_ID + " = '"+id+"'";
    		db.execSQL(sql);	
        	db.close();
            log("Found Edit request was at id: "+id + " was set to true");
    	} else
		if(trueOrFalse == false)
		{
			sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_8 + " = 'false'"
	   	    	 + " WHERE " + KEY_ID + " = '"+id+"'";
			db.execSQL(sql);	
	    	db.close();
            log("Found Edit request was at id: "+id + " was set to false");
		}
    }	
    
    public void removeEditRequest()
    {
    	 //creates readable and writeable database
    	 SQLiteDatabase db = this.getWritableDatabase();
    	
    	 String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_8 + " = 'false'"
    	 + " WHERE " + COLUMN_8 + " = 'true'";
    	 
    	 db.execSQL(sql);
    	 db.close();
    }


    //returns true if the data base already contains the parameter dataModel
    public boolean doesDatabaseContain(MenuItem dataModel)
	{
    	Log.d("Does Database Contain Method","getting readable database ->"+dataModel.toString());
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor c = db.rawQuery(SELECT_ALL,null); 
		c.moveToFirst();
		 
    	Log.d("Does Database Contain Method","This Model-> "+dataModel.toString());

		 
		 
    	Log.d("Does Database Contain Method","Looping through data base");
		 //loop through and add data base to list
		 for(int i = 0; i<c.getCount(); i++)
		 {
			 

             Log.d("Does Contains Method","DataModel: "+dataModel.getName()+" -> "+c.getString(c.getColumnIndex(COLUMN_1)));
             if(dataModel.getName().equals(c.getString(c.getColumnIndex(COLUMN_1))))
             {
                 Log.d("Does Database Contain Method","Match Found -> return true");
                 db.close();
                 return true;
             }

			c.moveToNext();
		 }
		 Log.d("Does Database Contain Method","Match NOT Found -> return false");
		db.close();
		return false;
	}
    
    public void logContentsOfDatabase()
    {
    	//get readable database
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	
    	//try using get list
    	Log.d("Database Contents","");    	
    	db.close();
    }
    
    //returns true if there is a edit request
    public boolean isThereAEditRequest()
    {
        log("Fuction is there an edit request called");
    	//get readable database
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	 Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_8 + " = 'true'",null);
		 c.moveToFirst();
    	
		 if(c.getCount() >0)
		 {
             log("Edit Request = true");
			 db.close();
             return true;

		 }else
		 {
             log("Edit Request = false");
             db.close();
			 return false;
		 }
    }
    
    //returns null if no request was made
    public MenuItem getDataModelAtId(int id)
    {
        //Log.d("Database","get data model at id"+id);
    	//get readable database
    	SQLiteDatabase db = this.getReadableDatabase();

    	 Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ KEY_ID + " = "+id,null);
		 c.moveToFirst();

		 if(c.getCount() >0)
		 {

		 MenuItem dataModel;
		//check to make sure only one was found

			 dataModel = new MenuItem();

			 dataModel.setName(c.getString(c.getColumnIndex(COLUMN_1)));
			 dataModel.setPlate(c.getString(c.getColumnIndex(COLUMN_2)));
			 dataModel.setCookTime(c.getString(c.getColumnIndex(COLUMN_3)));

             String contents = c.getString(c.getColumnIndex(COLUMN_4));
             String instructions = c.getString(c.getColumnIndex(COLUMN_5));

			 dataModel.setAllContents(new ArrayList<String>());
			 dataModel.setInstructions(new ArrayList<String>());
			 dataModel.setSkillRating(0.0f);
             dataModel.setNotesAndTips(c.getString(c.getColumnIndex(COLUMN_7)));
             dataModel.setStation(c.getString(c.getColumnIndex(COLUMN_9)));

			 db.close();
			 return dataModel;
		 }else
		 {
			 return null;
		 }


    }

    public MenuItem getEditRequestDataModel()
    {

    	//get readable database
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	 Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_8 + " = 'true'",null);
		 c.moveToFirst();
        log("Number of matching dataModels: "+ c.getCount());



        MenuItem dataModel;
		//check to make sure only one was found


			 dataModel = new MenuItem();

			 dataModel.setName(c.getString(c.getColumnIndex(COLUMN_1)));
			 dataModel.setPlate(c.getString(c.getColumnIndex(COLUMN_2)));
			 dataModel.setCookTime(c.getString(c.getColumnIndex(COLUMN_3)));
            dataModel.setStation(c.getString(c.getColumnIndex(COLUMN_9)));
            dataModel.setNotesAndTips(c.getString(c.getColumnIndex(COLUMN_7)));
            dataModel.setAllContents(getContents(c.getString(c.getColumnIndex(COLUMN_1))+" ("+c.getString(c.getColumnIndex(COLUMN_9))+")"));
            dataModel.setInstructions(getInstructions(c.getString(c.getColumnIndex(COLUMN_1))+" ("+c.getString(c.getColumnIndex(COLUMN_9))+")"));
			 db.close();
        log("Get Edit Request Data Model->"+ dataModel.toString());
			 return dataModel;
			 
		 
    }
    
    public int getSizeOfDB()
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	Cursor c = db.rawQuery(SELECT_ALL,null); 
    	int size = c.getCount();
    	db.close();
    	
    	return size;
    }
    
    public void deleteItem(MenuItem dataModel)
    {
    	 //creates readable and writeable database
   	 SQLiteDatabase db = this.getWritableDatabase();
   	 
   	 //sql delete command that compares to data Model
   	 String sql = "DELETE FROM " + TABLE_NAME
   	 + " WHERE " + COLUMN_1 + " = '"+dataModel.getName().toString() +"'";


   	 Log.d("Database","Item was Deleted");
   	 
   	 db.execSQL(sql);
   	 db.close();
    	
    }
    
    public void deleteall(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.delete(TABLE_NAME_CONTENTS,null,null);

        db.delete(TABLE_NAME_INSTRUCTIONS,null,null);
        db.delete(TABLE_NAME_APP_DATA,null,null);
        db.close();
    }




    //update- everything seems to update fine expect for the name, I may need to switch to id
    public void updateDataModel(String idToUpdate,MenuItem replacementDataModel) {


        log("Id to update: "+idToUpdate);
        //log("Old Data - >" );
        log("Data model to update with: " + replacementDataModel.toString());

        //save data-  update
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET "
                + COLUMN_1 + " = '" + replacementDataModel.getName()+"', "
                + COLUMN_2 + " = '" + replacementDataModel.getPlate()+"', "
                + COLUMN_3 + " = '" + replacementDataModel.getCookTime()+"', "
                + COLUMN_4 + " = '" + replacementDataModel.getContents()+"', "
                + COLUMN_5 + " = '" + replacementDataModel.getInstructions()+"', "
                + COLUMN_6 + " = '" + replacementDataModel.getSkillRating()+"', "
                + COLUMN_7 + " = '" + replacementDataModel.getNotesAndTips()+"', "
                + COLUMN_8 + " = '" + "true" +"', "
                + COLUMN_9 + " = '" + replacementDataModel.getStation()+"'"

                + " WHERE " + KEY_ID + " = '" +  idToUpdate+ "'"
                ;

        log("SQL UPDATE STATEMENT; "+sql);
        db.execSQL(sql);


        String sqlContentsDelete = "DELETE FROM "+TABLE_NAME_CONTENTS +" WHERE "+ KEY_ID +  " = '"+ replacementDataModel.getName() + "'";
        db.execSQL(sqlContentsDelete);


        String sqlIInstructionDelete = "DELETE FROM "+TABLE_NAME_INSTRUCTIONS +" WHERE "+ KEY_ID +  " = '"+ replacementDataModel.getName() + "'";
        db.execSQL(sqlIInstructionDelete);

        //add contents
        for(int i = 0; i< replacementDataModel.getContents().size(); i ++)
        {
            addContentsItem(replacementDataModel.getName()+ "("+replacementDataModel.getStation()+")",replacementDataModel.getContents().get(i));
        }

        //add contents
        for(int i = 0; i< replacementDataModel.getInstructions().size(); i ++)
        {
            addInstructionItem(replacementDataModel.getName()+ "("+replacementDataModel.getStation()+")", replacementDataModel.getInstructions().get(i));
        }

        db.close();
        log("update data Model Complete");
    }

    public void addDefaultData()
    {
        List<String>  contents = new ArrayList<String>();
        List<String> instructions = new ArrayList<String>();

        //item 1
        MenuItem temp = new MenuItem();
        temp.setName("Chicken Alfredo ");  //name of dish
        temp.setStation("Grill");
        contents.clear();
        contents.add("2 peices - 3 oz chicken");
        temp.setAllContents(contents);
        instructions.clear();
        instructions.add("place chicken on grill");
        temp.setInstructions(instructions);
        temp.setPlate("Can't Leak");
        temp.setCookTime("4-6 Minutes");
        temp.setSkillRating(0.0f);
        temp.setNotesAndTips("Notes");
        add(temp);



        //Chicken Alfredo on Window
        temp = new MenuItem();
        temp.setName("Chicken Alfredo");  //name of dish
        temp.setStation("Window");
        contents.clear();
        temp.setAllContents(contents);
        instructions.clear();
        instructions.add("Make sure dish is made properly");
        instructions.add("Organize dish with other table items");
        temp.setInstructions(instructions);
        temp.setPlate("Bowl");
        temp.setCookTime("1-3 Minutes");
        temp.setSkillRating(0.0f);
        temp.setNotesAndTips("Notes");
        add(temp);

        //Chicken Alfredo on Window
        temp = new MenuItem();
        temp.setName("Chicken and Shrimp Carbonara");  //name of dish
        temp.setStation("Window");
        contents.clear();
        contents.add("1 Green Scoop - Ziti Topping");
        temp.setAllContents(contents);
        instructions.clear();
        instructions.add("Make sure dish is made properly");
        instructions.add("Add Ziti Topping, and melt in melter");
        instructions.add("Organize dish with other table items");
        temp.setInstructions(instructions);
        temp.setPlate("Bowl");
        temp.setCookTime("1-3 Minutes");
        temp.setSkillRating(0.0f);
        temp.setNotesAndTips("1)Ziti Topping is Golden Brown" +
                "\n2)Dish will be hot when taken out of melter");
        add(temp);


        //-----------------------------Saute--------------------------------------------
        //Braised Beef on Saute
        temp = new MenuItem();
        temp.setName("Braised Beef(D)");  //name of dish
        temp.setStation("Saute");
        contents.clear();
        contents.add("2 - braised beef pack");
        contents.add("2 fl oz - Seasoned Tomatoes");
        contents.add("16 - Tortelloni");
        contents.add("?6 fl oz - Creamy Marsala");
        temp.setAllContents(contents);
        instructions.clear();
        instructions.add("Drop tortelloni in pasta boiler");
        instructions.add("Add braised beef packs to saute pan with oil");
        instructions.add("Add seasoned Tomatoes to pan");
        instructions.add("When braised beef is hot and soft to the touch add sauce.");
        instructions.add("When sauce bubbles through out add tortelloni");
        instructions.add("Mix tortelloni in sauce and add to bowl");
        temp.setInstructions(instructions);
        temp.setPlate("Bowl");
        temp.setCookTime("1-3 Minutes");
        temp.setSkillRating(0.0f);
        temp.setNotesAndTips("Make sure tomatoes, beef, and mushrooms are visible on top of dish. - No Basil ");
        add(temp);

        //L Braised Beef on Saute
        temp = new MenuItem();
        temp.setName("Braised Beef(L)");  //name of dish
        temp.setStation("Saute");
        contents.clear();
        contents.add("1 - braised beef pack");
        contents.add("1 fl oz - Seasoned Tomatoes");
        contents.add("12 - Tortelloni");
        contents.add("?4 fl oz - Creamy Marsala");
        temp.setAllContents(contents);
        instructions.clear();
        instructions.add("Drop tortelloni in pasta boiler");
        instructions.add("Add braised beef packs to saute pan with oil");
        instructions.add("Add seasoned Tomatoes to pan");
        instructions.add("When braised beef is hot and soft to the touch add sauce.");
        instructions.add("When sauce bubbles through out add tortelloni");
        instructions.add("Mix tortelloni in sauce and add to bowl");
        temp.setInstructions(instructions);
        temp.setPlate("Bowl");
        temp.setCookTime("1-3 Minutes");
        temp.setSkillRating(0.0f);
        temp.setNotesAndTips("Make sure tomatoes, beef, and mushrooms are visible on top of dish. - No Basil");
        add(temp);


        //carb and shrimp
        temp = new MenuItem();
        temp.setName("Chicken and Shrimp Carbonara");  //name of dish
        temp.setStation("Saute");
        contents.clear();
        contents.add("6 - unmarinated scampi halfs");
        contents.add("5 - unmarinated 25/29 shrimp");
        contents.add("3 oz - roasted red peppers");
        contents.add("1 - panchetta pack");
        contents.add("6 oz - Bucatini pasta");
        temp.setAllContents(contents);
        instructions.clear();
        temp.setInstructions(instructions);
        temp.setPlate("Bowl");
        temp.setCookTime("1-3 Minutes");
        temp.setSkillRating(0.0f);
        temp.setNotesAndTips("1)");
        add(temp);

        //Capellini Pomodoro (D)
        temp = new MenuItem();
        temp.setName("Capellini Pomodoro (D)");  //name of dish
        temp.setStation("Saute");
        contents.clear();
        contents.add("3 oz - Toscana Blend");
        contents.add("2 fl oz - White wine");
        contents.add("6 oz - Marinara sauce");
        contents.add("8 oz - Capellini Pasta");
        temp.setAllContents(contents);
        instructions.clear();
        temp.setInstructions(instructions);
        temp.setPlate("Bowl");
        temp.setCookTime("1-3 Minutes");
        temp.setSkillRating(0.0f);
        temp.setNotesAndTips("1)");
        add(temp);

        //Capellini Pomodoro (L)
        temp = new MenuItem();
        temp.setName("Capellini Pomodoro (L)");  //name of dish
        temp.setStation("Saute");
        contents.clear();
        contents.add("3 oz - Toscana Blend");
        contents.add("2 fl oz - White wine");
        contents.add("4 oz - Marinara sauce");
        contents.add("6 oz - Capellini Pasta");
        temp.setAllContents(contents);
        instructions.clear();
        temp.setInstructions(instructions);
        temp.setPlate("Bowl");
        temp.setCookTime("1-3 Minutes");
        temp.setSkillRating(0.0f);
        temp.setNotesAndTips("1)");
        add(temp);

        //Chicken Marsala
        temp = new MenuItem();
        temp.setName("Chicken Marsala");  //name of dish
        temp.setStation("Saute");
        contents.clear();
        contents.add("3 - 3 oz chicken ");
        contents.add("6 oz - Sliced mushrooms");
        contents.add("4 oz - Marsala sauce");
        temp.setAllContents(contents);
        instructions.clear();
        temp.setInstructions(instructions);
        temp.setPlate("Dinner Plate");
        temp.setCookTime("7-8 Minutes");
        temp.setSkillRating(0.0f);
        temp.setNotesAndTips("1)");
        add(temp);


        //Chicken Alfredo on Saute
        temp = new MenuItem();
        temp.setName("Chicken Alfredo");  //name of dish
        temp.setStation("Saute");
        contents.clear();
        contents.add("8 oz - Alfredo");
        contents.add("8 oz - Fettucini");
        temp.setAllContents(contents);
        instructions.clear();
        instructions.add("Add Chicken to Saute Pan");
        instructions.add("Drop Pasta in water");
        instructions.add("Alfredo to Saute Pan");
        instructions.add("Drain pasta and to Saute Pan");
        instructions.add("Spin pasta in Sauce and add to bowl");
        temp.setInstructions(instructions);
        temp.setPlate("Bowl");
        temp.setCookTime("1-3 Minutes");
        temp.setSkillRating(0.0f);
        temp.setNotesAndTips("Notes");
        add(temp);




        //add(new MenuItem("Chicken Alfredo", "Bowl", "4-6 Minutes",contents, instructions, 0.0f, "notes", "false", "Saute"));



    }
}
