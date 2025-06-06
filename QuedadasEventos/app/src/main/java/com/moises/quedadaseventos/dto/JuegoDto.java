package com.moises.quedadaseventos.dto;

public class JuegoDto {

    private int id;
    private String nombre;
    private int minJugadores;
    private int maxJugadores;
    private int tiempoEstimadoEnMinutos;

    public JuegoDto() {}

    public JuegoDto(int id, String nombre, int minJugadores, int maxJugadores, int tiempoEstimadoEnMinutos) {
        this.id = id;
        this.nombre = nombre;
        this.minJugadores = minJugadores;
        this.maxJugadores = maxJugadores;
        this.tiempoEstimadoEnMinutos = tiempoEstimadoEnMinutos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getMinJugadores() {
        return minJugadores;
    }

    public void setMinJugadores(int minJugadores) {
        this.minJugadores = minJugadores;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }

    public void setMaxJugadores(int maxJugadores) {
        this.maxJugadores = maxJugadores;
    }

    public int getTiempoEstimadoEnMinutos() {
        return tiempoEstimadoEnMinutos;
    }

    public void setTiempoEstimadoEnMinutos(int tiempoEstimadoEnMinutos) {
        this.tiempoEstimadoEnMinutos = tiempoEstimadoEnMinutos;
    }

    @Override
    public String toString() {
        return "JuegoDto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", minJugadores=" + minJugadores +
                ", maxJugadores=" + maxJugadores +
                ", tiempoEstimadoEnMinutos=" + tiempoEstimadoEnMinutos +
                '}';
    }
}
