package com.exo.javabackend;

import com.exo.javabackend.domain.model.ClientParticulier;
import com.exo.javabackend.domain.model.ClientPro;
import com.exo.javabackend.domain.model.TypeEnergie;
import com.exo.javabackend.domain.model.dto.ClientResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class CalculateBillingUseCaseE2ETests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public  void ShouldCalculateClientParticular() throws  Exception{
        Map<TypeEnergie, Double> consommationsInitiales = new HashMap<>();
        ClientParticulier client = new ClientParticulier("EKW12345678", "M.", "DUPONT","Jean", consommationsInitiales);
        client.definirConsommation(TypeEnergie.ELECTRICITE, 350.0);
        client.definirConsommation(TypeEnergie.GAZ, 150.0);

        System.out.println(objectMapper.writeValueAsString(client));

        var result = mockMvc.perform(MockMvcRequestBuilders.post("/billing/calculate/particular-billing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        var clientResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ClientResponseDTO.class
        );

        Assertions.assertEquals(59.6, clientResponse.sommeTotal());
        Assertions.assertEquals(42.35, clientResponse.montantParEnergie().get(TypeEnergie.ELECTRICITE));
        Assertions.assertEquals(17.25, clientResponse.montantParEnergie().get(TypeEnergie.GAZ));
    }

    @Test
    public  void ShouldCalculateClientPro() throws  Exception{
        Map<TypeEnergie, Double> consommationsInitiales = new HashMap<>();
        ClientPro pro = new ClientPro("EKW12345678", "92100000", "TEST.SASA",
                9_999_9.00, consommationsInitiales);
        pro.definirConsommation(TypeEnergie.ELECTRICITE, 350.0);
        pro.definirConsommation(TypeEnergie.GAZ, 150.0);


        var result = mockMvc.perform(MockMvcRequestBuilders.post("/billing/calculate/pro-billing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pro)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        var clientResponse = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ClientResponseDTO.class
        );

        Assertions.assertEquals(58.250, clientResponse.sommeTotal());
        Assertions.assertEquals(16.95, clientResponse.montantParEnergie().get(TypeEnergie.GAZ));
        Assertions.assertEquals(41.3, clientResponse.montantParEnergie().get(TypeEnergie.ELECTRICITE));
    }

    @Test
    public  void ShouldFailIfRefenceIsNotValid() throws  Exception{
        Map<TypeEnergie, Double> consommationsInitiales = new HashMap<>();
        ClientPro pro = new ClientPro("EKW1234", "92100000", "TEST.SASA",
                9_999_9.000, consommationsInitiales);


        mockMvc.perform(MockMvcRequestBuilders.post("/billing/calculate/pro-billing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pro)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }
}
