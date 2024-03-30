package com.exo.javabackend.application;

import com.exo.javabackend.domain.exception.BadRequestException;
import com.exo.javabackend.domain.model.Client;
import com.exo.javabackend.domain.model.ClientParticulier;
import com.exo.javabackend.domain.model.ClientPro;
import com.exo.javabackend.domain.model.TypeEnergie;
import com.exo.javabackend.domain.model.dto.ClientResponseDTO;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class CalculateBillingUseCase {
    private final Pattern REFERENCE_PATTERN =  Pattern.compile("^EKW\\d{8}$");
    public CalculateBillingUseCase() {
    }

    public ClientResponseDTO handle(Client client) {
        if (!validerReference(client.getReference())) {
            throw new BadRequestException("La référence du client est invalide.");
        }

        Map<TypeEnergie, Double> montantParEnergie = new EnumMap<>(TypeEnergie.class);
        double sommeTotal = client.getConsommations().entrySet().stream()
                .mapToDouble(consommation -> {
                    double montant = consommation.getValue() * client.getTarif(consommation.getKey());
                    montantParEnergie.put(consommation.getKey(), montant);
                    return montant;
                }).sum();

        if (client instanceof ClientParticulier particulier) {
            return new ClientResponseDTO(client.getReference(), particulier.getCivilite(), particulier.getNom(), particulier.getPrenom(), montantParEnergie, sommeTotal);
        } else {
            ClientPro pro = (ClientPro) client;
            return new ClientResponseDTO(client.getReference(), pro.getNumSiret(), pro.getRaisonSociale(), montantParEnergie, sommeTotal);
        }
    }

    private  boolean validerReference(String reference) {
        return REFERENCE_PATTERN.matcher(reference).matches();
    }
}

