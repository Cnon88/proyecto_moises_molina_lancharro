package com.moises.quedadaseventos.activitiy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.moises.quedadaseventos.R;
import com.moises.quedadaseventos.databinding.ActivityLoginBinding;
import com.moises.quedadaseventos.dto.LoginDto;
import com.moises.quedadaseventos.dto.LoginResponseDto;
import com.moises.quedadaseventos.retrofit.ApiClient;
import com.moises.quedadaseventos.retrofit.ApiService;
import com.moises.quedadaseventos.utils.TokenManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializar TokenManager antes que nada
        tokenManager = new TokenManager(getApplicationContext());
        if (redirectToEventosIfValidToken()) return; // Importante: retornar para evitar cargar la UI innecesariamente

        initViews();
        initRetrofit();
        setupListeners();
    }

    private void setupListeners() {
        // Configurar botón de login
        binding.startBtn.setOnClickListener(this::handleLoginClick);

        binding.textViewRegistrarse.setOnClickListener(this::handleCrearCuentaClick);
    }


    private void initRetrofit() {
        // Configurar Retrofit sin interceptor para el login
        Retrofit clientWithoutInterceptor = ApiClient.getClientWithoutInterceptor(getApplicationContext());
        apiService = clientWithoutInterceptor.create(ApiService.class);
    }

    private void initViews() {
        // Si no hay token o está expirado, entonces mostrar la pantalla de login
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private boolean redirectToEventosIfValidToken() {
        // Verificar si hay un token válido antes de inflar la vista
        if (tokenManager.hasToken() && !tokenManager.isTokenExpired()) {
            // Si hay token válido, ir directamente a EventosActivity
            navigateToEventos();
            return true;
        }
        return false;
    }

    private void handleCrearCuentaClick(View view) {
        Intent intent = new Intent(getApplicationContext(), RegistroActivity.class);
        startActivity(intent);
    }

    private void handleLoginClick(View view) {
        String user = binding.editTextEmailLogin.getText().toString().trim();
        String pass = binding.editTextPassword.getText().toString().trim();
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, R.string.completa_usuario_y_contrasena, Toast.LENGTH_SHORT).show();
        } else {
            binding.startBtn.setEnabled(false);
            LoginDto dtoLogin = new LoginDto(user, pass);

            apiService.doLogin(dtoLogin).enqueue(new Callback<LoginResponseDto>() {
                @Override
                public void onResponse(Call<LoginResponseDto> call, Response<LoginResponseDto> response) {
                    binding.startBtn.setEnabled(true);
                    if (response.isSuccessful() && response.body() != null) {
                        handleLoginSuccessfulResponse(response);
                    } else {
                        handleLoginErrorResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<LoginResponseDto> call, Throwable t) {
                    binding.startBtn.setEnabled(true);
                    handleLoginFailure(t);
                }
            });
        }
    }

    private void handleLoginFailure(Throwable t) {
        // Manejar error de red o de conversión
        Toast.makeText(getApplicationContext(), getString(R.string.error_de_conexion) + t.getMessage(), Toast.LENGTH_LONG).show();
    }

    private void handleLoginErrorResponse(Response<LoginResponseDto> response) {
        try {
            String errorMsg = response.errorBody() != null ? response.errorBody().string() : getString(R.string.error_de_autenticacion);
            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_de_autenticacion), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLoginSuccessfulResponse(Response<LoginResponseDto> response) {
        String token = response.body().getToken();
        Long expiresIn = response.body().getExpiresIn();
        // Guardar token
        tokenManager.saveToken(token, expiresIn);

        // Navegar a EventosActivity
        navigateToEventos();
    }

    private void navigateToEventos() {
        Intent intent = new Intent(getApplicationContext(), EventosActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
