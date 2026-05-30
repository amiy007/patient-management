package com.amiy.patientservice.service;

import com.amiy.patientservice.dto.PatientRequestDTO;
import com.amiy.patientservice.dto.PatientResponseDTO;
import com.amiy.patientservice.exception.EmailAlreadyExistsException;
import com.amiy.patientservice.exception.PatientNotFoundException;
import com.amiy.patientservice.mapper.PatientMapper;
import com.amiy.patientservice.model.Patient;
import com.amiy.patientservice.repository.PatientRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {

        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
       // List<PatientResponseDTO> patientResponseDTOS = patients.stream().map(patient -> PatientMapper.toPatientResponseDTO(patient)).toList();
        return patients.stream().map(PatientMapper::toPatientResponseDTO).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) throws EmailAlreadyExistsException {
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists "+patientRequestDTO.getEmail());
        }
        Patient patient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

        return PatientMapper.toPatientResponseDTO(patient);

    }
    //public  PatientResponseDTO getPatientById(String patientId){}

    public PatientResponseDTO updatePatient(UUID id,
                                            PatientRequestDTO patientRequestDTO)  {

        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID : "+id));

        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)) {
            throw new EmailAlreadyExistsException("Email already exists "+patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toPatientResponseDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
