package com.exo.javabackend.domain.model;

import java.util.Map;

public abstract class Client {

    protected String reference;
    protected Map<TypeEnergie, Double> consommations;

    public Client(String reference, Map<TypeEnergie, Double> consommations) {
        this.reference = reference;
        this.consommations = consommations;
    }

    public void definirConsommation(TypeEnergie typeEnergie, Double consommation) {
        consommations.put(typeEnergie, consommation);
    }

    public Map<TypeEnergie, Double> getConsommations() {
        return consommations;
    }

    public String getReference() {
        return reference;
    }

    public abstract double getTarif(TypeEnergie typeEnergie);

}

