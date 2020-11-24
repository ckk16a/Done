package com.example.done;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.done.Model.EventModel;
import com.example.done.Model.ToDoModel;
import com.example.done.Utils.EventDatabaseHandler;
import com.example.done.Utils.TaskDatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewEvent extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText newEventText;
    private Button newEventSaveButton;

    private EventDatabaseHandler db;

    public static AddNewEvent newInstance(){
        return new AddNewEvent();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_event, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newEventText = Objects.requireNonNull(getView()).findViewById(R.id.newEventText);
        newEventSaveButton = getView().findViewById(R.id.newEventButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String event = bundle.getString("event");
            newEventText.setText(event);
            assert event != null;
            if(event.length()>0)
                newEventSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
        }

        db = new EventDatabaseHandler(getActivity());
        db.openDatabase();

        newEventText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newEventSaveButton.setEnabled(false);
                    newEventSaveButton.setTextColor(Color.GRAY);
                }
                else{
                    newEventSaveButton.setEnabled(true);
                    newEventSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        final boolean finalIsUpdate = isUpdate;
        newEventSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newEventText.getText().toString();

                String name = dupName(text, text, 1);
                EventModel event = new EventModel();
                event.setEvent(name);
                db.insertEvent(event);

                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }

    //Checks if the event name is a duplicate, if it is it adds a copy number at the end
    private String dupName(String origName, String newName, int dupNum){
        if(db.eventExists(newName)){
            newName = origName +  " (" + dupNum + ")";
            return dupName(origName, newName, dupNum + 1);
        } else {
            return newName;
        }
    }
}
