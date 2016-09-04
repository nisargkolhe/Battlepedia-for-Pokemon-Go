package me.nisarg.battlepedia;

/**
 * Created by nisarg on 30/8/16.
 */

public class Move {
    private String attack;
    private String type;
    private String dps;
    private String power;
    private String duration;
    private String dmgWindow;

    public Move(String attack, String type, String dps, String power, String duration, String dmgWindow) {
        this.attack = attack;
        this.type = type;
        this.dps = dps;
        this.power = power;
        this.duration = duration;
        this.dmgWindow = dmgWindow;
    }

    public String getDmgWindow() {
        return dmgWindow;
    }

    public String getDuration() {
        return duration;
    }

    public String getPower() {
        return power;
    }

    public String getDps() {
        return dps;
    }

    public String getType() {
        return type;
    }

    public String getAttack() {
        return attack;
    }
}
