package com.exo.javabackend.domain.model;

import java.util.Map;

public class ClientPro extends Client {
    private String numSiret;
    private String raisonSociale;
    private Double ca;

    public ClientPro(String reference, String numSiret, String raisonSociale, Double ca, Map<TypeEnergie, Double> consommations) {
        super(reference, consommations);
        this.numSiret = numSiret;
        this.raisonSociale = raisonSociale;
        this.ca = ca;
    }

    public String getNumSiret() {
        return numSiret;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }


    @Override
    public double getTarif(TypeEnergie typeEnergie) {
        double tarif;
        if (ca > 1_000_000) {
            tarif = typeEnergie == TypeEnergie.ELECTRICITE ? 0.114 : 0.111;
        } else {
            tarif = typeEnergie == TypeEnergie.ELECTRICITE ? 0.118 : 0.113;
        }
        return tarif;
    }
}

