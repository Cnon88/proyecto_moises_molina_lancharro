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
import com.moises.quedadaseventos.dto.UsuarioDto;
import com.moises.quedadaseventos.listener.BottomBarItemSelectedListener;
import com.moises.quedadaseventos.adapter.EventosAdapter;
import com.moises.quedadaseventos.databinding.ActivityEventosBinding;
import com.moises.quedadaseventos.dto.EventoDto;
import com.moises.quedadaseventos.retrofit.ApiClient;
import com.moises.quedadaseventos.retrofit.ApiService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventosActivity extends AppCompatActivity {

    private ActivityEventosBinding binding;
    private EventosAdapter adapter;
    private ApiService apiService;

    private RecyclerView recyclerView;
    private ArrayList<EventoDto> eventosParticipando;
    private ArrayList<EventoDto> eventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initRetrofit();
        initAdapters();
        fetchInfo();
        setupListeners();
    }

    private void fetchInfo() {
        apiService.obtenerEventosParticipando().enqueue(new Callback<Set<EventoDto>>() {
            @Override
            public void onResponse(Call<Set<EventoDto>> call, Response<Set<EventoDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventosParticipando.clear();
                    eventosParticipando.addAll(response.body());

                    // Después de obtener los eventos participando, llamamos a obtener todos
                    fetchTodosEventos();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error al cargar eventos participando: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Set<EventoDto>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchTodosEventos() {
        apiService.getEventos().enqueue(new Callback<List<EventoDto>>() {
            @Override
            public void onResponse(Call<List<EventoDto>> call, Response<List<EventoDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<EventoDto> todosEventos = response.body();

                    // Crear un Set con los IDs de los eventos en los que participas
                    Set<Integer> idsParticipando = new HashSet<>();
                    for (EventoDto e : eventosParticipando) {
                        idsParticipando.add(e.getId());
                    }

                    // Filtrar eventos que no participas
                    List<EventoDto> eventosNoParticipando = new ArrayList<>();
                    for (EventoDto evento : todosEventos) {
                        if (!idsParticipando.contains(evento.getId())) {
                            eventosNoParticipando.add(evento);
                        }
                    }

                    // Actualizar la lista de eventos a mostrar
                    eventos.clear();
                    eventos.addAll(eventosNoParticipando);
                    eventos.sort(Comparator.comparing(EventoDto::getFechaEvento));
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error al cargar todos los eventos: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<EventoDto>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnNuevoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CrearEventoActivity.class);
                startActivity(i);
            }
        });

        binding.bottomNavigationEventos.setOnItemSelectedListener(new BottomBarItemSelectedListener(EventosActivity.this));
    }

    private void initRetrofit() {
        // Iniciar retrofit
        Retrofit clientWithInterceptor = ApiClient.getClientWithInterceptor(getApplicationContext());
        apiService = clientWithInterceptor.create(ApiService.class);
    }

    private void initAdapters() {
        eventos = new ArrayList<>();
        eventosParticipando = new ArrayList<>();
        adapter = new EventosAdapter(this, eventos, new AccionBotonEventoItem() {
            @Override
            public void realizarAccion(int eventoId) {
                apiService.inscribirseEnEvento(eventoId).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(EventosActivity.this, "Inscrito correctamente", Toast.LENGTH_LONG).show();
                            fetchInfo();
                        }
                        else {
                            Toast.makeText(EventosActivity.this, "Imposible inscribirse", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(EventosActivity.this, "Error", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public TipoAccionEvento getTipoAccion() {
                return TipoAccionEvento.INSCRIBIRSE;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initViews() {
        binding = ActivityEventosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.bottomNavigationEventos.setSelectedItemId(R.id.nav_events);
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomNavigationEventos.setSelectedItemId(R.id.nav_events);
    }
}