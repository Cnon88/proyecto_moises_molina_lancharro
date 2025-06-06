package com.moises.quedadaseventos.activitiy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.dto.CrearEventoDto;
import com.moises.quedadaseventos.dto.EventoDto;
import com.moises.quedadaseventos.dto.JuegoDto;
import com.moises.quedadaseventos.retrofit.ApiClient;
import com.moises.quedadaseventos.retrofit.ApiService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CrearEventoActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Views
    private MaterialToolbar toolbar;
    private TextInputEditText etTitulo;
    private TextInputEditText etDescripcion;
    private AutoCompleteTextView actvJuego;
    private TextInputEditText etMinPersonas;
    private TextInputEditText etMaxPersonas;
    private TextInputEditText etFechaInicioInscripciones;
    private TextInputEditText etFechaFinInscripciones;
    private TextInputEditText etFechaEvento;
    private TextInputEditText etHoraEvento;
    private MaterialButton btnSeleccionarUbicacion;
    private MaterialButton btnCrearEvento;
    private MaterialButton btnCancelar;

    // TextInputLayouts para mostrar errores
    private TextInputLayout tilTitulo;
    private TextInputLayout tilDescripcion;
    private TextInputLayout tilJuego;
    private TextInputLayout tilMinPersonas;
    private TextInputLayout tilMaxPersonas;
    private TextInputLayout tilFechaInicioInscripciones;
    private TextInputLayout tilFechaFinInscripciones;
    private TextInputLayout tilFechaEvento;
    private TextInputLayout tilHoraEvento;

    // Datos
    private List<JuegoDto> listaJuegos = new ArrayList<>();
    private JuegoDto juegoSeleccionado;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private GoogleMap mMap;
    private Marker marcadorUbicacion;

    // API
    private ApiService apiService;

    // Formatters para fechas
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_evento);

        initViews();
        initRetrofit();
        setupToolbar();
        setupClickListeners();
        setupDateTimePickers();
        loadJuegos();
        setupMap();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        etTitulo = findViewById(R.id.et_titulo);
        etDescripcion = findViewById(R.id.et_descripcion);
        actvJuego = findViewById(R.id.actv_juego);
        etMinPersonas = findViewById(R.id.et_min_personas);
        etMaxPersonas = findViewById(R.id.et_max_personas);
        etFechaInicioInscripciones = findViewById(R.id.et_fecha_inicio_inscripciones);
        etFechaFinInscripciones = findViewById(R.id.et_fecha_fin_inscripciones);
        etFechaEvento = findViewById(R.id.et_fecha_evento);
        etHoraEvento = findViewById(R.id.et_hora_evento);
        btnSeleccionarUbicacion = findViewById(R.id.btn_seleccionar_ubicacion);
        btnCrearEvento = findViewById(R.id.btn_crear_evento);
        btnCancelar = findViewById(R.id.btn_cancelar);

        // TextInputLayouts
        tilTitulo = findViewById(R.id.til_titulo);
        tilDescripcion = findViewById(R.id.til_descripcion);
        tilJuego = findViewById(R.id.til_juego);
        tilMinPersonas = findViewById(R.id.til_min_personas);
        tilMaxPersonas = findViewById(R.id.til_max_personas);
        tilFechaInicioInscripciones = findViewById(R.id.til_fecha_inicio_inscripciones);
        tilFechaFinInscripciones = findViewById(R.id.til_fecha_fin_inscripciones);
        tilFechaEvento = findViewById(R.id.til_fecha_evento);
        tilHoraEvento = findViewById(R.id.til_hora_evento);
    }

    private void initRetrofit() {
        Retrofit clientWithInterceptor = ApiClient.getClientWithInterceptor(getApplicationContext());
        apiService = clientWithInterceptor.create(ApiService.class);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Crear Evento");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        btnCrearEvento.setOnClickListener(v -> crearEvento());
        btnCancelar.setOnClickListener(v -> finish());
        btnSeleccionarUbicacion.setOnClickListener(v -> abrirSeleccionUbicacion());
    }

    private void setupDateTimePickers() {
        // Fecha inicio inscripciones
        etFechaInicioInscripciones.setOnClickListener(v -> showDatePicker((fecha) -> {
            etFechaInicioInscripciones.setText(fecha);
            tilFechaInicioInscripciones.setError(null);
        }));

        // Fecha fin inscripciones
        etFechaFinInscripciones.setOnClickListener(v -> showDatePicker((fecha) -> {
            etFechaFinInscripciones.setText(fecha);
            tilFechaFinInscripciones.setError(null);
        }));

        // Fecha evento
        etFechaEvento.setOnClickListener(v -> showDatePicker((fecha) -> {
            etFechaEvento.setText(fecha);
            tilFechaEvento.setError(null);
        }));

        // Hora evento
        etHoraEvento.setOnClickListener(v -> showTimePicker((hora) -> {
            etHoraEvento.setText(hora);
            tilHoraEvento.setError(null);
        }));
    }

    private void showDatePicker(OnDateSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String fecha = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    listener.onDateSelected(fecha);
                },
                year, month, day
        );

        // No permitir fechas pasadas
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void showTimePicker(OnTimeSelectedListener listener) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, selectedHour, selectedMinute) -> {
                    String hora = String.format("%02d:%02d", selectedHour, selectedMinute);
                    listener.onTimeSelected(hora);
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    private void loadJuegos() {
        apiService.getJuegos().enqueue(new Callback<List<JuegoDto>>() {
            @Override
            public void onResponse(Call<List<JuegoDto>> call, Response<List<JuegoDto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaJuegos = response.body();
                    setupJuegosDropdown();
                }
            }

            @Override
            public void onFailure(Call<List<JuegoDto>> call, Throwable t) {
                showMessage("Error al cargar los juegos");
            }
        });
    }

    private void setupJuegosDropdown() {
        List<String> nombresJuegos = new ArrayList<>();
        for (JuegoDto juego : listaJuegos) {
            nombresJuegos.add(juego.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                nombresJuegos
        );
        actvJuego.setAdapter(adapter);

        actvJuego.setOnItemClickListener((parent, view, position, id) -> {
            String nombreSeleccionado = (String) parent.getItemAtPosition(position);
            for (JuegoDto juego : listaJuegos) {
                if (juego.getNombre().equals(nombreSeleccionado)) {
                    juegoSeleccionado = juego;
                    tilJuego.setError(null);

                    // ACTUALIZA LOS CAMPOS DE MIN Y MAX PERSONAS
                    etMinPersonas.setText(String.valueOf(juego.getMinJugadores()));
                    etMaxPersonas.setText(String.valueOf(juego.getMaxJugadores()));

                    break;
                }
            }
        });
    }

    private void setupMap() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
    }

    private void abrirSeleccionUbicacion() {
        Intent intent = new Intent(this, SeleccionarUbicacionActivity.class);
        if (latitud != null && longitud != null) {
            intent.putExtra("latitud", latitud.doubleValue());
            intent.putExtra("longitud", longitud.doubleValue());
        }
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra("latitud", 0);
            double lng = data.getDoubleExtra("longitud", 0);

            latitud = BigDecimal.valueOf(lat);
            longitud = BigDecimal.valueOf(lng);

            actualizarMapa();
            btnSeleccionarUbicacion.setText("Ubicación seleccionada");
        }
    }

    private void actualizarMapa() {
        if (mMap != null && latitud != null && longitud != null) {
            LatLng ubicacion = new LatLng(latitud.doubleValue(), longitud.doubleValue());

            if (marcadorUbicacion != null) {
                marcadorUbicacion.remove();
            }

            marcadorUbicacion = mMap.addMarker(new MarkerOptions()
                    .position(ubicacion)
                    .title("Ubicación del evento"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f));
        }
    }

    private void crearEvento() {
        if (!validarCampos()) {
            return;
        }

        // Crear el DTO del evento
        CrearEventoDto nuevoEvento = new CrearEventoDto();
        nuevoEvento.setTitulo(etTitulo.getText().toString().trim());
        nuevoEvento.setDescripcion(etDescripcion.getText().toString().trim());
        nuevoEvento.setJuegoId(juegoSeleccionado.getId());
        nuevoEvento.setMinPersonas(Integer.parseInt(etMinPersonas.getText().toString().trim()));
        nuevoEvento.setMaxPersonas(Integer.parseInt(etMaxPersonas.getText().toString().trim()));
        nuevoEvento.setLatitud(latitud);
        nuevoEvento.setLongitud(longitud);

        // Convertir fechas
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            LocalDateTime fechaInicio = LocalDateTime.parse(
                    etFechaInicioInscripciones.getText().toString() + " 00:00", formatter
            );
            LocalDateTime fechaFin = LocalDateTime.parse(
                    etFechaFinInscripciones.getText().toString() + " 23:59", formatter
            );
            LocalDateTime fechaEvento = LocalDateTime.parse(
                    etFechaEvento.getText().toString() + " " + etHoraEvento.getText().toString(), formatter
            );

            nuevoEvento.setFechaInicioInscripciones(fechaInicio.format(dateTimeFormatter));
            nuevoEvento.setFechaFinInscripciones(fechaFin.format(dateTimeFormatter));
            nuevoEvento.setFechaEvento(fechaEvento.format(dateTimeFormatter));
        } catch (Exception e) {
            showMessage("Error en el formato de fechas");
            return;
        }

        // Deshabilitar botón y mostrar loading
        btnCrearEvento.setEnabled(false);
        btnCrearEvento.setText("Creando...");

        // Llamada a la API
        apiService.crearEvento(nuevoEvento).enqueue(new Callback<EventoDto>() {
            @Override
            public void onResponse(Call<EventoDto> call, Response<EventoDto> response) {
                btnCrearEvento.setEnabled(true);
                btnCrearEvento.setText("Crear Evento");

                if (response.isSuccessful()) {
                    showMessage("Evento creado exitosamente");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showMessage("Error al crear el evento");
                }
            }

            @Override
            public void onFailure(Call<EventoDto> call, Throwable t) {
                btnCrearEvento.setEnabled(true);
                btnCrearEvento.setText("Crear Evento");
                showMessage("Error de conexión");
            }
        });
    }

    private boolean validarCampos() {
        boolean esValido = true;

        // Limpiar errores previos
        limpiarErrores();

        // Validar título
        if (TextUtils.isEmpty(etTitulo.getText())) {
            tilTitulo.setError("El título es requerido");
            esValido = false;
        }

        // Validar descripción
        if (TextUtils.isEmpty(etDescripcion.getText())) {
            tilDescripcion.setError("La descripción es requerida");
            esValido = false;
        }

        // Validar juego
        if (juegoSeleccionado == null) {
            tilJuego.setError("Selecciona un juego");
            esValido = false;
        }

        // Validar número mínimo de personas
        try {
            int minPersonas = Integer.parseInt(etMinPersonas.getText().toString().trim());
            if (minPersonas < 1) {
                tilMinPersonas.setError("Debe ser mayor a 0");
                esValido = false;
            }
        } catch (NumberFormatException e) {
            tilMinPersonas.setError("Ingresa un número válido");
            esValido = false;
        }

        // Validar número máximo de personas
        try {
            int maxPersonas = Integer.parseInt(etMaxPersonas.getText().toString().trim());
            int minPersonas = Integer.parseInt(etMinPersonas.getText().toString().trim());

            if (maxPersonas < minPersonas) {
                tilMaxPersonas.setError("Debe ser mayor o igual al mínimo");
                esValido = false;
            }
        } catch (NumberFormatException e) {
            tilMaxPersonas.setError("Ingresa un número válido");
            esValido = false;
        }

        // Validar fechas
        if (TextUtils.isEmpty(etFechaInicioInscripciones.getText())) {
            tilFechaInicioInscripciones.setError("Selecciona la fecha de inicio");
            esValido = false;
        }

        if (TextUtils.isEmpty(etFechaFinInscripciones.getText())) {
            tilFechaFinInscripciones.setError("Selecciona la fecha de fin");
            esValido = false;
        }

        if (TextUtils.isEmpty(etFechaEvento.getText())) {
            tilFechaEvento.setError("Selecciona la fecha del evento");
            esValido = false;
        }

        if (TextUtils.isEmpty(etHoraEvento.getText())) {
            tilHoraEvento.setError("Selecciona la hora del evento");
            esValido = false;
        }

        // Validar ubicación
        if (latitud == null || longitud == null) {
            showMessage("Selecciona una ubicación para el evento");
            esValido = false;
        }

        return esValido;
    }

    private void limpiarErrores() {
        tilTitulo.setError(null);
        tilDescripcion.setError(null);
        tilJuego.setError(null);
        tilMinPersonas.setError(null);
        tilMaxPersonas.setError(null);
        tilFechaInicioInscripciones.setError(null);
        tilFechaFinInscripciones.setError(null);
        tilFechaEvento.setError(null);
        tilHoraEvento.setError(null);
    }

    private String convertirFechaParaAPI(String fecha, String hora) {
        // Convertir de "dd/MM/yyyy" y "HH:mm" a "yyyy-MM-ddTHH:mm:ss"
        String[] partesFecha = fecha.split("/");
        String fechaFormateada = partesFecha[2] + "-" +
                String.format("%02d", Integer.parseInt(partesFecha[1])) + "-" +
                String.format("%02d", Integer.parseInt(partesFecha[0]));

        return fechaFormateada + "T" + hora + ":00";
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Configurar mapa para solo mostrar, no interactuar
        mMap.getUiSettings().setAllGesturesEnabled(false);

        // Centrar en una ubicación por defecto (Madrid, por ejemplo)
        LatLng madrid = new LatLng(40.4168, -3.7038);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 10f));
    }

    // Interfaces para callbacks
    private interface OnDateSelectedListener {
        void onDateSelected(String fecha);
    }

    private interface OnTimeSelectedListener {
        void onTimeSelected(String hora);
    }
}