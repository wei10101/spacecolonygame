package com.spacecolony.colony;

public class Scientist extends CrewMember {
    public Scientist(int id, String name) {
        super(id, name, "Scientist");
    }

    @Override
    public int getSpecialAbilityDamage() {
        return getSkillLevel() * 3;
    }
}