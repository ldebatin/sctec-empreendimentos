package com.sctec.empreendimentos.controller;

import com.sctec.empreendimentos.dto.EmpreendimentoRequest;
import com.sctec.empreendimentos.dto.EmpreendimentoResponse;
import com.sctec.empreendimentos.service.EmpreendimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empreendimentos")
@RequiredArgsConstructor
@Tag(name = "Empreendimentos", description = "Gerenciamento de empreendimentos de Santa Catarina")
public class EmpreendimentoController {

    private final EmpreendimentoService service;

    @GetMapping
    @Operation(summary = "Listar todos os empreendimentos")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<List<EmpreendimentoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empreendimento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empreendimento encontrado"),
        @ApiResponse(responseCode = "404", description = "Empreendimento não encontrado")
    })
    public ResponseEntity<EmpreendimentoResponse> findById(
            @Parameter(description = "ID do empreendimento") @PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar novo empreendimento")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Empreendimento criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<EmpreendimentoResponse> create(@RequestBody @Valid EmpreendimentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar empreendimento existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Empreendimento atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Empreendimento não encontrado")
    })
    public ResponseEntity<EmpreendimentoResponse> update(
            @Parameter(description = "ID do empreendimento") @PathVariable Integer id,
            @RequestBody @Valid EmpreendimentoRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover empreendimento")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Empreendimento removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Empreendimento não encontrado")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do empreendimento") @PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
