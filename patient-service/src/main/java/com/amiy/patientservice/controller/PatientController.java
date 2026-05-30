package com.amiy.patientservice.controller;

import com.amiy.patientservice.dto.PatientRequestDTO;
import com.amiy.patientservice.dto.PatientResponseDTO;
import com.amiy.patientservice.dto.validators.CreatePatientValidationGroup;
import com.amiy.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@Tag(name="Patient", description="API for managing Patients")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {

        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
        // return ResponseEntity.ok(patients);
    }


    @PostMapping
    @Operation(summary = "Create Patients")
    public ResponseEntity<PatientResponseDTO> savePatient(@Validated({Default.class, CreatePatientValidationGroup.class     }) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Patients")
    public ResponseEntity<PatientResponseDTO>updatePatient(@Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO,
                                                           @PathVariable UUID id) {
        PatientResponseDTO patientResponseDTO= patientService.updatePatient(id,patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Patients")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
