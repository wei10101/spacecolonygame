package com.spacecolony.colony;

import java.util.HashMap;
import java.util.Map;

public class Statistics {

    private int totalMissions;
    private int missionsWon;
    private int totalTrainingSessions;
    private int enemiesDefeated;

    public Map<String, Integer> crewCountBySpecialization;

    public Statistics() {
        crewCountBySpecialization = new HashMap<>();
        totalMissions = 0;
        missionsWon = 0;
        totalTrainingSessions = 0;
        enemiesDefeated = 0;
    }

    public void missionCompleted(boolean isWin) {
        totalMissions++;
        if (isWin) {
            missionsWon++;
            enemiesDefeated++;
        }
    }

    public void trainingSessionCompleted() {
        totalTrainingSessions++;
    }

    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    public double getWinRate() {
        if (totalMissions == 0) return 0;
        return (double) missionsWon / totalMissions * 100;
    }

    public int getTotalMissions() {
        return totalMissions;
    }

    public int getMissionsWon() {
        return missionsWon;
    }

    public int getTotalTrainingSessions() {
        return totalTrainingSessions;
    }
}