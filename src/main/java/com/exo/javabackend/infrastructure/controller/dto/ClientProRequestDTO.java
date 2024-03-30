package com.exo.javabackend.infrastructure.controller.dto;


import com.exo.javabackend.domain.model.TypeEnergie;

import java.util.Map;

public record ClientProRequestDTO(
        String reference,
        String numSiret,
        String raisonSociale,
        Double ca,
        Map<TypeEnergie, Double> consommations
) {}