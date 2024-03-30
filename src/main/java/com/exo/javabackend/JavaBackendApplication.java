package com.exo.javabackend;

import com.exo.javabackend.application.CalculateBillingUseCase;
import com.exo.javabackend.domain.model.ClientParticulier;
import com.exo.javabackend.domain.model.ClientPro;
import com.exo.javabackend.domain.model.TypeEnergie;
import com.exo.javabackend.domain.model.dto.ClientResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class JavaBackendApplication implements CommandLineRunner {

    @Autowired
    private CalculateBillingUseCase calculateBillingUseCase;

    public static void main(String[] args) {
        SpringApplication.run(JavaBackendApplication.class, args);


    }

    @Override
    public void run(String... args) throws Exception {
        //Particular Client
        Map<TypeEnergie, Double> consommationsInitiales = new HashMap<>();
        ClientParticulier client = new ClientParticulier("EKW12345678", "M.", "DUPONT","Jean", consommationsInitiales);
        client.definirConsommation(TypeEnergie.ELECTRICITE, 350.0);
        client.definirConsommation(TypeEnergie.GAZ, 150.0);

        var dto = calculateBillingUseCase.handle(client);

        System.out.println(toStringParticulier(dto));


        //Pro Client
        Map<TypeEnergie, Double> consommationsInitialesPro = new HashMap<>();
        ClientPro pro = new ClientPro("EKW12345679", "92100000", "TEST.SASA",
                1_000_001.0, consommationsInitialesPro);
        pro.definirConsommation(TypeEnergie.ELECTRICITE, 350.0);
        pro.definirConsommation(TypeEnergie.GAZ, 150.0);

        ClientResponseDTO result = calculateBillingUseCase.handle(pro);

        System.out.println(toStringPro(result));

    }

    private String toStringParticulier(ClientResponseDTO dto) {
        return "ClientDTO{" +
                "reference='" + dto.reference() + '\'' +
                ", montantParEnergie=" + dto.montantParEnergie() +
                ", sommeTotal=" + dto.sommeTotal() +
                ", civilite=" + dto.civilite() +
                ", nom=" + dto.nom() +
                ", prenom=" + dto.prenom() +
                '}';
    }

    private String toStringPro(ClientResponseDTO dto) {
        return "ClientDTO-PRO{" +
                "reference='" + dto.reference() + '\'' +
                ", montantParEnergie=" + dto.montantParEnergie() +
                ", sommeTotal=" + dto.sommeTotal() +
                ", siret=" + dto.numSiret() +
                ", raison social=" + dto.raisonSociale() +
                '}';
    }
}
