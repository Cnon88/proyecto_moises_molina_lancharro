package com.moises.quedadaseventos.dto;

import java.math.BigDecimal;

public class RegistroUsuarioDto {

    private String username;
    private String email;
    private String password;
    private BigDecimal lat;
    private BigDecimal lng;

    public RegistroUsuarioDto() {

    }

    public RegistroUsuarioDto(String username, String email, String password, BigDecimal lat, BigDecimal lng) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.lat = lat;
        this.lng = lng;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "RegistroUsuarioDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
