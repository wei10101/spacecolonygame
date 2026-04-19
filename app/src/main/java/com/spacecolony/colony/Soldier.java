package com.spacecolony.colony;

public class Soldier extends CrewMember {
    public Soldier(int id, String name) {
        super(id, name, "Soldier");
    }

    @Override
    public int getSpecialAbilityDamage() {
        return getSkillLevel() * 4;
    }
}