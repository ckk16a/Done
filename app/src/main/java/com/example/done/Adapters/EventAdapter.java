package com.example.done.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.done.AddNewEvent;
import com.example.done.EventActivity;

import com.example.done.TaskActivity;
import com.example.done.Model.EventModel;
import com.example.done.R;
import com.example.done.Utils.EventDatabaseHandler;
import com.example.done.Utils.TaskDatabaseHandler;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<EventModel> eventList;
    private EventDatabaseHandler db;
    private TaskDatabaseHandler db2;
    private EventActivity activity;

    public EventAdapter(EventDatabaseHandler db, TaskDatabaseHandler db2, EventActivity activity) {
        this.db = db;
        this.db2 = db2;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();
        db2.openDatabase();

        final EventModel item = eventList.get(position);
        holder.event.setText(item.getEvent());
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setEvents(List<EventModel> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        EventModel item = eventList.get(position);
        db2.onEventDelete(item.getEvent());
        db.deleteEvent(item.getId());
        eventList.remove(position);
        notifyItemRemoved(position);
        activity.hintVisible(eventList);
    }

    public void editItem(int position) {
        EventModel item = eventList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("event", item.getEvent());
        AddNewEvent fragment = new AddNewEvent();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewEvent.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView event;

        ViewHolder(View view) {
            super(view);
            event = view.findViewById(R.id.eventTextView);
        }
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView event = view.findViewById(R.id.eventTextView);
            Context context = view.getContext();
            Intent intent = new Intent(context, TaskActivity.class);
            intent.putExtra("eventName", event.getText());
            intent.putExtra("eventID", event.getId());

            context.startActivity(intent);
        }
    };
}
