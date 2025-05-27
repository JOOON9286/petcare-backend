package com.example.vet_backend.repository;

import com.example.vet_backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByVet_VetId(Long vetId);

    List<Appointment> findByUser_UserId(Long userId);

}
