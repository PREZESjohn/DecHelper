package com.project.dechelper.repository;

import com.project.dechelper.model.Information;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface InformationRepository extends JpaRepository<Information, Long> {
    List<Information> findBySubject(String subject);
    void deleteById(Long id);
    void deleteBySubject(String subject);
}
