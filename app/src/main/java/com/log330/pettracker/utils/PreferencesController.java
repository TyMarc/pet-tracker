package com.log330.pettracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by marc-antoinehinse on 2015-10-19.
 */
public class PreferencesController {
    private static final String PREF_NAME = "pettracker";

    public static final String IS_ALREADY_LOGGED_IN = "IS_ALREADY_LOGGED_IN";

    public static void setPreference(final Context context, final String preference, final String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(preference, value);
        editor.commit();
    }

    public static void setPreference(final Context context, final String preference, final boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(preference, value);
        editor.commit();
    }

    public static String getPreference(final Context context, final String preference) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String restoredText = prefs.getString(preference, null);
        return restoredText;
    }

    public static int getIntPreference(final Context context, final String preference) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int restoredText = prefs.getInt(preference, -1);
        return restoredText;
    }

    public static boolean getBooleanPreference(final Context context, final String preference) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean restoredText = prefs.getBoolean(preference, false);
        return restoredText;
    }
}