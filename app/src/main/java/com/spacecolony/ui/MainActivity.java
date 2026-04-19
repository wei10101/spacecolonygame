package com.spacecolony.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.spacecolony.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRecruit = findViewById(R.id.btnRecruit);
        Button btnQuarters = findViewById(R.id.btnQuarters);
        Button btnSimulator = findViewById(R.id.btnSimulator);
        Button btnMissionControl = findViewById(R.id.btnMissionControl);
        Button btnStats = findViewById(R.id.btnStats);

        btnRecruit.setOnClickListener(v -> startActivity(new Intent(this, RecruitActivity.class)));
        btnQuarters.setOnClickListener(v -> startActivity(new Intent(this, QuartersActivity.class)));
        btnSimulator.setOnClickListener(v -> startActivity(new Intent(this, SimulatorActivity.class)));
        btnMissionControl.setOnClickListener(v -> startActivity(new Intent(this, MissionControlActivity.class)));
        btnStats.setOnClickListener(v -> startActivity(new Intent(this, StatisticsActivity.class)));
    }
}