package com.exo.javabackend.infrastructure.controller;

import com.exo.javabackend.application.CalculateBillingUseCase;
import com.exo.javabackend.domain.model.ClientParticulier;
import com.exo.javabackend.domain.model.ClientPro;
import com.exo.javabackend.domain.model.dto.ClientResponseDTO;
import com.exo.javabackend.infrastructure.controller.dto.ClientParticularRequestDTO;
import com.exo.javabackend.infrastructure.controller.dto.ClientProRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/billing")
public class CalculateBillingController {

    @Autowired
    private final CalculateBillingUseCase calculateBillingUseCase;

    public CalculateBillingController(CalculateBillingUseCase calculateBillingUseCase) {
        this.calculateBillingUseCase = calculateBillingUseCase;
    }

    @PostMapping("/calculate/particular-billing")
    public ResponseEntity<ClientResponseDTO> calculateParticularBilling(@RequestBody ClientParticularRequestDTO clientDto) {
        var client = new ClientParticulier(clientDto.reference(), clientDto.civilite(),clientDto.nom(),
                clientDto.prenom(), clientDto.consommations());

        var result = calculateBillingUseCase.handle(client);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/calculate/pro-billing")
    public ResponseEntity<ClientResponseDTO> calculateProBilling(@RequestBody ClientProRequestDTO clientDto) {
        var client = new ClientPro(clientDto.reference(), clientDto.numSiret(),clientDto.raisonSociale(),
                clientDto.ca(), clientDto.consommations());

        var result = calculateBillingUseCase.handle(client);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
