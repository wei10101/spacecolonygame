package com.spacecolony.colony;

import java.util.List;

public abstract class CrewMember {
    private int id;
    private String name;
    private String specialization;
    private int skillLevel;
    private int experience;
    private int energy;
    private int maxEnergy;
    private int resilience;
    private String location;

    private int missionsParticipated;
    private int missionsWon;
    private int trainingCount;

    private boolean defending = false;
    private boolean specialUsed = false;
    private int defenseBuff = 0;
    private int defenseDebuff = 0;

    private boolean powerStrikeReady = false;


    public CrewMember(int id, String name, String specialization) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.experience = 0;
        this.location = "Quarters";

        missionsParticipated = 0;
        missionsWon = 0;
        trainingCount = 0;

        switch (specialization) {
            case "Pilot":
                this.skillLevel = 5;
                this.resilience = 4;
                this.maxEnergy = 20;
                break;
            case "Engineer":
                this.skillLevel = 6;
                this.resilience = 3;
                this.maxEnergy = 19;
                break;
            case "Medic":
                this.skillLevel = 7;
                this.resilience = 2;
                this.maxEnergy = 18;
                break;
            case "Scientist":
                this.skillLevel = 8;
                this.resilience = 1;
                this.maxEnergy = 17;
                break;
            case "Soldier":
                this.skillLevel = 9;
                this.resilience = 0;
                this.maxEnergy = 16;
                break;
            default:
                this.skillLevel = 1;
                this.resilience = 0;
                this.maxEnergy = 20;
        }
        this.energy = this.maxEnergy;
    }

    public abstract int getSpecialAbilityDamage();

    public void defend(List<CrewMember> team) {
        if (specialization.equals("Medic")) {
            CrewMember lowest = null;

            for (CrewMember c : team) {
                if (!c.isAlive()) continue;

                if (lowest == null || c.getEnergy() < lowest.getEnergy()) {
                    lowest = c;
                }
            }

            if (lowest != null) {
                lowest.setEnergy(Math.min(lowest.getMaxEnergy(), lowest.getEnergy() + 5));
            }

        } else {
            defending = true;
        }
    }

    public void takeDamage(int dmg) {

        int finalDamage = dmg;

        if (defending) {
            finalDamage /= 2;
        }

        this.energy = Math.max(0, this.energy - finalDamage);

        defending = false;
        defenseBuff = 0;
    }

    public void addExperience(int exp) {
        this.experience += exp;

        while (this.experience >= 5) {
            this.experience -= 5;
            this.skillLevel += 2;
        }
    }

    public boolean train() {

        if (this.energy < 5) {
            return false;
        }

        addExperience(1);
        trainingCount++;
        this.energy = Math.max(0, this.energy - 5);

        return true;
    }

    public void recordMission(boolean win) {
        missionsParticipated++;
        if (win) missionsWon++;
    }

    public boolean isAlive() {
        return this.energy > 0;
    }

    public boolean isSpecialUsed() {
        return specialUsed;
    }

    public void setSpecialUsed(boolean used) {
        this.specialUsed = used;
    }

    public void addDefenseBuff(int value) {
        this.defenseBuff += value;
    }

    public void addDefenseDebuff(int value) {
        this.defenseDebuff += value;
    }

    public int getDefenseBuff() { return defenseBuff; }
    public int getDefenseDebuff() { return defenseDebuff; }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public int getSkillLevel() { return skillLevel; }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }
    public int getMaxEnergy() { return maxEnergy; }
    public int getResilience() { return resilience; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getMissionsParticipated() { return missionsParticipated; }
    public int getMissionsWon() { return missionsWon; }
    public int getTrainingCount() { return trainingCount; }

    public boolean isDefending() {
        return defending;
    }
    public boolean isPowerStrikeReady() {
        return powerStrikeReady;
    }

    public void setPowerStrikeReady(boolean ready) {
        this.powerStrikeReady = ready;
    }

    public CrewMember getWeakestCrew(List<CrewMember> crewList) {

        CrewMember weakest = crewList.get(0);

        for (CrewMember c : crewList) {
            if (c.getEnergy() < weakest.getEnergy()) {
                weakest = c;
            }
        }

        return weakest;
    }
}