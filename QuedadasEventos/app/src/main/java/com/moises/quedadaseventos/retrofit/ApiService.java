package com.moises.quedadaseventos.retrofit;

import com.moises.quedadaseventos.dto.CrearEventoDto;
import com.moises.quedadaseventos.dto.EventoDto;
import com.moises.quedadaseventos.dto.JuegoDto;
import com.moises.quedadaseventos.dto.LoginDto;
import com.moises.quedadaseventos.dto.LoginResponseDto;
import com.moises.quedadaseventos.dto.RegistroUsuarioDto;
import com.moises.quedadaseventos.dto.UsuarioDto;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("autenticacion/login")
    Call<LoginResponseDto> doLogin(@Body LoginDto loginDto);
    @POST("autenticacion/registro")
    Call<UsuarioDto> doRegistro(@Body RegistroUsuarioDto registroUsuarioDto);


    @GET("juegos")
    Call<List<JuegoDto>> getJuegos();


    @POST("eventos")
    Call<EventoDto> crearEvento(@Body CrearEventoDto nuevoEvento);
    @GET("eventos")
    Call<List<EventoDto>> getEventos();
    @GET("eventos/{id}")
    Call<EventoDto> getDetalleEvento(@Path("id") int eventId);





    @GET("usuarios")
    Call<Set<UsuarioDto>> getUsuarios();
    @GET("usuarios/{id}/siguiendo")
    Call<Set<UsuarioDto>> getUsuariosSiguiendo(@Path("id") int userId);
    @GET("usuarios/{id}/seguidores")
    Call<Set<UsuarioDto>> getUsuariosSeguidores(@Path("id") int userId);





    @GET("perfil")
    Call<UsuarioDto> getPerfil();
    @POST("perfil/seguir/{idSeguido}")
    Call<Void> seguirUsuario(@Path("idSeguido") int idSeguido);
    @DELETE("perfil/dejar-de-seguir/{idSeguido}")
    Call<Void> dejarDeSeguirUsuario(@Path("idSeguido") int idSeguido);
    @GET("perfil/eventos/participando")
    Call<Set<EventoDto>> obtenerEventosParticipando();
    @GET("perfil/eventos/creados")
    Call<Set<EventoDto>> obtenerEventosCreados();
    @POST("perfil/eventos/inscribirse/{idEvento}")
    Call<Void> inscribirseEnEvento(@Path("idEvento") int idEvento);
    @DELETE("perfil/eventos/desinscribirse/{idEvento}")
    Call<Void> desinscribirseDeEvento(@Path("idEvento") int idEvento);
    @DELETE("perfil/eventos/eliminar/{idEvento}")
    Call<Void> eliminarEvento(@Path("idEvento") int idEvento);

}
