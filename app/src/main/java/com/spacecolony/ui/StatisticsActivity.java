package com.spacecolony.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.spacecolony.R;
import com.spacecolony.colony.CrewMember;
import com.spacecolony.colony.Statistics;
import com.spacecolony.manager.GameManager;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_statistics);

        Statistics stats = GameManager.getInstance().getStorage().getStatistics();
        TextView tvStats = findViewById(R.id.tvStats);
        Button btnBack = findViewById(R.id.btnBack);

        String info = "=== Colony Statistics ===\n" +
                "Total Missions: " + stats.getTotalMissions() +
                "\nMissions Won: " + stats.getMissionsWon() +
                "\nTraining Sessions: " + stats.getTotalTrainingSessions() +
                "\nEnemies Defeated: " + stats.getEnemiesDefeated();

        List<CrewMember> crewList = GameManager.getInstance().getStorage().getAllCrew();
        info += "\nTotal Crew: " + crewList.size();

        info += "\n\n=== Crew Statistics ===\n";

        for (CrewMember c : crewList) {
            info += c.getName() + " (" + c.getSpecialization() + ")\n";
            info += "  Missions: " + c.getMissionsParticipated() + "\n";
            info += "  Wins: " + c.getMissionsWon() + "\n";
            info += "  Trainings: " + c.getTrainingCount() + "\n\n";
        }

        tvStats.setText(info);

        btnBack.setOnClickListener(v -> finish());
    }
}