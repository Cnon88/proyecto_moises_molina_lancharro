package com.moises.quedadaseventos.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


public class TokenManager {
    private static final String PREF_NAME = "AuthQuedadasPrefs";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_TOKEN_EXPIRATION = "token_expiration";

    private final SharedPreferences prefs;

    public TokenManager(Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token, long expirationTimeInMillis) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_TOKEN, token);
        editor.putLong(KEY_TOKEN_EXPIRATION, expirationTimeInMillis);
        editor.apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public boolean hasToken() {
        return getToken() != null;
    }

    public boolean isTokenExpired() {
        long now = System.currentTimeMillis();
        long expirationTime = prefs.getLong(KEY_TOKEN_EXPIRATION, 0);
        return expirationTime < now;
    }

    public void clearToken() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_TOKEN);
        editor.remove(KEY_TOKEN_EXPIRATION);
        editor.apply();
    }
}