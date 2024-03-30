package com.exo.javabackend.domain.model;

import java.util.Map;

public class ClientParticulier extends Client {
    private String civilite;
    private String nom;
    private String prenom;

    public ClientParticulier(String reference, String civilite,String nom, String prenom,
                             Map<TypeEnergie, Double> consommations) {
        super(reference, consommations);
        this.civilite = civilite;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getCivilite() {
        return civilite;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public double getTarif(TypeEnergie typeEnergie) {
        return typeEnergie == TypeEnergie.ELECTRICITE ? 0.121 : 0.115;
    }
}

