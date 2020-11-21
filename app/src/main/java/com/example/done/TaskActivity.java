package com.example.done;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.done.Model.EventModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.done.Adapters.ToDoAdapter;
import com.example.done.Model.ToDoModel;
import com.example.done.Utils.TaskDatabaseHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TaskActivity extends AppCompatActivity implements DialogCloseListener{

    private TaskDatabaseHandler db;

    private RecyclerView tasksRecyclerView;
    private TextView noTask;
    private TextView createTask;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;

    private List<ToDoModel> taskList;

    private TextView textView;

    private String event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        textView = findViewById(R.id.tasksText);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                event= null;
            } else {
                event= extras.getString("eventName");
            }
        } else {
            event= (String) savedInstanceState.getSerializable("eventName");
        }

        textView.setText(event);

        db = new TaskDatabaseHandler(this);
        db.openDatabase();

        noTask = findViewById(R.id.noTaskText);
        createTask = findViewById(R.id.createTaskText);

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, TaskActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new TaskRecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab = findViewById(R.id.fab);

        taskList = db.getAllTasks(event);

        tasksAdapter.setTasks(taskList);

        hintVisible(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance(event).show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList = db.getAllTasks(event);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
        hintVisible(taskList);
    }

    public String getEvent(){
        return event;
    }

    public void back(View view){
        Context context = view.getContext();
        Intent intent = new Intent(context, EventActivity.class);

        context.startActivity(intent);
    }

    public void hintVisible(List<ToDoModel> taskList){
        if(taskList.isEmpty()){
            noTask.setVisibility(View.VISIBLE);
            createTask.setVisibility(View.VISIBLE);
        } else {
            noTask.setVisibility(View.GONE);
            createTask.setVisibility(View.GONE);
        }
    }
}