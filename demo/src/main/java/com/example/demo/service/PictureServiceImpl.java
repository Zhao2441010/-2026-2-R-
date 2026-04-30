package com.example.demo.service;

import com.example.demo.entity.Picture;
import com.example.demo.repository.PictureRepository;
import com.example.demo.service.PictureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {

    private final PictureRepository pictureRepository;
    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }


    @Override
    @Transactional
    public void addPicture(Long cid, String url) {
        pictureRepository.save(new Picture(cid, url));
    }

    @Override
    @Transactional
    public void setDisplaySituationById(Long pid, Boolean display) {
        pictureRepository.updatePictureDisplavinfo(pid, display);
    }

    @Override
    @Transactional
    public void addPicture(Long cid, String url, Long uploaderId) {
        pictureRepository.save(new Picture(cid, url, uploaderId));
    }

    @Override
    @Transactional
    public void deletePictureById(Long id) {
        pictureRepository.deleteByid(id);
    }

    @Override
    public List<Picture> findByCatId(Long cid) {
        return pictureRepository.findByCatId(cid);
    }

    @Override
    public List<Picture> selectDisplayPictureByCatid(Long cid) {
        return pictureRepository.selectDisplayPictureByCatid(cid);
    }


}
