package com.spacecolony.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.spacecolony.R;
import com.spacecolony.adapter.CrewAdapter;
import com.spacecolony.colony.CrewMember;
import com.spacecolony.manager.GameManager;

import java.util.List;

public class QuartersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CrewAdapter adapter;

    private CrewMember selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quarters);

        recyclerView = findViewById(R.id.recyclerView);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnToSimulator = findViewById(R.id.btnToSimulator);
        Button btnToMission = findViewById(R.id.btnToMission);
        Button btnRecallMedBay = findViewById(R.id.btnRecallMedBay);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnLoad = findViewById(R.id.btnLoad);
        Button btnDelete = findViewById(R.id.btnDelete);

        btnBack.setOnClickListener(v -> finish());

        loadList();

        btnToSimulator.setOnClickListener(v -> {

            if (adapter == null) {
                Toast.makeText(this, "List not ready!", Toast.LENGTH_SHORT).show();
                return;
            }

            List<CrewMember> selectedList = adapter.getSelectedCrew();

            if (selectedList.isEmpty()) {
                Toast.makeText(this, "Select crew!", Toast.LENGTH_SHORT).show();
                return;
            }

            selectedList.get(0).setLocation("Simulator");
            loadList();
        });

        btnToMission.setOnClickListener(v -> {

            if (adapter == null) {
                Toast.makeText(this, "List not ready!", Toast.LENGTH_SHORT).show();
                return;
            }

            List<CrewMember> selectedList = adapter.getSelectedCrew();

            if (selectedList.size() < 2) {
                Toast.makeText(this, "Select at least 2 crew!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedList.size() > 3) {
                Toast.makeText(this, "Maximum 3 crew allowed!", Toast.LENGTH_SHORT).show();
                return;
            }

            GameManager.getInstance().getMissionControl().getSelectedCrew().clear();

            for (CrewMember c : selectedList) {
                GameManager.getInstance().getMissionControl().addCrew(c);
            }

            startActivity(new Intent(this, MissionControlActivity.class));
        });

        btnRecallMedBay.setOnClickListener(v -> {

            List<CrewMember> medList = GameManager.getInstance()
                    .getStorage()
                    .getCrewByLocation("MedBay");

            if (medList.isEmpty()) {
                Toast.makeText(this, "No crew in MedBay", Toast.LENGTH_SHORT).show();
                return;
            }

            for (CrewMember c : medList) {
                c.setLocation("Quarters");
                c.setEnergy(c.getMaxEnergy());
                c.setExperience(0);
            }

            Toast.makeText(this, "All crew returned from MedBay!", Toast.LENGTH_SHORT).show();
            loadList();
        });

        btnSave.setOnClickListener(v -> {
            GameManager.getInstance().saveGame(this);
            Toast.makeText(this, "Game Saved!", Toast.LENGTH_SHORT).show();
        });

        btnLoad.setOnClickListener(v -> {

            if (!GameManager.getInstance().hasSave(this)) {
                Toast.makeText(this, "No save file!", Toast.LENGTH_SHORT).show();
                return;
            }

            GameManager.getInstance().loadGame(this);

            Toast.makeText(this, "Game Loaded!", Toast.LENGTH_SHORT).show();

            loadList();
        });

        btnDelete.setOnClickListener(v -> {
            if (selected == null) {
                Toast.makeText(this, "Select crew!", Toast.LENGTH_SHORT).show();
                return;
            }

            GameManager.getInstance().getStorage().removeCrew(selected);

            Toast.makeText(this, "Crew deleted!", Toast.LENGTH_SHORT).show();

            selected = null;
            loadList();
        });
    }

    private void loadList() {
        List<CrewMember> list = GameManager.getInstance()
                .getStorage()
                .getCrewByLocation("Quarters");

        selected = null;

        adapter = new CrewAdapter(list, crew -> selected = crew);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (CrewMember c : GameManager.getInstance().getStorage().getAllCrew()) {
            if ("Quarters".equals(c.getLocation())) {
                c.setEnergy(c.getMaxEnergy());
            }
        }
    }
}