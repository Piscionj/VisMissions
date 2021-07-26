package me.Viscar.Missions;

import org.bukkit.ChatColor;

public class Mission {

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    public enum MissionType {
        CATCH, KILL, CRAFT, MINE, SMELT, TAME, PLANT
    }

    private Difficulty difficulty;
    private int amountRequiredMin;
    private int amountRequiredMax;
    private MissionType missionType;
    private String missionObjective; // Used different depending on the mission type

    public Mission(MissionType missionType, Difficulty difficulty, int amountRequiredMin, int amountRequiredMax, String missionObjective) {
        this.missionType = missionType;
        this.difficulty = difficulty;
        this.amountRequiredMin = amountRequiredMin;
        this.amountRequiredMax = amountRequiredMax;
        this.missionObjective = missionObjective;
    }

    public String getDifficultyColoredString() {
        ChatColor difficultyColor = ChatColor.WHITE;
        switch (difficulty) {
            case EASY:
                difficultyColor = ChatColor.GREEN;
                break;
            case MEDIUM:
                difficultyColor = ChatColor.YELLOW;
                break;
            case HARD:
                difficultyColor = ChatColor.RED;
                break;
        }
        return difficultyColor + difficulty.name();
    }

    public MissionType getMissionType() {
        return this.missionType;
    }

    public int getRandomAmountRequired() {
        return amountRequiredMin + ((int) ((amountRequiredMax - amountRequiredMin) * Math.random()));
    }

    public String getMissionObjective() {
        return this.missionObjective;
    }
}
