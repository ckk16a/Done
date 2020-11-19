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

import com.example.done.Utils.TaskDatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.done.Adapters.EventAdapter;
import com.example.done.Model.EventModel;
import com.example.done.Utils.EventDatabaseHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class EventActivity extends AppCompatActivity implements DialogCloseListener {

    private EventDatabaseHandler db;
    private TaskDatabaseHandler db2;

    private RecyclerView eventsRecyclerView;
    private EventAdapter eventsAdapter;
    private FloatingActionButton fab;

    private List<EventModel> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new EventDatabaseHandler(this);
        db.openDatabase();

        db2 = new TaskDatabaseHandler(this);
        db2.openDatabase();

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsAdapter = new EventAdapter(db, db2, EventActivity.this);
        eventsRecyclerView.setAdapter(eventsAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new EventRecyclerItemTouchHelper(eventsAdapter));
        itemTouchHelper.attachToRecyclerView(eventsRecyclerView);

        fab = findViewById(R.id.fab);

        eventList = db.getAllEvents();

        eventsAdapter.setEvents(eventList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewEvent.newInstance().show(getSupportFragmentManager(), AddNewEvent.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        eventList = db.getAllEvents();
        eventsAdapter.setEvents(eventList);
        eventsAdapter.notifyDataSetChanged();
    }
}