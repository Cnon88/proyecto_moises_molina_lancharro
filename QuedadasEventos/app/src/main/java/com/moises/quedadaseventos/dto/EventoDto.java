package com.moises.quedadaseventos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class EventoDto {

    private Integer id;
    private String titulo;
    private String descripcion;
    private JuegoDto juego;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private UsuarioDto creador;
    private Integer minPersonas;
    private Integer maxPersonas;
    private String fechaInicioInscripciones;
    private String fechaFinInscripciones;
    private String fechaEvento;
    private EstadoEvento estado;
    private Set<UsuarioDto> participantes;

    public enum EstadoEvento {
        PENDIENTE,
        LLENO,
        FINALIZADO
    }

    public EventoDto() {

    }

    public EventoDto(Integer id, String titulo, String descripcion, JuegoDto juego, BigDecimal latitud, BigDecimal longitud, UsuarioDto creador, Integer minPersonas, Integer maxPersonas, String fechaInicioInscripciones, String fechaFinInscripciones, String fechaEvento, EstadoEvento estado, Set<UsuarioDto> participantes) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.juego = juego;
        this.latitud = latitud;
        this.longitud = longitud;
        this.creador = creador;
        this.minPersonas = minPersonas;
        this.maxPersonas = maxPersonas;
        this.fechaInicioInscripciones = fechaInicioInscripciones;
        this.fechaFinInscripciones = fechaFinInscripciones;
        this.fechaEvento = fechaEvento;
        this.estado = estado;
        this.participantes = participantes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public JuegoDto getJuego() {
        return juego;
    }

    public void setJuego(JuegoDto juego) {
        this.juego = juego;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public UsuarioDto getCreador() {
        return creador;
    }

    public void setCreador(UsuarioDto creador) {
        this.creador = creador;
    }

    public Integer getMinPersonas() {
        return minPersonas;
    }

    public void setMinPersonas(Integer minPersonas) {
        this.minPersonas = minPersonas;
    }

    public Integer getMaxPersonas() {
        return maxPersonas;
    }

    public void setMaxPersonas(Integer maxPersonas) {
        this.maxPersonas = maxPersonas;
    }

    public String getFechaInicioInscripciones() {
        return fechaInicioInscripciones;
    }

    public void setFechaInicioInscripciones(String fechaInicioInscripciones) {
        this.fechaInicioInscripciones = fechaInicioInscripciones;
    }

    public String getFechaFinInscripciones() {
        return fechaFinInscripciones;
    }

    public void setFechaFinInscripciones(String fechaFinInscripciones) {
        this.fechaFinInscripciones = fechaFinInscripciones;
    }

    public String getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(String fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }

    public Set<UsuarioDto> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(Set<UsuarioDto> participantes) {
        this.participantes = participantes;
    }

    @Override
    public String toString() {
        return "EventoDto{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", juego=" + juego +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", creador=" + creador +
                ", minPersonas=" + minPersonas +
                ", maxPersonas=" + maxPersonas +
                ", fechaInicioInscripciones=" + fechaInicioInscripciones +
                ", fechaFinInscripciones=" + fechaFinInscripciones +
                ", fechaEvento=" + fechaEvento +
                ", estado=" + estado +
                ", participantes=" + participantes +
                '}';
    }
}
