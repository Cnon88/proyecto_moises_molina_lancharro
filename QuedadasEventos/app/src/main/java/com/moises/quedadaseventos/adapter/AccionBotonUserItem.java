package com.moises.quedadaseventos.adapter;

public interface AccionBotonUserItem {
    enum TipoAccion { SIN_ACCION, SEGUIR, DEJAR_DE_SEGUIR }

    void onAccionRealizada();

    TipoAccion getTipoAccion();
}

