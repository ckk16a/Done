package com.example.done.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.done.Model.EventModel;

import java.util.ArrayList;
import java.util.List;

public class EventDatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "EventListDatabase";
    private static final String EVENT_TABLE = "event";
    private static final String ID = "id";
    private static final String EVENT = "event";
    private static final String CREATE_EVENT_TABLE = "CREATE TABLE " + EVENT_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + EVENT + " TEXT, "
             + " INTEGER)";

    private SQLiteDatabase db;

    public EventDatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertEvent(EventModel event){
        ContentValues cv = new ContentValues();
        cv.put(EVENT, event.getEvent());
        db.insert(EVENT_TABLE, null, cv);
    }

    public List<EventModel> getAllEvents(){
        List<EventModel> eventList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(EVENT_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        EventModel event = new EventModel();
                        event.setId(cur.getInt(cur.getColumnIndex(ID)));
                        event.setEvent(cur.getString(cur.getColumnIndex(EVENT)));
                        eventList.add(event);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return eventList;
    }

    public void updateEvent(int id, String event) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT, event);
        db.update(EVENT_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteEvent(int id){
        db.delete(EVENT_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

    //Checks if the event is already in the database
    public boolean eventExists(String event){
        Cursor cursor = null;
        event=event.replaceAll("'","\\'");
        String sql = "Select * From " + EVENT_TABLE + " where " + EVENT + " = ?";
        cursor= db.rawQuery(sql,new String[] { event });

        if(cursor.getCount()>0){
            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }
    }
}
