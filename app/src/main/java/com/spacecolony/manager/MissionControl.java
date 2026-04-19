package com.spacecolony.manager;

import com.spacecolony.colony.CrewMember;
import com.spacecolony.colony.MedBay;
import com.spacecolony.colony.Storage;
import com.spacecolony.colony.Threat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MissionControl {
    private Storage storage;
    private List<CrewMember> selectedCrew;
    private Threat currentThreat;
    private StringBuilder missionLog;
    private MedBay medBay;
    private int currentCrewIndex;
    private int round;
    private Set<CrewMember> reportedDead;

    private Random random = new Random();

    private Threat reduceThreatDefense(Threat t, int amount) {
        try {
            java.lang.reflect.Field f = t.getClass().getDeclaredField("resilience");
            f.setAccessible(true);
            int current = (int) f.get(t);
            f.set(t, Math.max(0, current - amount));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public MissionControl(Storage storage) {
        this.storage = storage;
        selectedCrew = new ArrayList<>();
        missionLog = new StringBuilder();
        medBay = new MedBay();
        currentCrewIndex = 0;
        round = 1;
        reportedDead = new HashSet<>();
    }

    public void addCrew(CrewMember crew) {
        if (selectedCrew.size() < 3 && !selectedCrew.contains(crew)) {
            selectedCrew.add(crew);
            crew.setLocation("MissionControl");
        }
    }

    public void generateThreat() {

        if (selectedCrew.size() < 2) {
            missionLog.append("Need at least 2 crew members to start mission!\n");
            return;
        }

        if (selectedCrew.size() > 3) {
            missionLog.append("Maximum 3 crew members allowed!\n");
            return;
        }

        int difficulty = Math.max(1, selectedCrew.size() * storage.getStatistics().getTotalMissions() / 5);
        currentThreat = new Threat(difficulty);

        round = 1;
        missionLog.setLength(0);
        reportedDead.clear();
        currentCrewIndex = 0;

        missionLog.append("=== MISSION: Encounter - ")
                .append(currentThreat.getName())
                .append(" ===\n");
        missionLog.append("Threat: ").append(currentThreat.getName())
                .append(" (skill: ").append(currentThreat.getSkill())
                .append(", resilience: ").append(currentThreat.getResilience())
                .append(", energy: ").append(currentThreat.getHealth()).append("/").append(currentThreat.getMaxHealth()).append(")\n");
        missionLog.append("Effect: ").append(currentThreat.getDescription()).append("\n\n");

        for (int i = 0; i < selectedCrew.size(); i++) {
            CrewMember cm = selectedCrew.get(i);
            missionLog.append("Crew Member ")
                    .append((char) ('A' + i))
                    .append(": ")
                    .append(cm.getSpecialization()).append("(").append(cm.getName()).append(")")
                    .append(" skill: ").append(cm.getSkillLevel())
                    .append("; res: ").append(cm.getResilience())
                    .append("; exp: ").append(cm.getExperience())
                    .append("; energy: ").append(cm.getEnergy()).append("/").append(cm.getMaxEnergy())
                    .append("\n");
        }

        missionLog.append("\n=== Role Advantages ===\n");

        for (CrewMember cm : selectedCrew) {

            int bonus = getRoleBonus(cm);

            if (bonus > 0) {
                missionLog.append(cm.getSpecialization())
                        .append("(").append(cm.getName()).append(")")
                        .append(" gains +")
                        .append(bonus)
                        .append(" skill against ")
                        .append(currentThreat.getName())
                        .append("\n");
            } else {
                missionLog.append(cm.getSpecialization())
                        .append("(").append(cm.getName()).append(")")
                        .append(" has no advantage against ")
                        .append(currentThreat.getName())
                        .append("\n");
            }
        }

        missionLog.append("\n");

        applyThreatEffect();
    }

    public void playerAction(String action) {

        if (currentThreat == null || !currentThreat.isAlive()) return;
        if (selectedCrew.isEmpty()) return;

        if (currentCrewIndex >= selectedCrew.size()) {
            currentCrewIndex = 0;
            round++;
        }

        CrewMember cm = selectedCrew.get(currentCrewIndex);

        if (currentCrewIndex == 0) {
            missionLog.append("--- Round ").append(round).append(" ---\n");
        }

        int variation = random.nextInt(5) - 2;
        int bonus = getRoleBonus(cm);

        if (action.equals("Attack")) {

            int base = cm.getSkillLevel() + bonus;
            int raw = base + variation;

            int dmg;

            if (cm.isPowerStrikeReady()) {

                dmg = Math.max(0, raw + 3);

                missionLog.append("  [Power Strike Activated]\n");
                missionLog.append("  Damage: ")
                        .append(raw)
                        .append(" + 3 = ")
                        .append(dmg)
                        .append(" (ignore defense)\n");

                cm.setPowerStrikeReady(false);

            } else {

                dmg = Math.max(0, raw - currentThreat.getResilience());

                missionLog.append("  Damage dealt: ")
                        .append(raw).append(" - ")
                        .append(currentThreat.getResilience()).append(" = ")
                        .append(dmg).append("\n");
            }

            currentThreat.takeDamage(dmg);

            missionLog.append(cm.getSpecialization()).append("(").append(cm.getName()).append(") acts against ").append(currentThreat.getName()).append("\n");

            if (bonus > 0) {
                missionLog.append("  [Role Bonus +").append(bonus).append("]\n");
            }

            missionLog.append("  ").append(currentThreat.getName())
                    .append(" energy: ").append(currentThreat.getHealth())
                    .append("/").append(currentThreat.getMaxHealth()).append("\n");
        }

        else if (action.equals("Defend")) {

            cm.defend(selectedCrew);

            if (cm.getSpecialization().equals("Medic")) {

                CrewMember target = cm.getWeakestCrew(selectedCrew);

                int before = target.getEnergy();
                int after = Math.min(target.getMaxEnergy(), before + 5);

                target.setEnergy(after);

                missionLog.append("Medic(").append(cm.getName())
                        .append(") heals ")
                        .append(target.getSpecialization()).append("(").append(target.getName()).append(")\n");

                missionLog.append("  Energy: ")
                        .append(before).append(" -> ").append(after).append("\n");

            } else {
                missionLog.append(cm.getSpecialization())
                        .append("(").append(cm.getName()).append(") is defending (damage halved)\n");
            }
        }

        else if (action.equals("Special")) {

            if (cm.isSpecialUsed()) {
                missionLog.append("Special can only be used once!\n");
            } else {

                cm.setSpecialUsed(true);

                String role = cm.getSpecialization();

                if (role.equals("Medic")) {
                    missionLog.append("Medic(").append(cm.getName()).append(") uses special: heal all +5\n");

                    for (CrewMember c : selectedCrew) {
                        int before = c.getEnergy();
                        int after = Math.min(c.getMaxEnergy(), before + 5);
                        c.setEnergy(after);

                        missionLog.append("  ")
                                .append(c.getSpecialization()).append("(").append(c.getName()).append("): ")
                                .append(before).append(" -> ").append(after).append("\n");
                    }
                }

                else if (role.equals("Engineer")) {
                    missionLog.append("Engineer(").append(cm.getName()).append(") uses special: defense +1 (1 turn)\n");

                    int before = cm.getResilience() + cm.getDefenseBuff();
                    cm.addDefenseBuff(1);
                    int after = cm.getResilience() + cm.getDefenseBuff();

                    missionLog.append("  Defense: ")
                            .append(before)
                            .append(" -> ")
                            .append(after)
                            .append("\n");
                }

                else if (role.equals("Soldier")) {
                    missionLog.append("Soldier(").append(cm.getName()).append(") uses special: Power Strike\n");
                    missionLog.append("  Next attack: +3 damage and ignores enemy defense\n");

                    cm.setPowerStrikeReady(true);
                }

                else if (role.equals("Scientist")) {
                    int before = currentThreat.getResilience();

                    missionLog.append("Scientist(").append(cm.getName()).append(") uses special: threat defense -2\n");

                    currentThreat = reduceThreatDefense(currentThreat, 2);

                    int after = currentThreat.getResilience();

                    missionLog.append("  Threat defense: ")
                            .append(before)
                            .append(" -> ")
                            .append(after)
                            .append("\n");
                }

                else if (role.equals("Pilot")) {
                    missionLog.append("Pilot(").append(cm.getName()).append(") uses special: evade next attack\n");
                    currentThreat.setSkipNextAttack(true);
                }
            }
        }

        if (currentThreat.isAlive()) {
            threatCounterAttack();
        }

        missionLog.append("\n");

        if (!currentThreat.isAlive()) {
            missionLog.append("\n=== MISSION COMPLETE ===\n");

            boolean isBonus = currentThreat.getType().equals("BONUS_EXP");

            for (CrewMember c : selectedCrew) {
                if (c.isAlive()) {

                    int expGain = 2;

                    if (isBonus) {
                        expGain += 1;
                    }

                    c.addExperience(expGain);

                    missionLog.append(c.getSpecialization())
                            .append("(").append(c.getName())
                            .append(") gained ")
                            .append(expGain)
                            .append(" EXP\n");
                }
            }

            finishMission(true);
            return;
        }

        currentCrewIndex++;
    }

    private int getRoleBonus(CrewMember cm) {

        String role = cm.getSpecialization();
        String type = currentThreat.getType();

        if (role.equals("Engineer") && type.equals("DEFENSE_DOWN")) return 2;
        if (role.equals("Scientist") && type.equals("BONUS_EXP")) return 2;
        if (role.equals("Medic") && type.equals("HALF_ENERGY")) return 2;

        return 0;
    }

    private void applyThreatEffect() {

        String type = currentThreat.getType();

        if (type.equals("HALF_ENERGY")) {

            missionLog.append("Threat effect applied: All crew lose half energy\n");

            for (CrewMember c : selectedCrew) {
                int lost = c.getEnergy() / 2;
                c.setEnergy(c.getEnergy() - lost);
            }
        }

        else if (type.equals("DEFENSE_DOWN")) {

            missionLog.append("Threat effect applied: All crew defense -1\n");

            for (CrewMember c : selectedCrew) {
                c.addDefenseDebuff(-1);
            }
        }

        else if (type.equals("BONUS_EXP")) {

            missionLog.append("Threat effect applied: Bonus EXP will be granted after mission\n");
        }

        missionLog.append("\n");
    }

    private void threatCounterAttack() {

        if (currentThreat.shouldSkipAttack()) {
            missionLog.append(currentThreat.getName()).append(" cannot attack this turn!\n");
            currentThreat.setSkipNextAttack(false);
            return;
        }

        if (selectedCrew.isEmpty()) return;

        CrewMember target = selectedCrew.get(random.nextInt(selectedCrew.size()));

        int variation = random.nextInt(5) - 2;
        int base = currentThreat.getSkill();

        int totalDefense = target.getResilience()
                + target.getDefenseBuff()
                + target.getDefenseDebuff();

        int rawDamage = base + variation;
        int dmg = Math.max(0, rawDamage - totalDefense);

        target.takeDamage(dmg);

        missionLog.append(currentThreat.getName()).append(" retaliates against ")
                .append(target.getSpecialization()).append("(").append(target.getName()).append(")\n");

        if (target.isDefending()) {

            int reduced = dmg / 2;

            missionLog.append("  Damage dealt: (")
                    .append(rawDamage).append(" - ")
                    .append(totalDefense).append(") / 2 = ")
                    .append(reduced).append("\n");

        } else {

            missionLog.append("  Damage dealt: ")
                    .append(rawDamage).append(" - ")
                    .append(totalDefense).append(" = ")
                    .append(dmg).append("\n");
        }

        missionLog.append("  ").append(target.getSpecialization())
                .append("(").append(target.getName()).append(") energy: ")
                .append(target.getEnergy()).append("/")
                .append(target.getMaxEnergy()).append("\n");

        if (target.getEnergy() == 0) {

            missionLog.append(target.getSpecialization())
                    .append("(").append(target.getName())
                    .append(") was defeated and sent to MedBay!\n");

            target.setExperience(0);
            target.setLocation("MedBay");

            medBay.addInjuredCrew(target);

            int removedIndex = selectedCrew.indexOf(target);
            selectedCrew.remove(target);

            if (removedIndex <= currentCrewIndex && currentCrewIndex > 0) {
                currentCrewIndex--;
            }
        }

        if (selectedCrew.isEmpty()) {

            missionLog.append("\n=== MISSION FAILED ===\n");
            missionLog.append("All crew members have been defeated!\n");
            missionLog.append("Threat ").append(currentThreat.getName()).append(" remains undefeated.\n\n");

            for (CrewMember cm : storage.getAllCrew()) {
                if ("MissionControl".equals(cm.getLocation()) || "MedBay".equals(cm.getLocation())) {
                    cm.recordMission(false);
                }
            }

            finishMission(false);
        }
    }

    public void finishMission(boolean win) {
        storage.getStatistics().missionCompleted(win);

        for (CrewMember cm : selectedCrew) {
            cm.recordMission(win);
            cm.setLocation("Quarters");
            cm.setEnergy(cm.getMaxEnergy());
        }

        selectedCrew.clear();
        currentCrewIndex = 0;
        round = 1;
    }

    public List<CrewMember> getSelectedCrew() { return selectedCrew; }
    public Threat getCurrentThreat() { return currentThreat; }
    public String getMissionLog() { return missionLog.toString(); }
    public int getCurrentCrewIndex() { return currentCrewIndex; }

    public MedBay getMedBay() {
        return medBay;
    }
}