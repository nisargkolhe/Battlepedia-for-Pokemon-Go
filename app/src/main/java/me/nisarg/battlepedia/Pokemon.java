package me.nisarg.battlepedia;

import android.content.Context;

/**
 * Created by nisarg on 20/7/16.
 */

// TODO all these should be private
public class Pokemon {
    public int ndex;
    public String cpMultiplier;
    public int hpBase;
    public int maxTotalCP;
    public int maxTotalHP;
    public String basicAttack;
    public String specialAttack;
    public int stamina;
    public int attack;
    public int defence;
    private String name;
    public String type1;
    public String type2;
    public static boolean isFav;

    public int getNdex() {
        return ndex;
    }

    public String getThumb(Context context) {
        return "android.resource://" + context.getPackageName() + "/drawable/m" + ndex;
    }

    public String getName() {
        return Helper.getPokemonNames()[ndex - 1];
    }
}
