package com.moises.quedadaseventos.listener;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationBarView;
import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.activitiy.EventosActivity;
import com.moises.quedadaseventos.activitiy.EventosAsistenciaActivity;
import com.moises.quedadaseventos.activitiy.OwnerActivity;
import com.moises.quedadaseventos.activitiy.PerfilActivity;
import com.moises.quedadaseventos.activitiy.UsuariosActivity;

public class BottomBarItemSelectedListener implements NavigationBarView.OnItemSelectedListener {

    private Activity activity;

    public BottomBarItemSelectedListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Intent intent = null;

        if (item.getItemId() == R.id.nav_events) {
            intent = new Intent(activity, EventosActivity.class);
        }
        else if (item.getItemId() == R.id.nav_community) {
            intent = new Intent(activity, UsuariosActivity.class);
        }
        else if (item.getItemId() == R.id.nav_joined_events) {
            intent = new Intent(activity, EventosAsistenciaActivity.class);
        }
        else if (item.getItemId() == R.id.nav_my_events) {
            intent = new Intent(activity, OwnerActivity.class);
        }
        else if (item.getItemId() == R.id.nav_profile) {
            intent = new Intent(activity, PerfilActivity.class);
        }

        if (intent != null && !activity.getClass().getName().equals(intent.getComponent().getClassName())) {
            activity.startActivity(intent);
            return true;
        }

        return false;
    }
}
