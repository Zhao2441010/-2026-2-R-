package com.example.demo.service;


import com.example.demo.entity.Cat;
import com.example.demo.repository.CatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CatServiceImpl implements CatService {

    private final CatRepository catRepository;
    public CatServiceImpl(CatRepository catRepository) {
        this.catRepository = catRepository;
    }


    @Transactional
    @Override
    public void saveCat(String name,String image, int age, String type, String health, Boolean jueyu) {
        catRepository.save(new Cat(name, image,age, type, health, jueyu));
    }

    @Transactional
    @Override
    public void deleteCatById(Long id) {
        catRepository.deleteById(id);
    }

    @Override
    public Cat getCatById(Long id) {
        return catRepository.getById(id);
    }

    @Override
    public List<Cat> getTop10(){
        List<Cat>all=catRepository.findAll();
        List<Cat> anss=new ArrayList<Cat>();

        for(int i=0;i<all.size();i++){
            if(i<10)anss.add(all.get(i));
             else break;
        }
        return anss;
    }

    @Override
    public List<Cat> getAllCat() {
        return catRepository.findAll();
    }


    @Override
    public Long countAll(){
        return catRepository.countAll();
    }


}
