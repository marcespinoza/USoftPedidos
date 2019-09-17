package com.usoft.pedidos.Dom;

public class Cliente {

    public Cliente() {
    }

    String nomcli;
    String domcli;
    String codcli;
    String persona;
    String nrocuenta;

    public String getNrocuenta() {
        return nrocuenta;
    }

    public void setNrocuenta(String nrocuenta) {
        this.nrocuenta = nrocuenta;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public String getNomcli() {
        return nomcli;
    }

    public void setNomcli(String nomcli) {
        this.nomcli = nomcli;
    }

    public String getDomcli() {
        return domcli;
    }

    public void setDomcli(String domcli) {
        this.domcli = domcli;
    }

    public String getCodcli() {
        return codcli;
    }

    public void setCodcli(String codcli) {
        this.codcli = codcli;
    }
}
