package com.moises.quedadaseventos.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Objects;

public class UsuarioDto implements Parcelable {

    private int id;
    private String nickname;
    private String email;
    private BigDecimal latitudHogar;
    private BigDecimal longitudHogar;

    public UsuarioDto() {
    }

    public UsuarioDto(int id, String nickname, String email, BigDecimal latitudHogar, BigDecimal longitudHogar) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.latitudHogar = latitudHogar;
        this.longitudHogar = longitudHogar;
    }

    protected UsuarioDto(Parcel in) {
        this.id = in.readInt();
        this.nickname = in.readString();
        this.email = in.readString();
        this.latitudHogar = new BigDecimal(in.readString());
        this.longitudHogar = new BigDecimal(in.readString());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getLatitudHogar() {
        return latitudHogar;
    }

    public void setLatitudHogar(BigDecimal latitudHogar) {
        this.latitudHogar = latitudHogar;
    }

    public BigDecimal getLongitudHogar() {
        return longitudHogar;
    }

    public void setLongitudHogar(BigDecimal longitudHogar) {
        this.longitudHogar = longitudHogar;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioDto that = (UsuarioDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    public static final Creator<UsuarioDto> CREATOR = new Creator<UsuarioDto>() {
        @Override
        public UsuarioDto createFromParcel(Parcel in) {
            return new UsuarioDto(in);
        }

        @Override
        public UsuarioDto[] newArray(int size) {
            return new UsuarioDto[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nickname);
        dest.writeString(email);
        dest.writeString(latitudHogar != null ? latitudHogar.toString() : "0");
        dest.writeString(longitudHogar != null ? longitudHogar.toString() : "0");
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
