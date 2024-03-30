package com.exo.javabackend.infrastructure.controller.dto;


import com.exo.javabackend.domain.model.TypeEnergie;

import java.util.Map;

public record ClientParticularRequestDTO(
        String reference,
        String civilite,
        String nom,
        String prenom,
        Map<TypeEnergie, Double> consommations
) {}