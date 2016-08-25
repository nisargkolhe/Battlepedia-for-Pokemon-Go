package me.nisarg.battlepedia;

import android.content.Context;

/**
 * Created by nisarg on 20/7/16.
 */
public class Pokemon {
    private int ndex;
    private String cpMultiplier;
    private int hpBase;
    private int maxTotalCP;
    private int maxTotalHP;
    private String basicAttack;
    private String specialAttack;
    private int stamina;
    private int attack;
    private int defence;
    private String name;
    private String type1;
    private String type2;
    private static boolean isFav;


    public int getNdex() {
        return ndex;
    }

    public void setNdex(int ndex) {
        this.ndex = ndex;
    }

    public String getCpMultiplier() {
        return cpMultiplier;
    }

    public void setCpMultiplier(String cpMultiplier) {
        this.cpMultiplier = cpMultiplier;
    }

    public int getHpBase() {
        return hpBase;
    }

    public void setHpBase(int hpBase) {
        this.hpBase = hpBase;
    }

    public int getMaxTotalCP() {
        return maxTotalCP;
    }

    public void setMaxTotalCP(int maxTotalCP) {
        this.maxTotalCP = maxTotalCP;
    }

    public int getMaxTotalHP() {
        return maxTotalHP;
    }

    public void setMaxTotalHP(int maxTotalHP) {
        this.maxTotalHP = maxTotalHP;
    }

    public String getBasicAttack() {
        return basicAttack;
    }

    public void setBasicAttack(String basicAttack) {
        this.basicAttack = basicAttack;
    }

    public String getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(String specialAttack) {
        this.specialAttack = specialAttack;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public static boolean isFav() {
        return isFav;
    }

    public static void setIsFav(boolean isFav) {
        Pokemon.isFav = isFav;
    }


    public String getThumb(Context context) {
        return "android.resource://" + context.getPackageName() + "/drawable/m" + ndex;
    }

    public String getName() {
        return Helper.getPokemonNames()[ndex - 1];
    }
}
