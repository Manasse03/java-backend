package com.exo.javabackend;

import com.exo.javabackend.application.CalculateBillingUseCase;
import com.exo.javabackend.domain.exception.BadRequestException;
import com.exo.javabackend.domain.model.Client;
import com.exo.javabackend.domain.model.ClientParticulier;
import com.exo.javabackend.domain.model.ClientPro;
import com.exo.javabackend.domain.model.TypeEnergie;
import com.exo.javabackend.domain.model.dto.ClientResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;


public class CalculateBillingUseCaseTests {
    CalculateBillingUseCase useCase = new CalculateBillingUseCase();

    @Test
    public void testParticularClientAmountElectricAndGas() {
        Map<TypeEnergie, Double> consommationsInitiales = new HashMap<>();
        ClientParticulier client = new ClientParticulier("EKW12345678", "M.", "DUPONT","Jean", consommationsInitiales);
        client.definirConsommation(TypeEnergie.ELECTRICITE, 350.0);
        client.definirConsommation(TypeEnergie.GAZ, 150.0);

        ClientResponseDTO result = useCase.handle(client);

        Assertions.assertEquals(client.getNom(), result.nom().get());
        Assertions.assertEquals(59.6, result.sommeTotal());
        Assertions.assertEquals(42.35, result.montantParEnergie().get(TypeEnergie.ELECTRICITE));
        Assertions.assertEquals(17.25, result.montantParEnergie().get(TypeEnergie.GAZ));


    }

    @Test
    public void testParticularClientAmountElectric() {
        Map<TypeEnergie, Double> consommationsInitiales = new HashMap<>();
        ClientParticulier client = new ClientParticulier("EKW12345678", "M.", "DUPONT","Jean", consommationsInitiales);
        client.definirConsommation(TypeEnergie.ELECTRICITE, 50.0);

        ClientResponseDTO result = useCase.handle(client);
        Assertions.assertEquals(6.05, result.sommeTotal());
        Assertions.assertEquals(6.05, result.montantParEnergie().get(TypeEnergie.ELECTRICITE));
    }


    @Test
    public void testParticularClientAmountGas() {
        Map<TypeEnergie, Double> consommationsInitiales = new HashMap<>();
        Client client = new ClientParticulier("EKW12345678", "M.", "DUPONT","Jean", consommationsInitiales);
        client.definirConsommation(TypeEnergie.GAZ, 150.0);

        ClientResponseDTO result = useCase.handle(client);
        Assertions.assertEquals(17.25, result.sommeTotal());
        Assertions.assertEquals(17.25, result.montantParEnergie().get(TypeEnergie.GAZ));

    }

    @Test
    public void testProClientAmountElectricAndGasLess1M() {
        Map<TypeEnergie, Double> consommationsInitiales = new HashMap<>();
        ClientPro client = new ClientPro("EKW12345678", "92100000", "TEST.SASA",
                9_999_99.0, consommationsInitiales);
        client.definirConsommation(TypeEnergie.ELECTRICITE, 350.0);
        client.definirConsommation(TypeEnergie.GAZ, 150.0);

        ClientResponseDTO result = useCase.handle(client);
        Assertions.assertEquals(client.getNumSiret(), result.numSiret().get());
        Assertions.assertEquals(58.250, result.sommeTotal());
        Assertions.assertEquals(16.95, result.montantParEnergie().get(TypeEnergie.GAZ));
        Assertions.assertEquals(41.3, result.montantParEnergie().get(TypeEnergie.ELECTRICITE));

    }

   @Test
    public void testProClientAmountElectricAndGasGraMore1M() {
        Map<TypeEnergie, Double> consommationsInitiales = new HashMap<>();
        Client client = new ClientPro("EKW12345678", "92100000", "TEST.SASA",
                1_000_001.0, consommationsInitiales);
        client.definirConsommation(TypeEnergie.ELECTRICITE, 350.0);
        client.definirConsommation(TypeEnergie.GAZ, 150.0);

       ClientResponseDTO result = useCase.handle(client);

       Assertions.assertEquals(56.55, result.sommeTotal());
       Assertions.assertEquals(16.65, result.montantParEnergie().get(TypeEnergie.GAZ));
       Assertions.assertEquals(39.9, result.montantParEnergie().get(TypeEnergie.ELECTRICITE));

    }

    @Test
    public void whenReferenceIsNotValidFormat_shouldThrow() {
        Map<TypeEnergie, Double> consommationsInitiales = new HashMap<>();
        ClientParticulier client = new ClientParticulier("EKW1234", "M.", "DUPONT","Jean", consommationsInitiales);

        var expection = Assertions.assertThrows(BadRequestException.class, () -> {
            useCase.handle(client);
        });

        Assertions.assertEquals("La référence du client est invalide.", expection.getMessage());
    }
}
