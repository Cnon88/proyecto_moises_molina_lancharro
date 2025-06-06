package com.moises.quedadaseventos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CrearEventoDto {

    private String titulo;
    private String descripcion;
    private Integer juegoId;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private Integer minPersonas;
    private Integer maxPersonas;
    private String fechaInicioInscripciones;
    private String fechaFinInscripciones;
    private String fechaEvento;

    public CrearEventoDto() {}

    public CrearEventoDto(String titulo, String descripcion, Integer juegoId, BigDecimal latitud, BigDecimal longitud, Integer minPersonas, Integer maxPersonas, String fechaInicioInscripciones, String fechaFinInscripciones, String fechaEvento) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.juegoId = juegoId;
        this.latitud = latitud;
        this.longitud = longitud;
        this.minPersonas = minPersonas;
        this.maxPersonas = maxPersonas;
        this.fechaInicioInscripciones = fechaInicioInscripciones;
        this.fechaFinInscripciones = fechaFinInscripciones;
        this.fechaEvento = fechaEvento;
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

    public Integer getJuegoId() {
        return juegoId;
    }

    public void setJuegoId(Integer juegoId) {
        this.juegoId = juegoId;
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
}
