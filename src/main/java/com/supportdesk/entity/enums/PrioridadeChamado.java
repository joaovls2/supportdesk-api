package com.supportdesk.entity.enums;

public enum PrioridadeChamado {

    BAIXA(48),
    MEDIA(24),
    ALTA(8),
    CRITICA(4);

    private final int horasSla;

    PrioridadeChamado(int horasSla) {
        this.horasSla = horasSla;
    }

    public int getHorasSla() {
        return horasSla;
    }
}