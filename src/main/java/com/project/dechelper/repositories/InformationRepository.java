package com.project.dechelper.repositories;

import com.project.dechelper.model.Information;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface InformationRepository extends JpaRepository<Information, Long> {
    List<Information> findBySubject(String subject);

    @Modifying
    @Query("delete from Information i where i.id=:id")
    void deleteById(Long id);

    @Transactional
    void deleteBySubject(String subject);
}
