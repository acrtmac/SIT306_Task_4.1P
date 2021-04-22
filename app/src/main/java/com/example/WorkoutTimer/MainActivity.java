package com.example.WorkoutTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Chronometer;

public class MainActivity extends AppCompatActivity {

    Chronometer chronometer;
    TextView textViewLastTime;
    EditText editTextWorkType;

    String WorkoutName = "workout";
    String WorkoutType;

    String Timerecorded = "Time";
    String Reminder = "Reminder";
    String duration = "duration";
    String spentTime;
    long recordTime;

    String Base = "Basetime";
    long currentBase;

    String Currentstate = "Currentstate";
    String state;

    String last_info;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WorkoutType = "";
        spentTime = "";
        state = "off";
        recordTime = 0;
        this.initView(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        currentBase = chronometer.getBase();
        outState.putLong(Base, currentBase);
        outState.putLong(Timerecorded, recordTime);
        outState.putString(duration, spentTime);
        outState.putString(WorkoutName, WorkoutType);
        outState.putString(Reminder, last_info);
        outState.putString(Currentstate, state);
    }

    public void initView(Bundle savedInstanceState){
        textViewLastTime = findViewById(R.id.textViewLastTime);
        editTextWorkType = findViewById(R.id.editTextWorkType);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");

        if(savedInstanceState!=null){
            WorkoutType = savedInstanceState.getString(WorkoutName);
            spentTime = savedInstanceState.getString(duration);
            state = savedInstanceState.getString(Currentstate);
            last_info = savedInstanceState.getString(Reminder);
            recordTime = savedInstanceState.getLong(Timerecorded);
            currentBase = savedInstanceState.getLong(Base);

            switch (state){
                case "off":
                    break;

                case "on":
                    chronometer.setBase(currentBase);
                    chronometer.start();
                    break;

                case "stop":
                    chronometer.setBase(SystemClock.elapsedRealtime() - recordTime );
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + state);
            }
        }

        last_info = getString(R.string.last_info, spentTime, WorkoutType);
        textViewLastTime.setText(last_info);
    }

    public void onClickButton(View view) {
        switch (view.getId())
        {
            case R.id.imageButtonPlay:
                chronometer.setBase(SystemClock.elapsedRealtime()-recordTime);
                chronometer.start();
                state = "on";
                break;

            case R.id.imageButtonPause:
                chronometer.stop();
                if(state == "on")
                {
                    recordTime = SystemClock.elapsedRealtime()-chronometer.getBase();
                }
                state = "stop";
                break;

            case R.id.imageButtonStop:
                WorkoutType = editTextWorkType.getText().toString();
                editTextWorkType.setText("");
                spentTime = chronometer.getText().toString();
                last_info = getString(R.string.last_info, spentTime, WorkoutType);
                textViewLastTime.setText(last_info);
                recordTime = 0;
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                state = "off";
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

    }
}