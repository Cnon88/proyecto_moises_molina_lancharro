package com.moises.quedadaseventos.activitiy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.adapter.ViewPagerPerfilAdapter;
import com.moises.quedadaseventos.dto.UsuarioDto;
import com.moises.quedadaseventos.listener.BottomBarItemSelectedListener;
import com.moises.quedadaseventos.databinding.ActivityPerfilBinding;
import com.moises.quedadaseventos.retrofit.ApiClient;
import com.moises.quedadaseventos.retrofit.ApiService;
import com.moises.quedadaseventos.utils.TokenManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerfilActivity extends AppCompatActivity {

    private ActivityPerfilBinding binding;
    private TokenManager tokenManager;
    private ApiService apiService;

    private List<UsuarioDto> followers;
    private List<UsuarioDto> following;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tokenManager = new TokenManager(getApplicationContext());
        initViews();
        initRetrofit();
        fetchPerfil();
        setupListeners();

        String[] tabTitles = {getString(R.string.seguidores), getString(R.string.seguidos)};

    }

    private void fetchPerfil() {
        apiService.getPerfil().enqueue(new Callback<UsuarioDto>() {
            @Override
            public void onResponse(Call<UsuarioDto> call, Response<UsuarioDto> response) {
                if (response.isSuccessful()) {
                    handlePerfilRetrieved(response);
                }
                else {
                    handlePerfilBadResponse(response);
                }
            }

            @Override
            public void onFailure(Call<UsuarioDto> call, Throwable t) {
                handlePerfilFailure(t);
            }
        });
    }

    private void handlePerfilBadResponse(Response<UsuarioDto> response) {
        Toast.makeText(getApplicationContext(), getString(R.string.error_recuperando_perfil), Toast.LENGTH_LONG).show();
    }

    private void handlePerfilRetrieved(Response<UsuarioDto> response) {
        UsuarioDto user = response.body();
        Integer id = user.getId();
        binding.tvUsername.setText(user.getNickname());
        binding.tvEmail.setText(user.getEmail());

        apiService.getUsuariosSiguiendo(id).enqueue(new Callback<Set<UsuarioDto>>() {
            @Override
            public void onResponse(Call<Set<UsuarioDto>> call, Response<Set<UsuarioDto>> responseFollowing) {
                following = responseFollowing.body() != null
                        ? new ArrayList<UsuarioDto>(responseFollowing.body()) : new ArrayList<>();

                following.sort(Comparator.comparing(UsuarioDto::getNickname, String.CASE_INSENSITIVE_ORDER));

                // Luego de obtener ambos sets, verifica si ya tienes los followers
                if (followers != null) {
                    setupViewPager(id);
                }
            }

            @Override
            public void onFailure(Call<Set<UsuarioDto>> call, Throwable t) { }
        });

        apiService.getUsuariosSeguidores(id).enqueue(new Callback<Set<UsuarioDto>>() {
            @Override
            public void onResponse(Call<Set<UsuarioDto>> call, Response<Set<UsuarioDto>> responseFollowers) {
                followers = responseFollowers.body() != null
                        ? new ArrayList<>(responseFollowers.body()) : new ArrayList<>();

                followers.sort(Comparator.comparing(UsuarioDto::getNickname, String.CASE_INSENSITIVE_ORDER));

                // Luego de obtener ambos sets, verifica si ya tienes los following
                if (following != null) {
                    setupViewPager(id);
                }
            }

            @Override
            public void onFailure(Call<Set<UsuarioDto>> call, Throwable t) { }
        });
    }

    private void setupViewPager(int userId) {
        ViewPagerPerfilAdapter adapter = new ViewPagerPerfilAdapter(this, new ArrayList<>(followers), new ArrayList<>(following), userId, apiService);
        binding.viewPagerPerfil.setAdapter(adapter);

        String[] tabTitles = {getString(R.string.seguidores), getString(R.string.seguidos)};
        new TabLayoutMediator(binding.tabLayoutPerfil, binding.viewPagerPerfil,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();
    }

    private void handlePerfilFailure(Throwable t) {
        Toast.makeText(getApplicationContext(), getString(R.string.error_recuperando_perfil) + ":" + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void initRetrofit() {
        // Iniciar retrofit
        Retrofit clientWithInterceptor = ApiClient.getClientWithInterceptor(getApplicationContext());
        apiService = clientWithInterceptor.create(ApiService.class);
    }

    private void setupListeners() {
        binding.bottomNavigationPerfil.setOnItemSelectedListener(new BottomBarItemSelectedListener(PerfilActivity.this));

        binding.btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenManager.clearToken();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getApplicationContext().startActivity(intent);
            }
        });
    }

    private void initViews() {
        binding = ActivityPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationPerfil.setSelectedItemId(R.id.nav_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomNavigationPerfil.setSelectedItemId(R.id.nav_profile);
    }
}