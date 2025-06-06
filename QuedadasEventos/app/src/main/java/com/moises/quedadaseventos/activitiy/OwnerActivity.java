package com.moises.quedadaseventos.activitiy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.adapter.AccionBotonEventoItem;
import com.moises.quedadaseventos.databinding.ActivityOwnerBinding;
import com.moises.quedadaseventos.listener.BottomBarItemSelectedListener;
import com.moises.quedadaseventos.adapter.EventosAdapter;
import com.moises.quedadaseventos.databinding.ActivityEventosBinding;
import com.moises.quedadaseventos.dto.EventoDto;
import com.moises.quedadaseventos.retrofit.ApiClient;
import com.moises.quedadaseventos.retrofit.ApiService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OwnerActivity extends AppCompatActivity {

    private ActivityOwnerBinding binding;
    private EventosAdapter adapter;
    private ApiService apiService;

    private RecyclerView recyclerViewEventosCreados;
    private ArrayList<EventoDto> eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initRetrofit();
        initAdapters();
        fetchEvents();
        setupListeners();
    }

    private void fetchEvents() {
        apiService.obtenerEventosCreados().enqueue(new Callback<Set<EventoDto>>() {
            @Override
            public void onResponse(Call<Set<EventoDto>> call, Response<Set<EventoDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Limpiar la lista actual de eventos
                    eventos.clear();

                    // Añadir todos los eventos recibidos de la API
                    Set<EventoDto> events = response.body();
                    eventos.addAll(events);
                    eventos.sort(Comparator.comparing(EventoDto::getFechaEvento));

                    // Notificar al adaptador que los datos han cambiado
                    adapter.notifyDataSetChanged();
                } else {
                    // Manejar error en la respuesta
                    // Puedes mostrar un Toast o un mensaje en caso de error
                    Toast.makeText(getApplicationContext(),
                            "Error al cargar eventos: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Set<EventoDto>> call, Throwable t) {
                // Manejar error de red o de conversión
                Toast.makeText(getApplicationContext(),
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.bottomNavigationOwner.setOnItemSelectedListener(new BottomBarItemSelectedListener(OwnerActivity.this));
    }

    private void initRetrofit() {
        // Iniciar retrofit
        Retrofit clientWithInterceptor = ApiClient.getClientWithInterceptor(getApplicationContext());
        apiService = clientWithInterceptor.create(ApiService.class);
    }

    private void initAdapters() {
        eventos = new ArrayList<>();
        adapter = new EventosAdapter(this, eventos, new AccionBotonEventoItem() {
            @Override
            public void realizarAccion(int eventoId) {
                apiService.eliminarEvento(eventoId).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(OwnerActivity.this, "Eliminado correctamente", Toast.LENGTH_LONG).show();
                            fetchEvents();
                        }
                        else {
                            Toast.makeText(OwnerActivity.this, "Imposible eliminar", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(OwnerActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public TipoAccionEvento getTipoAccion() {
                return TipoAccionEvento.ELIMINAR;
            }
        });

        recyclerViewEventosCreados.setAdapter(adapter);
    }

    private void initViews() {
        binding = ActivityOwnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerViewEventosCreados = binding.recyclerViewEventosCreadosPorTi;
        recyclerViewEventosCreados.setLayoutManager(new LinearLayoutManager(this));

        binding.bottomNavigationOwner.setSelectedItemId(R.id.nav_my_events);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomNavigationOwner.setSelectedItemId(R.id.nav_my_events);
    }
}