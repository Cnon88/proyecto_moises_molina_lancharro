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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.adapter.AccionBotonUserItem;
import com.moises.quedadaseventos.adapter.UsuariosAdapter;
import com.moises.quedadaseventos.dto.UsuarioDto;
import com.moises.quedadaseventos.retrofit.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingFragment extends Fragment {

    private static final String FOLLOWINGS_KEY = "followings_key";
    private final ApiService apiService;

    public FollowingFragment(ApiService apiService) {
        this.apiService = apiService;
    }

    public static FollowingFragment newInstance(ArrayList<UsuarioDto> followings, int userId, ApiService apiService) {
        FollowingFragment fragment = new FollowingFragment(apiService);
        Bundle args = new Bundle();
        args.putParcelableArrayList(FOLLOWINGS_KEY, followings);
        args.putInt("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.users_list_profile, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewUsersProfile);

        ArrayList<UsuarioDto> siguiendo = getArguments().getParcelableArrayList(FOLLOWINGS_KEY);
        int userId = getArguments().getInt("userId");

        UsuariosAdapter adapter = new UsuariosAdapter(siguiendo, apiService);
        AccionBotonUserItem callback = new AccionBotonUserItem() {
            @Override
            public void onAccionRealizada() {
                List<UsuarioDto> usersInAdapter = adapter.getUsuarios();
                apiService.getUsuariosSiguiendo(userId).enqueue(new Callback<Set<UsuarioDto>>() {
                    @Override
                    public void onResponse(Call<Set<UsuarioDto>> call, Response<Set<UsuarioDto>> response) {
                        if (response.isSuccessful()) {
                            adapter.actualizarUsuarios(new ArrayList<>(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Set<UsuarioDto>> call, Throwable t) {
                        Toast.makeText(getContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public TipoAccion getTipoAccion() {
                return TipoAccion.DEJAR_DE_SEGUIR;
            }
        };

        adapter.setCallback(callback);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
