package com.spacecolony.colony;

import java.util.Random;

public class Threat {
    private String name;
    private int health;
    private int maxHealth;
    private int skill;
    private int resilience;
    private int difficultyLevel;

    private String description;
    private String type;

    private boolean skipNextAttack = false;

    public Threat(int difficulty) {
        this.difficultyLevel = difficulty;

        int rand = new Random().nextInt(5);

        switch (rand) {
            case 0:
                this.name = "Asteroid Storm";
                this.type = "NORMAL";
                this.description = "No special effect.";
                this.maxHealth = 25 + difficulty * 5;
                this.skill = 5 + difficulty;
                this.resilience = 2;
                break;
            case 1:
                this.name = "System Failure";
                this.type = "DEFENSE_DOWN";
                this.description = "All crew resilience is reduced by one.";
                this.maxHealth = 20 + difficulty * 4;
                this.skill = 4 + difficulty;
                this.resilience = 3;
                break;
            case 2:
                this.name = "Solar Flare";
                this.type = "NORMAL";
                this.description = "No special effect.";
                this.maxHealth = 28 + difficulty * 5;
                this.skill = 5 + difficulty;
                this.resilience = 2;
                break;
            case 3:
                this.name = "New Planet";
                this.type = "BONUS_EXP";
                this.description = "Gain +1 extra EXP after mission.";
                this.maxHealth = 22 + difficulty * 4;
                this.skill = 4 + difficulty;
                this.resilience = 1;
                break;
            case 4:
                this.name = "Virus Outbreak";
                this.type = "HALF_ENERGY";
                this.description = "All crew lose half energy";
                this.maxHealth = 26 + difficulty * 5;
                this.skill = 5 + difficulty;
                this.resilience = 2;
                break;
        }

        this.health = this.maxHealth;
    }

    public void takeDamage(int dmg) {
        this.health = Math.max(0, this.health - dmg);
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public String getName() { return name; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getSkill() { return skill; }
    public int getResilience() { return resilience; }
    public int getDifficultyLevel() { return difficultyLevel; }
    public int getDamage() { return skill; }

    public String getDescription() { return description; }
    public String getType() { return type; }

    public void setSkipNextAttack(boolean skip) {
        this.skipNextAttack = skip;
    }

    public boolean shouldSkipAttack() {
        return skipNextAttack;
    }

}