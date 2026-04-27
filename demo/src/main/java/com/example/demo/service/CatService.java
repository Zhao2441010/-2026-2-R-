package com.example.demo.service;


import com.example.demo.entity.Cat;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CatService{

    void saveCat(String name,String image, int age, String type, String health, Boolean jueyu);

    void deleteCatById(Long id);

    Cat getCatById(Long id);

    List<Cat> getTop10();

    List<Cat> getAllCat();

}
