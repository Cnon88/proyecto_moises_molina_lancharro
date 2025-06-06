package com.moises.quedadaseventos.activitiy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.adapter.AccionBotonUserItem;
import com.moises.quedadaseventos.adapter.UsuariosAdapter;
import com.moises.quedadaseventos.dto.EventoDto;
import com.moises.quedadaseventos.dto.UsuarioDto;
import com.moises.quedadaseventos.retrofit.ApiClient;
import com.moises.quedadaseventos.retrofit.ApiService;

import java.util.ArrayList;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetalleEventoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EventoDto evento;

    // Views
    private ImageView ivGameImage;
    private TextView tvEventTitle;
    private Chip chipEstado;
    private TextView tvGameName;
    private TextView tvDescription;
    private TextView tvEventDate;
    private TextView tvRegistrationPeriod;
    private TextView tvParticipants;
    private TextView tvCreatorName;
    private TextView tvParticipantCount;
    private RecyclerView rvParticipants;
    private MaterialButton btnJoinEvent;
    private MaterialButton btnShareEvent;
    private MaterialButton btnOpenMap;

    private ApiService apiService;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initRetrofit();
        getEventFromIntent();
        setupClickListeners();
    }

    private void initViews() {
        setContentView(R.layout.activity_detalle_evento);

        ivGameImage = findViewById(R.id.iv_game_image);
        tvEventTitle = findViewById(R.id.tv_event_title);
        chipEstado = findViewById(R.id.chip_estado);
        tvGameName = findViewById(R.id.tv_game_name);
        tvDescription = findViewById(R.id.tv_description);
        tvEventDate = findViewById(R.id.tv_event_date);
        tvRegistrationPeriod = findViewById(R.id.tv_registration_period);
        tvParticipants = findViewById(R.id.tv_participants);
        tvCreatorName = findViewById(R.id.tv_creator_name);
        tvParticipantCount = findViewById(R.id.tv_participant_count);
        rvParticipants = findViewById(R.id.rv_participants);
        btnJoinEvent = findViewById(R.id.btn_join_event);
        btnShareEvent = findViewById(R.id.btn_share_event);
        btnOpenMap = findViewById(R.id.btn_open_map);

        // Crear y agregar SupportMapFragment dinámicamente al contenedor
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_map_placeholder, mapFragment)
                .commit();

        mapFragment.getMapAsync(this);
    }

    private void initRetrofit() {
        // Iniciar retrofit
        Retrofit clientWithInterceptor = ApiClient.getClientWithInterceptor(getApplicationContext());
        apiService = clientWithInterceptor.create(ApiService.class);
    }

    private void loadEventoData() {
        if (evento == null) return;

        // Título
        tvEventTitle.setText(evento.getTitulo());

        // Estado
        setupEstadoChip(evento.getEstado());

        // Juego
        if (evento.getJuego() != null) {
            tvGameName.setText(evento.getJuego().getNombre());
            loadGameImage(evento.getJuego().getNombre());
        }

        // Descripción
        tvDescription.setText(evento.getDescripcion());

        // Fecha del evento
        tvEventDate.setText(evento.getFechaEvento().replace("T", " "));

        // Período de inscripción
        String registrationPeriod = evento.getFechaInicioInscripciones().replace("T", " ") +
                " - " + evento.getFechaFinInscripciones().replace("T", " ");
        tvRegistrationPeriod.setText(registrationPeriod);

        // Participantes
        String participantsText = evento.getMinPersonas() + " - " + evento.getMaxPersonas() + " personas";
        tvParticipants.setText(participantsText);

        // Coordenadas
        if (evento != null && evento.getLatitud() != null && evento.getLongitud() != null && mMap != null) {
            String coordinates = "Lat: " + evento.getLatitud() + ", Lng: " + evento.getLongitud();
            // Define latitud y longitud
            LatLng ubicacion = new LatLng(evento.getLatitud().doubleValue(), evento.getLongitud().doubleValue());

            // Coloca un marcador en esa ubicación
            Marker marker = mMap.addMarker(new MarkerOptions().position(ubicacion).title(evento.getTitulo()));

            // Mueve la cámara al marcador con zoom 15
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f));

            if (marker != null) {
                marker.showInfoWindow(); // <-- Esto abre el título por defecto
            }

            // Deshabilitar gestos para que el mapa no se mueva
            mMap.getUiSettings().setAllGesturesEnabled(false);
        }

        // Creador
        if (evento.getCreador() != null) {
            tvCreatorName.setText(evento.getCreador().getNickname());
        }

        // Lista de participantes
        setupParticipantsList();
    }

    private void setupEstadoChip(EventoDto.EstadoEvento estado) {
        chipEstado.setText(estado.toString());

        switch (estado) {
            case PENDIENTE:
                chipEstado.setChipBackgroundColorResource(R.color.status_active);
                break;
            case LLENO:
                chipEstado.setChipBackgroundColorResource(R.color.status_inactive);
                break;
            case FINALIZADO:
                chipEstado.setChipBackgroundColorResource(R.color.text_secondary);
                break;
        }
    }

    private void loadGameImage(String nombreJuego) {
        int imageResourceId = getGameImageResource(nombreJuego);

        if (imageResourceId != 0) {
            ivGameImage.setImageResource(imageResourceId);
        }
    }

    private int getGameImageResource(String nombreJuego) {
        switch (nombreJuego) {
            case "Paleo":
                return R.drawable.paleo;
            case "After the virus":
                return R.drawable.after_the_virus;
            case "Guerra del Anillo":
                return R.drawable.guerra_del_anillo;
            case "Pequeñas grandes mazmorras":
                return R.drawable.pequenas_grandes_mazmorras;
        }

        return R.drawable.paleo;
    }

    private void setupParticipantsList() {
        if (evento.getParticipantes() != null && !evento.getParticipantes().isEmpty()) {
            tvParticipantCount.setText(String.valueOf(evento.getParticipantes().size()));

            UsuariosAdapter adapter = new UsuariosAdapter(new ArrayList<>(evento.getParticipantes()), null);

            AccionBotonUserItem callback = new AccionBotonUserItem() {
                @Override
                public void onAccionRealizada() { }

                @Override
                public TipoAccion getTipoAccion() {
                    return TipoAccion.SIN_ACCION;
                }
            };

            adapter.setCallback(callback);

            // Configurar RecyclerView
            rvParticipants.setLayoutManager(new LinearLayoutManager(this));
            rvParticipants.setAdapter(adapter);
        } else {
            tvParticipantCount.setText("0");
        }
    }

    private void setupClickListeners() {
        btnJoinEvent.setOnClickListener(v -> joinEvent());
        btnShareEvent.setOnClickListener(v -> shareEvent());
        btnOpenMap.setOnClickListener(v -> openMap());
    }

    private void joinEvent() {
        // Implementar lógica para unirse al evento
        // Ejemplo: llamada a API, validaciones, etc.

        // Verificar si ya está inscrito
        if (isUserAlreadyJoined()) {
            showMessage("Ya estás inscrito en este evento");
            return;
        }

        // Verificar límite de participantes
        if (evento.getParticipantes().size() >= evento.getMaxPersonas()) {
            showMessage("El evento ha alcanzado el límite máximo de participantes");
            return;
        }

        // Aquí harías la llamada al API para inscribirse
        joinEventAPI();
    }

    private void shareEvent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        String shareText = "¡Mira este evento de " + evento.getJuego().getNombre() + "!\n\n" +
                evento.getTitulo() + "\n" +
                "Fecha: " + evento.getFechaEvento().replace("T", " ") + "\n" +
                "Participantes: " + evento.getMinPersonas() + "-" + evento.getMaxPersonas() + " personas";

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Evento: " + evento.getTitulo());

        startActivity(Intent.createChooser(shareIntent, "Compartir evento"));
    }

    private void openMap() {
        if (evento.getLatitud() != null && evento.getLongitud() != null) {
            String uri = "geo:" + evento.getLatitud() + "," + evento.getLongitud() +
                    "?q=" + evento.getLatitud() + "," + evento.getLongitud() +
                    "(" + evento.getTitulo() + ")";

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Si Google Maps no está instalado, abrir en navegador
                String url = "https://www.google.com/maps?q=" + evento.getLatitud() + "," + evento.getLongitud();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        }
    }

    private boolean isUserAlreadyJoined() {
        // Implementar lógica para verificar si el usuario actual ya está inscrito
        // Ejemplo: comparar IDs de usuario
        return false; // placeholder
    }

    private void joinEventAPI() {
        // Mostrar loading
        btnJoinEvent.setEnabled(false);
        btnJoinEvent.setText("Uniéndose...");

        // Simular llamada API (reemplazar con tu implementación real)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Actualizar UI después de unirse exitosamente
            showMessage("¡Te has unido al evento exitosamente!");
            btnJoinEvent.setText("Ya inscrito");
            btnJoinEvent.setEnabled(false);

            // Actualizar contador de participantes
            int currentCount = Integer.parseInt(tvParticipantCount.getText().toString());
            tvParticipantCount.setText(String.valueOf(currentCount + 1));

        }, 2000);
    }

    private void openChat() {
        // Implementar apertura de chat con el creador
    }

    private void openProfile() {
        // Implementar apertura de perfil del creador
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Método para recibir el evento desde el Intent
    private void getEventFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("eventoId")) {
            int eventoId = intent.getIntExtra("eventoId", -1);
            // Cargar evento por ID desde API o base de datos
            loadEventById(eventoId);
        }
    }

    private void loadEventById(int eventoId) {
        apiService.getDetalleEvento(eventoId).enqueue(new Callback<EventoDto>() {
            @Override
            public void onResponse(Call<EventoDto> call, Response<EventoDto> response) {
                if (response.isSuccessful()) {
                    evento = response.body();
                    loadEventoData();
                }
            }

            @Override
            public void onFailure(Call<EventoDto> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (evento != null && evento.getLatitud() != null && evento.getLongitud() != null && mMap != null) {
            String coordinates = "Lat: " + evento.getLatitud() + ", Lng: " + evento.getLongitud();
            // Define latitud y longitud
            LatLng ubicacion = new LatLng(evento.getLatitud().doubleValue(), evento.getLongitud().doubleValue());

            // Coloca un marcador en esa ubicación
            Marker marker = mMap.addMarker(new MarkerOptions().position(ubicacion).title(evento.getTitulo()));

            // Mueve la cámara al marcador con zoom 15
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f));

            if (marker != null) {
                marker.showInfoWindow(); // <-- Esto abre el título por defecto
            }

            // Deshabilitar gestos para que el mapa no se mueva
            mMap.getUiSettings().setAllGesturesEnabled(false);
        }
    }
}