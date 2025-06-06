package com.moises.quedadaseventos.adapter;

public interface AccionBotonEventoItem {

    enum TipoAccionEvento { INSCRIBIRSE, DESINSCRIBIRSE, ELIMINAR };
    void realizarAccion(int eventoId);
    TipoAccionEvento getTipoAccion();
}
