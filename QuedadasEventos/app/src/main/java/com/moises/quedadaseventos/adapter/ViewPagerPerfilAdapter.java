package com.moises.quedadaseventos.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.moises.quedadaseventos.dto.UsuarioDto;
import com.moises.quedadaseventos.fragment.FollowersFragment;
import com.moises.quedadaseventos.fragment.FollowingFragment;
import com.moises.quedadaseventos.retrofit.ApiService;

import java.util.ArrayList;

public class ViewPagerPerfilAdapter extends FragmentStateAdapter {

    private final ArrayList<UsuarioDto> followers;
    private final ArrayList<UsuarioDto> following;
    private final ApiService apiService;
    private int userId;

    public ViewPagerPerfilAdapter(@NonNull FragmentActivity fragmentActivity,
                                  ArrayList<UsuarioDto> followers,
                                  ArrayList<UsuarioDto> following,
                                  int userId,
                                  ApiService apiService) {
        super(fragmentActivity);
        this.followers = followers;
        this.following = following;
        this.apiService = apiService;
        this.userId = userId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return FollowersFragment.newInstance(followers);
        } else {
            return FollowingFragment.newInstance(following, userId, apiService);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

