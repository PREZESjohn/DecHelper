package com.project.dechelper.services;

import com.project.dechelper.model.Information;
import com.project.dechelper.repositories.InformationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InformationServiceImpl implements InformationService {
    private final InformationRepository informationRepository;

    @Override
    public List<Information> getAllInfo() {
        return informationRepository.findAll();
    }

    @Override
    public List<Information> getAllInfoBySubject(String subject) {
        return informationRepository.findBySubject(subject);
    }

    @Override
    public Information saveInfo(Information information) {
        return informationRepository.save(information);
    }

    @Override
    public void deleteInfoById(Long id) {
        informationRepository.deleteById(id);
    }

    @Override
    public void deleteAllInfoBySubject(String subject) {
        informationRepository.deleteBySubject(subject);
    }
}
