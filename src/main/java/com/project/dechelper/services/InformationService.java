package com.project.dechelper.services;

import com.project.dechelper.model.Information;

import java.util.List;


public interface InformationService {

    List<Information> getAllInfo();
    List<Information> getAllInfoBySubject(String subject);

    Information saveInfo(Information information);
    void deleteInfoById(Long id);
    void deleteAllInfoBySubject(String subject);

}
