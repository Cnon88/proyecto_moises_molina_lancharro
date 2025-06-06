package com.moises.quedadaseventos.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.adapter.AccionBotonUserItem;
import com.moises.quedadaseventos.adapter.UsuariosAdapter;
import com.moises.quedadaseventos.dto.UsuarioDto;

import java.util.ArrayList;

public class FollowersFragment extends Fragment {

    private static final String FOLLOWERS_KEY = "followers_key";

    public static FollowersFragment newInstance(ArrayList<UsuarioDto> followers) {
        FollowersFragment fragment = new FollowersFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(FOLLOWERS_KEY, followers);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.users_list_profile, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewUsersProfile);

        ArrayList<UsuarioDto> seguidores = getArguments().getParcelableArrayList(FOLLOWERS_KEY);

        UsuariosAdapter adapter = new UsuariosAdapter(seguidores, null);
        AccionBotonUserItem callback = new AccionBotonUserItem() {
            @Override
            public void onAccionRealizada() { }

            @Override
            public TipoAccion getTipoAccion() {
                return TipoAccion.SIN_ACCION;
            }
        };

        adapter.setCallback(callback);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
