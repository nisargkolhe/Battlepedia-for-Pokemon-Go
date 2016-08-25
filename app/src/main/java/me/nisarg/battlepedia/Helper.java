package me.nisarg.battlepedia;

import android.content.Context;

public class Helper {
    private static Context context = null;

    private static String[] pokemonNames = null;


    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Helper.context = context;
    }

    public static String[] getPokemonNames() {
        if (pokemonNames == null) {
            pokemonNames = getContext().getResources().getStringArray(R.array.Pokemon);
        }
        return pokemonNames;
    }

}
