package com.spacecolony.ui;

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

public class SimulatorActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CrewMember selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulator);

        recyclerView = findViewById(R.id.recyclerView);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnTrain = findViewById(R.id.btnTrain);
        Button btnToQuarters = findViewById(R.id.btnToQuarters);

        btnBack.setOnClickListener(v -> finish());

        loadList();

        btnTrain.setOnClickListener(v -> {
            if (selected == null) {
                Toast.makeText(this, "Select crew!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selected.getEnergy() < 5) {
                selected.setLocation("MedBay");
                GameManager.getInstance()
                        .getMissionControl()
                        .getMedBay()
                        .addInjuredCrew(selected);

                Toast.makeText(this, selected.getName() + " is exhausted and sent to MedBay!", Toast.LENGTH_SHORT).show();

                selected = null;
                loadList();
                return;
            }

            selected.train();
            GameManager.getInstance().getStorage().getStatistics().trainingSessionCompleted();
            loadList();
        });

        btnToQuarters.setOnClickListener(v -> {
            if (selected != null) {
                selected.setLocation("Quarters");
                loadList();
            }
        });
    }

    private void loadList() {
        List<CrewMember> list = GameManager.getInstance().getStorage().getCrewByLocation("Simulator");
        CrewAdapter adapter = new CrewAdapter(list, crew -> selected = crew);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}