package com.moises.quedadaseventos.activitiy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;
import com.moises.quedadaseventos.databinding.ActivityRegistroBinding;
import com.moises.quedadaseventos.dto.RegistroUsuarioDto;
import com.moises.quedadaseventos.dto.UsuarioDto;
import com.moises.quedadaseventos.retrofit.ApiClient;
import com.moises.quedadaseventos.retrofit.ApiService;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private ApiService apiService;

    private double latitude = 0.0;
    private double longitude = 0.0;
    private boolean locationObtained = false;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initRetrofit();
        initLocation();
        setupListeners();
        requestLocationPermission();
    }

    private void initViews() {
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initRetrofit() {
        Retrofit clientWithoutInterceptor = ApiClient.getClientWithoutInterceptor(getApplicationContext());
        apiService = clientWithoutInterceptor.create(ApiService.class);
    }

    private void initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(5000)
                .setMaxUpdateDelayMillis(15000)
                .setMaxUpdates(1)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                Location location = locationResult.getLastLocation();
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    locationObtained = true;

                    Toast.makeText(RegistroActivity.this,
                            "Ubicación obtenida correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void setupListeners() {
        binding.registerBtn.setOnClickListener(v -> attemptRegistration());

        binding.textViewLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Validación en tiempo real de contraseñas
        binding.editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                validatePasswordsMatch();
            }
        });
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this,
                        "Permiso de ubicación necesario para el registro",
                        Toast.LENGTH_LONG).show();
                // Opcional: usar ubicación por defecto o pedir de nuevo
                showLocationPermissionDialog();
            }
        }
    }

    private void showLocationPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Ubicación requerida")
                .setMessage("Para ofrecerte las mejores quedadas cercanas, necesitamos acceso a tu ubicación.")
                .setPositiveButton("Permitir", (dialog, which) -> requestLocationPermission())
                .setNegativeButton("Usar ubicación manual", (dialog, which) -> {
                    // Opcional: permitir introducir ubicación manualmente
                    useDefaultLocation();
                })
                .setCancelable(false)
                .show();
    }

    private void useDefaultLocation() {
        // Ubicación por defecto (Madrid, por ejemplo)
        latitude = 40.4168;
        longitude = -3.7038;
        locationObtained = true;
        Toast.makeText(this, "Usando ubicación por defecto", Toast.LENGTH_SHORT).show();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Primero intentar obtener la última ubicación conocida
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        locationObtained = true;
                        Toast.makeText(this, "Ubicación obtenida", Toast.LENGTH_SHORT).show();
                    } else {
                        // Si no hay última ubicación, solicitar nueva
                        requestNewLocation();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error obteniendo ubicación", Toast.LENGTH_SHORT).show();
                    requestNewLocation();
                });
    }

    private void requestNewLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void attemptRegistration() {
        String username = binding.editTextUsername.getText().toString().trim();
        String email = binding.editTextEmail.getText().toString().trim();
        String password = binding.editTextPassword.getText().toString();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString();

        // Validaciones locales
        if (!validateInputs(username, email, password, confirmPassword)) {
            return;
        }

        // Verificar que tenemos ubicación
        if (!locationObtained) {
            Toast.makeText(this, "Obteniendo ubicación, por favor espera...", Toast.LENGTH_SHORT).show();
            getCurrentLocation();
            return;
        }

        // Deshabilitar botón durante el registro
        binding.registerBtn.setEnabled(false);
        binding.registerBtn.setText("Registrando...");

        // Llamada a la API con Retrofit
        registerUser(username, email, password, latitude, longitude);
    }

    public boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateInputs(String username, String email, String password, String confirmPassword) {
        // Validar username
        if (username.isEmpty()) {
            binding.editTextUsername.setError("El nombre de usuario es obligatorio");
            binding.editTextUsername.requestFocus();
            return false;
        }

        if (username.length() < 3) {
            binding.editTextUsername.setError("El nombre de usuario debe tener al menos 3 caracteres");
            binding.editTextUsername.requestFocus();
            return false;
        }

        if (!isValidEmail(email)) {
            binding.editTextEmail.setError("El email debe ser valido");
            binding.editTextEmail.requestFocus();
            return false;
        }

        // Validar password
        if (password.isEmpty()) {
            binding.editTextPassword.setError("La contraseña es obligatoria");
            binding.editTextPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            binding.editTextPassword.setError("La contraseña debe tener al menos 6 caracteres");
            binding.editTextPassword.requestFocus();
            return false;
        }

        // Validar confirm password
        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.setError("Confirma tu contraseña");
            binding.editTextConfirmPassword.requestFocus();
            return false;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            binding.editTextConfirmPassword.setError("Las contraseñas no coinciden");
            binding.editTextConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void validatePasswordsMatch() {
        String password = binding.editTextPassword.getText().toString();
        String confirmPassword = binding.editTextConfirmPassword.getText().toString();

        if (!confirmPassword.isEmpty() && !password.equals(confirmPassword)) {
            binding.editTextConfirmPassword.setError("Las contraseñas no coinciden");
        } else {
            binding.editTextConfirmPassword.setError(null);
        }
    }

    private void registerUser(String username, String email, String password, double lat, double lng) {
        RegistroUsuarioDto request = new RegistroUsuarioDto(username, email, password, new BigDecimal(lat), new BigDecimal(lng));

        Call<UsuarioDto> call = apiService.doRegistro(request);
        call.enqueue(new Callback<UsuarioDto>() {
            @Override
            public void onResponse(Call<UsuarioDto> call, Response<UsuarioDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleRegistrationSuccess(response.body());
                } else {
                    handleRegistrationError(response);
                }
            }

            @Override
            public void onFailure(Call<UsuarioDto> call, Throwable t) {
                handleNetworkError(t);
            }
        });
    }

    private void handleRegistrationSuccess(UsuarioDto response) {
        String message = "Registro exitoso";
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Ir a LoginActivity o MainActivity
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

        resetButton();
    }

    private void handleRegistrationError(Response<UsuarioDto> response) {
        String errorMessage = "Error en el registro";

        try {
            if (response.errorBody() != null) {
                String errorBody = response.errorBody().string();
                // Parsear error JSON si es necesario
                JSONObject errorJson = new JSONObject(errorBody);
                errorMessage = errorJson.optString("error", errorMessage);
            }

            switch (response.code()) {
                case 400:
                    errorMessage = "Datos inválidos: " + errorMessage;
                    break;
                case 409:
                    errorMessage = "El usuario ya existe";
                    binding.editTextUsername.setError("Este nombre de usuario ya está en uso");
                    binding.editTextUsername.requestFocus();
                    break;
                case 500:
                    errorMessage = "Error interno del servidor";
                    break;
            }

        } catch (Exception e) {
            errorMessage = "Error de comunicación con el servidor";
        }

        showError(errorMessage);
        resetButton();
    }

    private void handleNetworkError(Throwable t) {
        String errorMessage;

        if (t instanceof SocketTimeoutException) {
            errorMessage = "Tiempo de espera agotado. Inténtalo de nuevo.";
        } else if (t instanceof UnknownHostException) {
            errorMessage = "Error de conexión. Verifica tu internet.";
        } else {
            errorMessage = "Error de red: " + t.getMessage();
        }

        showError(errorMessage);
        resetButton();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void resetButton() {
        binding.registerBtn.setEnabled(true);
        binding.registerBtn.setText("¡Crear cuenta!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

}