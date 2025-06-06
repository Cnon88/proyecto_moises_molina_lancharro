package com.moises.quedadaseventos.activitiy;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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
import com.google.android.material.textview.MaterialTextView;
import com.moises.quedadaseventos.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SeleccionarUbicacionActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcadorSeleccionado;
    private LatLng ubicacionSeleccionada;

    // Views
    private MaterialToolbar toolbar;
    private MaterialTextView tvDireccion;
    private MaterialButton btnConfirmar;
    private MaterialButton btnCancelar;

    // Geocoder para obtener direcciones
    private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_ubicacion);

        initViews();
        setupToolbar();
        setupClickListeners();
        setupMap();

        geocoder = new Geocoder(this, Locale.getDefault());

        // Verificar si se pasó una ubicación previa
        Intent intent = getIntent();
        if (intent.hasExtra("latitud") && intent.hasExtra("longitud")) {
            double lat = intent.getDoubleExtra("latitud", 0);
            double lng = intent.getDoubleExtra("longitud", 0);
            ubicacionSeleccionada = new LatLng(lat, lng);
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar_ubicacion);
        tvDireccion = findViewById(R.id.tv_direccion);
        btnConfirmar = findViewById(R.id.btn_confirmar_ubicacion);
        btnCancelar = findViewById(R.id.btn_cancelar_ubicacion);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Seleccionar Ubicación");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupClickListeners() {
        btnConfirmar.setOnClickListener(v -> confirmarUbicacion());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_seleccion);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Configurar el mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Configurar listener para clicks en el mapa
        mMap.setOnMapClickListener(this::onMapClick);

        // Si hay una ubicación previa, mostrarla
        if (ubicacionSeleccionada != null) {
            actualizarMarcador(ubicacionSeleccionada);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionSeleccionada, 15f));
        } else {
            // Centrar en Madrid por defecto
            LatLng madrid = new LatLng(40.4168, -3.7038);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(madrid, 12f));
        }
    }

    private void onMapClick(LatLng latLng) {
        ubicacionSeleccionada = latLng;
        actualizarMarcador(latLng);
        obtenerDireccion(latLng);
        btnConfirmar.setEnabled(true);
    }

    private void actualizarMarcador(LatLng latLng) {
        // Remover marcador anterior si existe
        if (marcadorSeleccionado != null) {
            marcadorSeleccionado.remove();
        }

        // Agregar nuevo marcador
        marcadorSeleccionado = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Ubicación seleccionada"));

        if (marcadorSeleccionado != null) {
            marcadorSeleccionado.showInfoWindow();
        }
    }

    private void obtenerDireccion(LatLng latLng) {
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1
            );

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String direccion = construirDireccion(address);
                tvDireccion.setText(direccion);
            } else {
                tvDireccion.setText("Lat: " + String.format("%.6f", latLng.latitude) +
                        ", Lng: " + String.format("%.6f", latLng.longitude));
            }
        } catch (IOException e) {
            tvDireccion.setText("Lat: " + String.format("%.6f", latLng.latitude) +
                    ", Lng: " + String.format("%.6f", latLng.longitude));
        }
    }

    private String construirDireccion(Address address) {
        StringBuilder direccion = new StringBuilder();

        if (address.getThoroughfare() != null) {
            direccion.append(address.getThoroughfare());
        }

        if (address.getSubThoroughfare() != null) {
            if (direccion.length() > 0) direccion.append(" ");
            direccion.append(address.getSubThoroughfare());
        }

        if (address.getLocality() != null) {
            if (direccion.length() > 0) direccion.append(", ");
            direccion.append(address.getLocality());
        }

        if (address.getAdminArea() != null) {
            if (direccion.length() > 0) direccion.append(", ");
            direccion.append(address.getAdminArea());
        }

        if (address.getCountryName() != null) {
            if (direccion.length() > 0) direccion.append(", ");
            direccion.append(address.getCountryName());
        }

        return direccion.toString().isEmpty() ?
                "Ubicación seleccionada" : direccion.toString();
    }

    private void confirmarUbicacion() {
        if (ubicacionSeleccionada != null) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("latitud", ubicacionSeleccionada.latitude);
            resultIntent.putExtra("longitud", ubicacionSeleccionada.longitude);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "Selecciona una ubicación en el mapa", Toast.LENGTH_SHORT).show();
        }
    }
}