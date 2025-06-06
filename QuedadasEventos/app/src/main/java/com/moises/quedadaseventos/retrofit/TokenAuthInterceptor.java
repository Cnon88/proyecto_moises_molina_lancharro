package com.moises.quedadaseventos.retrofit;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.moises.quedadaseventos.activitiy.LoginActivity;
import com.moises.quedadaseventos.utils.TokenManager;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenAuthInterceptor implements Interceptor {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer ";
    private final Context context;
    private final TokenManager tokenManager;

    public TokenAuthInterceptor(Context context) {
        this.context = context.getApplicationContext();
        this.tokenManager = new TokenManager(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Si la petición ya tiene Authorization header, la dejamos pasar sin modificar
        if (originalRequest.header(AUTHORIZATION) != null) {
            return chain.proceed(originalRequest);
        }

        // Verificar si tenemos un token
        if (!tokenManager.hasToken()) {
            redirectToLogin();
            return chain.proceed(originalRequest);
        }

        // Verificar si el token ha expirado
        if (tokenManager.isTokenExpired()) {
            redirectToLogin();
            return chain.proceed(originalRequest);
        }

        // Agregar el token a la petición
        String token = tokenManager.getToken();
        Request authenticatedRequest = originalRequest.newBuilder()
                .header(AUTHORIZATION, BEARER + token)
                .build();

        return chain.proceed(authenticatedRequest);
    }

    private void redirectToLogin() {
        // Redirigir al Splash en el hilo principal
        new Handler(Looper.getMainLooper()).post(() -> {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        });
    }
}
