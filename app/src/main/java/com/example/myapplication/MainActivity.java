package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int timeSelected = 0;
    private CountDownTimer timeCountDown = null;
    private int timeProgress = 0;
    private long pauseOffSet = 0;
    private boolean isStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton addBtn = findViewById(R.id.ButtonAddTime);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeFunction();
            }
        });
        Button startBtn = findViewById(R.id.ButtonStartPause);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimerSetup();
            }
        });
        TextView addTimeTv = findViewById(R.id.Add10);
        addTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExtraTime();
            }
        });
        ImageButton resetImageBtn = findViewById(R.id.ButtonResetTime);
        resetImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTime();
            }
        });
    }
    private void addExtraTime() {
        ProgressBar progressBar = findViewById(R.id.ProgressBarTimer);
        if (timeSelected != 0) {
            timeSelected += 10;
            progressBar.setMax(timeSelected);
            timePause();
            startTimer(pauseOffSet);
            Toast.makeText(this, "10 sec added", Toast.LENGTH_SHORT).show();
        }
    }
    private void resetTime() {
        if (timeCountDown != null) {
            timeCountDown.cancel();
            timeProgress = 0;
            timeSelected = 0;
            pauseOffSet = 0;
            timeCountDown = null;
            Button startBtn = findViewById(R.id.ButtonStartPause);
            startBtn.setText("Start");
            isStart = true;
            ProgressBar progressBar = findViewById(R.id.ProgressBarTimer);
            progressBar.setProgress(0);
            TextView timeLeftTv = findViewById(R.id.TextViewShowTime);
            timeLeftTv.setText("0");
        }
    }
    private void timePause() {
        if (timeCountDown != null) {
            timeCountDown.cancel();
        }
    }
    private void startTimerSetup() {
        Button startBtn = findViewById(R.id.ButtonStartPause);
        if (timeSelected > timeProgress) {
            if (isStart) {
                startBtn.setText("Pause");
                startTimer(pauseOffSet);
                isStart = false;
            } else {
                isStart = true;
                startBtn.setText("Resume");
                timePause();
            }
        } else {
            Toast.makeText(this, "Enter Time", Toast.LENGTH_SHORT).show();
        }
    }
    private void startTimer(long pauseOffSetL) {
        ProgressBar progressBar = findViewById(R.id.ProgressBarTimer);
        progressBar.setProgress(timeProgress);
        timeCountDown = new CountDownTimer((timeSelected * 1000) - (pauseOffSetL * 1000), 1000) {
            @Override
            public void onTick(long p0) {
                timeProgress++;
                pauseOffSet = timeSelected - (p0 / 1000);
                progressBar.setProgress(timeSelected - timeProgress);
                TextView timeLeftTv = findViewById(R.id.TextViewShowTime);
                timeLeftTv.setText(Integer.toString(timeSelected - timeProgress));
            }

            @Override
            public void onFinish() {
                resetTime();
                Toast.makeText(MainActivity.this, "Time's Up!", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }
    private void setTimeFunction() {
        final Dialog timeDialog = new Dialog(this);
        timeDialog.setContentView(R.layout.settime_dialog);
        final EditText timeSet = timeDialog.findViewById(R.id.EditText);
        final TextView timeLeftTv = findViewById(R.id.TextViewShowTime);
        final Button btnStart = findViewById(R.id.ButtonStartPause);
        final ProgressBar progressBar = findViewById(R.id.ProgressBarTimer);

        timeDialog.findViewById(R.id.ButtonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timeSet.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Enter Time Duration", Toast.LENGTH_SHORT).show();
                } else {
                    resetTime();

                    timeLeftTv.setText(timeSet.getText());
                    btnStart.setText("Start");
                    timeSelected = Integer.parseInt(timeSet.getText().toString());
                    progressBar.setMax(timeSelected);
                }
                timeDialog.dismiss();
            }
        });

        timeDialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeCountDown != null) {
            timeCountDown.cancel();
            timeProgress = 0;
        }
    }
}