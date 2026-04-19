package com.spacecolony.colony;

public class Engineer extends CrewMember {
    public Engineer(int id, String name) {
        super(id, name, "Engineer");
    }

    @Override
    public int getSpecialAbilityDamage() {
        return getSkillLevel() * 2;
    }
}