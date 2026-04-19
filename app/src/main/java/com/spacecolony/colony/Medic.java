package com.spacecolony.colony;

public class Medic extends CrewMember {
    public Medic(int id, String name) {
        super(id, name, "Medic");
    }

    @Override
    public int getSpecialAbilityDamage() {
        return getSkillLevel() * 2;
    }
}