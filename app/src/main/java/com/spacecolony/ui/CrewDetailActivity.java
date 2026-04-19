package com.spacecolony.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.spacecolony.R;
import com.spacecolony.colony.CrewMember;
import com.spacecolony.manager.GameManager;

public class CrewDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew_detail);

        TextView tvDetail = findViewById(R.id.tvDetail);
        Button btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        int crewId = getIntent().getIntExtra("crewId", -1);

        CrewMember target = null;

        for (CrewMember c : GameManager.getInstance().getStorage().getAllCrew()) {
            if (c.getId() == crewId) {
                target = c;
                break;
            }
        }

        if (target == null) {
            tvDetail.setText("Crew not found");
            return;
        }

        String info =
                "===== CREW DETAIL =====\n\n" +
                        "Name: " + target.getName() + "\n" +
                        "Role: " + target.getSpecialization() + "\n\n" +

                        "--- BASIC ---\n" +
                        "Skill: " + target.getSkillLevel() + "\n" +
                        "Energy: " + target.getEnergy() + "/" + target.getMaxEnergy() + "\n" +
                        "Experience: " + target.getExperience() + "\n\n" +

                        "--- PERFORMANCE ---\n" +
                        "Missions: " + target.getMissionsParticipated() + "\n" +
                        "Wins: " + target.getMissionsWon() + "\n" +
                        "Training: " + target.getTrainingCount();

        tvDetail.setText(info);
    }
}