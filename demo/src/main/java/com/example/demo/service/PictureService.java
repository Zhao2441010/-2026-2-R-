package com.example.demo.service;

import com.example.demo.entity.Picture;

import java.util.List;

public interface PictureService {
    void addPicture(Long cid,String url);

    List<Picture>findByCatId(Long cid);

    List<Picture>selectDisplayPictureByCatid(Long cid);

    void setDisplaySituationById(Long pid,Boolean display);

    void deletePictureById(Long id);

    void addPicture(Long cid, String url, Long uploaderId);

}
