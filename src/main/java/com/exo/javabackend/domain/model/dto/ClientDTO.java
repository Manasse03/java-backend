package com.exo.javabackend.domain.model.dto;

import com.exo.javabackend.domain.model.TypeEnergie;

import java.util.Map;
import java.util.Optional;

public record ClientDTO(
        String reference,
        Map<TypeEnergie, Double> montantParEnergie,
        double sommeTotal,
        Optional<String> civilite,
        Optional<String> nom,
        Optional<String> prenom,
        Optional<String> numSiret,
        Optional<String> raisonSociale,
        Optional<Double> ca) {

    //constructor for ClientParticulier
    public ClientDTO(String reference, String civilite, String nom, String prenom, Map<TypeEnergie,
            Double> montantParEnergie, double sommeTotal) {
        this(reference, montantParEnergie, sommeTotal, Optional.of(civilite), Optional.of(nom), Optional.of(prenom),
                Optional.empty(), Optional.empty(), Optional.empty());
    }

    //constructor for ClientPro
    public ClientDTO(String reference, String numSiret, String raisonSociale,
                     Map<TypeEnergie, Double> montantParEnergie, double sommeTotal) {
        this(reference, montantParEnergie, sommeTotal, Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(numSiret), Optional.of(raisonSociale), Optional.empty());
    }


}
