package com.spacecolony.colony;

public class Pilot extends CrewMember {
    public Pilot(int id, String name) {
        super(id, name, "Pilot");
    }

    @Override
    public int getSpecialAbilityDamage() {
        return getSkillLevel() * 3;
    }
}