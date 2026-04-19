package com.spacecolony.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacecolony.R;
import com.spacecolony.adapter.CrewAdapter;
import com.spacecolony.colony.CrewMember;
import com.spacecolony.colony.Threat;
import com.spacecolony.manager.GameManager;

import java.util.List;

public class MissionControlActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvLog, tvThreatInfo, tvCrewStatus, tvTurn;
    private Button btnLaunch, btnAttack, btnDefend, btnSpecial, btnBack;
    private ProgressBar progressThreat, progressCrew;
    private CrewMember selectedCrew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_control);

        recyclerView = findViewById(R.id.recyclerView);
        tvThreatInfo = findViewById(R.id.tvThreatInfo);
        tvCrewStatus = findViewById(R.id.tvCrewStatus);
        tvTurn = findViewById(R.id.tvTurn);
        tvLog = findViewById(R.id.tvLog);

        progressThreat = findViewById(R.id.progressThreat);
        progressCrew = findViewById(R.id.progressCrew);

        btnLaunch = findViewById(R.id.btnLaunch);
        btnAttack = findViewById(R.id.btnAttack);
        btnDefend = findViewById(R.id.btnDefend);
        btnSpecial = findViewById(R.id.btnSpecial);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        loadCrewList();
        updateAll();

        btnLaunch.setOnClickListener(v -> {

            int crewSize = GameManager.getInstance()
                    .getMissionControl()
                    .getSelectedCrew()
                    .size();

            if (crewSize < 2) {
                Toast.makeText(this, "At least 2 crew members required!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (crewSize > 3) {
                Toast.makeText(this, "Maximum 3 crew members allowed!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (GameManager.getInstance().getMissionControl().getCurrentThreat() != null &&
                    GameManager.getInstance().getMissionControl().getCurrentThreat().isAlive()) {

                Toast.makeText(this, "Finish current threat first!", Toast.LENGTH_SHORT).show();
                return;
            }

            GameManager.getInstance().getMissionControl().generateThreat();
            updateAll();
        });

        btnAttack.setOnClickListener(v -> act("Attack"));
        btnDefend.setOnClickListener(v -> act("Defend"));
        btnSpecial.setOnClickListener(v -> act("Special"));
    }

    private void act(String action) {
        Threat threat = GameManager.getInstance().getMissionControl().getCurrentThreat();

        if (threat == null || !threat.isAlive()) {
            Toast.makeText(this, "No active threat!", Toast.LENGTH_SHORT).show();
            return;
        }

        GameManager.getInstance().getMissionControl().playerAction(action);
        updateAll();

        if (!threat.isAlive()) {
            Toast.makeText(this, "MISSION SUCCESS!", Toast.LENGTH_LONG).show();
        }
    }

    private void updateAll() {
        updateThreatDisplay();
        updateTurnDisplay();
        loadCrewList();
        updateLog();
    }

    private void updateThreatDisplay() {
        Threat t = GameManager.getInstance().getMissionControl().getCurrentThreat();

        if (t == null) {
            tvThreatInfo.setText("Threat: None");
            progressThreat.setProgress(0);
        } else {
            tvThreatInfo.setText("Threat: " + t.getName() +
                    "\nHP: " + t.getHealth() + "/" + t.getMaxHealth());

            progressThreat.setMax(t.getMaxHealth());
            progressThreat.setProgress(t.getHealth());
        }
    }

    private void updateTurnDisplay() {
        List<CrewMember> crewList = GameManager.getInstance().getMissionControl().getSelectedCrew();
        int idx = GameManager.getInstance().getMissionControl().getCurrentCrewIndex();

        if (crewList.isEmpty()) {
            tvTurn.setText("Turn: No crew");
            progressCrew.setProgress(0);
            return;
        }

        if (idx >= crewList.size()) {
            tvTurn.setText("Turn: Threat Attacking");
            progressCrew.setProgress(0);
        } else {
            CrewMember c = crewList.get(idx);

            tvTurn.setText("Turn: " + c.getSpecialization() + "(" + c.getName() + ")");

            progressCrew.setMax(c.getMaxEnergy());
            progressCrew.setProgress(c.getEnergy());

            tvCrewStatus.setText("Current: " + c.getName() +
                    " | Energy: " + c.getEnergy() + "/" + c.getMaxEnergy());
        }
    }

    private void updateLog() {
        tvLog.setText(GameManager.getInstance().getMissionControl().getMissionLog());
        ScrollView scrollView = (ScrollView) tvLog.getParent();
        scrollView.post(() -> scrollView.scrollTo(0, tvLog.getBottom()));
    }

    private void loadCrewList() {
        List<CrewMember> list = GameManager.getInstance()
                .getStorage()
                .getCrewByLocation("MissionControl");

        List<CrewMember> selected = GameManager.getInstance()
                .getMissionControl()
                .getSelectedCrew();

        selected.clear();

        for (int i = 0; i < list.size() && i < 3; i++) {
            selected.add(list.get(i));
        }

        CrewAdapter adapter = new CrewAdapter(list, crew -> selectedCrew = crew);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}