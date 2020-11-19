package com.example.done.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.done.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class TaskDatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 4;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String DESCRIPTION = "description";
    private static final String STATUS = "status";
    private static final String EVENT = "event";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "  + DESCRIPTION + " TEXT, " +  EVENT + " TEXT, " + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public TaskDatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(DESCRIPTION, task.getDescription());
        cv.put(EVENT, task.getEvent());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    public List<ToDoModel> getAllTasks(String event){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        //Log.d("thing", getTableAsString(TODO_TABLE));
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        if(cur.getString(cur.getColumnIndex(EVENT)).equals(event)) {
                            ToDoModel task = new ToDoModel();
                            task.setId(cur.getInt(cur.getColumnIndex(ID)));
                            task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                            task.setDescription(cur.getString(cur.getColumnIndex(DESCRIPTION)));
                            task.setEvent(cur.getString(cur.getColumnIndex(EVENT)));
                            task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                            taskList.add(task);
                        }
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
        return taskList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateEvent(String event, String newEvent) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT, newEvent);
        db.update(TODO_TABLE, cv, EVENT + "= ?", new String[] {String.valueOf(event)});
    }

    public void updateDescription(int id, String description) {
        ContentValues cv = new ContentValues();
        cv.put(DESCRIPTION, description);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

    //Deletes all tasks in an event when that event is deleted
    public void onEventDelete(String event){
        db.delete(TODO_TABLE, EVENT + "= ?", new String[] {String.valueOf(event)});
    }

    //Prints whole table for debugging purposes
    public String getTableAsString (String tableName) {
        Log.d("TAG", "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }
}
