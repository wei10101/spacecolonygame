package com.spacecolony.colony;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class Storage implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<Integer, CrewMember> crewMap;
    private Statistics statistics;
    private int nextCrewId;

    public Storage() {
        crewMap = new HashMap<>();
        statistics = new Statistics();
        nextCrewId = 1;
    }

    public Statistics getStatistics() {
        if (statistics == null) {
            statistics = new Statistics();
        }
        return statistics;
    }

    public int getNextCrewId() {
        return nextCrewId++;
    }

    public List<CrewMember> getAllCrew() {
        if (crewMap == null) {
            crewMap = new HashMap<>();
        }
        return new ArrayList<>(crewMap.values());
    }

    public void addCrew(CrewMember crew) {
        if (crewMap == null) {
            crewMap = new HashMap<>();
        }
        crewMap.put(crew.getId(), crew);
    }

    public List<CrewMember> getCrewByLocation(String location) {
        List<CrewMember> result = new ArrayList<>();

        if (crewMap == null) return result;

        for (CrewMember crew : crewMap.values()) {
            if (crew != null &&
                    crew.getLocation() != null &&
                    crew.getLocation().equals(location)) {

                result.add(crew);
            }
        }
        return result;
    }

    public void saveToFile(Context context) {
        try {

            RuntimeTypeAdapterFactory<CrewMember> crewAdapter =
                    RuntimeTypeAdapterFactory.of(CrewMember.class, "type")
                            .registerSubtype(Pilot.class, "Pilot")
                            .registerSubtype(Engineer.class, "Engineer")
                            .registerSubtype(Medic.class, "Medic")
                            .registerSubtype(Scientist.class, "Scientist")
                            .registerSubtype(Soldier.class, "Soldier");

            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(crewAdapter)
                    .create();

            String json = gson.toJson(this);

            FileOutputStream fos = context.openFileOutput("save.json", Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeCrew(CrewMember crew) {
        if (crewMap != null && crew != null) {
            crewMap.remove(crew.getId());
        }
    }

    public static Storage loadFromFile(Context context) {
        try {
            FileInputStream fis = context.openFileInput("save.json");

            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            String json = new String(data);

            RuntimeTypeAdapterFactory<CrewMember> crewAdapter =
                    RuntimeTypeAdapterFactory.of(CrewMember.class, "type")
                            .registerSubtype(Pilot.class, "Pilot")
                            .registerSubtype(Engineer.class, "Engineer")
                            .registerSubtype(Medic.class, "Medic")
                            .registerSubtype(Scientist.class, "Scientist")
                            .registerSubtype(Soldier.class, "Soldier");

            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(crewAdapter)
                    .create();

            Type type = new TypeToken<Storage>() {}.getType();

            Storage loaded = gson.fromJson(json, type);

            if (loaded == null) {
                return new Storage();
            }

            if (loaded.crewMap == null) {
                loaded.crewMap = new HashMap<>();
            }

            if (loaded.statistics == null) {
                loaded.statistics = new Statistics();
            }

            return loaded;

        } catch (Exception e) {
            e.printStackTrace();
            return new Storage();
        }
    }
}