package com.moises.quedadaseventos.activitiy;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.adapter.AccionBotonUserItem;
import com.moises.quedadaseventos.adapter.UsuariosAdapter;
import com.moises.quedadaseventos.databinding.ActivityUsuariosBinding;
import com.moises.quedadaseventos.dto.UsuarioDto;
import com.moises.quedadaseventos.listener.BottomBarItemSelectedListener;
import com.moises.quedadaseventos.retrofit.ApiClient;
import com.moises.quedadaseventos.retrofit.ApiService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UsuariosActivity  extends AppCompatActivity {

    private ActivityUsuariosBinding binding;
    private UsuariosAdapter adapter;
    private ApiService apiService;

    private RecyclerView recyclerViewUsuarios;
    private ArrayList<UsuarioDto> usuariosFinales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initRetrofit();
        initAdapters();

        cargarUsuarios();

        setupListeners();
    }

    private void cargarUsuarios() {
        apiService.getUsuarios().enqueue(new Callback<Set<UsuarioDto>>() {
            @Override
            public void onResponse(Call<Set<UsuarioDto>> call, Response<Set<UsuarioDto>> response) {
                if (response.isSuccessful()) {
                    Set<UsuarioDto> usuarios = response.body();
                    cargarPerfil(usuarios);
                } else {
                    mostrarError("Error al cargar usuarios: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Set<UsuarioDto>> call, Throwable t) {
                mostrarError("Error al cargar usuarios: " + t.getMessage());
            }
        });
    }

    private void cargarPerfil(Set<UsuarioDto> usuarios) {
        apiService.getPerfil().enqueue(new Callback<UsuarioDto>() {
            @Override
            public void onResponse(Call<UsuarioDto> call, Response<UsuarioDto> response) {
                if (response.isSuccessful()) {
                    UsuarioDto perfil = response.body();
                    cargarSiguiendo(usuarios, perfil);
                } else {
                    mostrarError("Error al cargar perfil: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UsuarioDto> call, Throwable t) {
                mostrarError("Error al cargar perfil: " + t.getMessage());
            }
        });
    }

    private void cargarSiguiendo(Set<UsuarioDto> usuarios, UsuarioDto perfil) {
        apiService.getUsuariosSiguiendo(perfil.getId()).enqueue(new Callback<Set<UsuarioDto>>() {
            @Override
            public void onResponse(Call<Set<UsuarioDto>> call, Response<Set<UsuarioDto>> response) {
                if (response.isSuccessful()) {
                    Set<UsuarioDto> siguiendo = response.body();
                    List<UsuarioDto> noSeguidos = usuarios.stream()
                            .filter(u -> !siguiendo.contains(u) && u.getId() != perfil.getId())
                            .collect(Collectors.toList());

                    usuariosFinales.clear();
                    usuariosFinales.addAll(noSeguidos);
                    usuariosFinales.sort(Comparator.comparing(UsuarioDto::getNickname, String.CASE_INSENSITIVE_ORDER));

                    adapter.notifyDataSetChanged();
                } else {
                    mostrarError("Error al cargar usuarios seguidos: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Set<UsuarioDto>> call, Throwable t) {
                mostrarError("Error al cargar usuarios seguidos: " + t.getMessage());
            }
        });
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();
    }


    private void setupListeners() {
        binding.bottomNavigationUsuarios.setOnItemSelectedListener(new BottomBarItemSelectedListener(UsuariosActivity.this));
    }

    private void initRetrofit() {
        // Iniciar retrofit
        Retrofit clientWithInterceptor = ApiClient.getClientWithInterceptor(getApplicationContext());
        apiService = clientWithInterceptor.create(ApiService.class);
    }

    private void initAdapters() {
        usuariosFinales = new ArrayList<>();
        adapter = new UsuariosAdapter(usuariosFinales, apiService);

        AccionBotonUserItem callback = new AccionBotonUserItem() {
            @Override
            public void onAccionRealizada() {
                apiService.getUsuarios().enqueue(new Callback<Set<UsuarioDto>>() {
                    @Override
                    public void onResponse(Call<Set<UsuarioDto>> call, Response<Set<UsuarioDto>> response) {
                        if (response.isSuccessful()) {
                            cargarUsuarios();
                        }
                    }

                    @Override
                    public void onFailure(Call<Set<UsuarioDto>> call, Throwable t) {
                        Toast.makeText(UsuariosActivity.this, "Error al recargar usuarios", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public TipoAccion getTipoAccion() {
                return TipoAccion.SEGUIR;
            }
        };

        adapter.setCallback(callback);

        recyclerViewUsuarios.setAdapter(adapter);
    }

    private void initViews() {
        binding = ActivityUsuariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerViewUsuarios = binding.recyclerViewUsuarios;
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));

        binding.bottomNavigationUsuarios.setSelectedItemId(R.id.nav_community);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomNavigationUsuarios.setSelectedItemId(R.id.nav_community);
    }
}