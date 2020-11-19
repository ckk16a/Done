package com.example.done;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.done.Utils.TaskDatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailActivity extends AppCompatActivity {

    private String task, description, event;
    private int id;

    private FloatingActionButton fab;
    private TaskDatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final EditText taskView = findViewById(R.id.taskView);
        final EditText descriptionView = findViewById(R.id.descriptionView);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                task = null;
                description = null;
                event = null;
                id = Integer.parseInt(null);
            } else {
                task = extras.getString("task");
                description = extras.getString("description");
                event = extras.getString("event");
                id = extras.getInt("id");

            }
        } else {
            task = (String) savedInstanceState.getSerializable("task");
            description = (String) savedInstanceState.getSerializable("description");
            event = (String) savedInstanceState.getSerializable("event");
            id = (int) savedInstanceState.getSerializable("id");

        }

        taskView.setText(task);
        descriptionView.setText(description);

        db = new TaskDatabaseHandler(this);
        db.openDatabase();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateTask(id, taskView.getText().toString());
                db.updateDescription(id, descriptionView.getText().toString());

                Context context = v.getContext();
                Intent intent = new Intent(context, TaskActivity.class);
                intent.putExtra("eventName", event);

                context.startActivity(intent);
            }
        });
    }

    public void back(View view) {
        finish();
    }
}