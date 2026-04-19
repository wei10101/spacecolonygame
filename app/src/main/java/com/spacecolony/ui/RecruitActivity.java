package com.spacecolony.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.spacecolony.R;
import com.spacecolony.colony.CrewMember;
import com.spacecolony.colony.Engineer;
import com.spacecolony.colony.Medic;
import com.spacecolony.colony.Pilot;
import com.spacecolony.colony.Scientist;
import com.spacecolony.colony.Soldier;
import com.spacecolony.manager.GameManager;

public class RecruitActivity extends AppCompatActivity {

    private EditText etName;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruit);

        etName = findViewById(R.id.etName);
        radioGroup = findViewById(R.id.radioGroup);
        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnCancel = findViewById(R.id.btnCancel);

        btnCreate.setOnClickListener(v -> createCrew());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void createCrew() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Enter name!", Toast.LENGTH_SHORT).show();
            return;
        }

        int checkId = radioGroup.getCheckedRadioButtonId();
        if (checkId == -1) {
            Toast.makeText(this, "Choose specialization!", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = GameManager.getInstance().getStorage().getNextCrewId();
        CrewMember crew = null;

        if (checkId == R.id.radioPilot) crew = new Pilot(id, name);
        else if (checkId == R.id.radioEngineer) crew = new Engineer(id, name);
        else if (checkId == R.id.radioMedic) crew = new Medic(id, name);
        else if (checkId == R.id.radioScientist) crew = new Scientist(id, name);
        else if (checkId == R.id.radioSoldier) crew = new Soldier(id, name);

        if (crew != null) {
            GameManager.getInstance().getStorage().addCrew(crew);
            Toast.makeText(this, "Created: " + name + " (ID: " + id + ")", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}