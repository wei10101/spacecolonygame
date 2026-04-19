package com.spacecolony.colony;
import java.util.ArrayList;

public class MedBay {
    private ArrayList<CrewMember> injuredCrew;

    public MedBay() {
        injuredCrew = new ArrayList<>();
    }

    public void addInjuredCrew(CrewMember crew) {
        if (!injuredCrew.contains(crew)) {
            injuredCrew.add(crew);
            crew.setLocation("MedBay");
        }
    }

    public void healCrew(CrewMember crew) {
        if (injuredCrew.contains(crew)) {
            crew.setEnergy(crew.getMaxEnergy());
            injuredCrew.remove(crew);
            crew.setLocation("Quarters");
        }
    }

    public ArrayList<CrewMember> getMedBayCrews() {
        return injuredCrew;
    }

    public void clear() {
        injuredCrew.clear();
    }
}