package com.example.demo.repository;

import com.example.demo.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureRepository extends JpaRepository<Picture,Long> {

    @Query("select p from Picture p where p.display=true and p.catId=:cid")
    List<Picture> selectDisplayPictureByCatid(@Param(cid) Long cid);

    @Query("select count(p) from Picture p where p.display=true and p.catId=:cid")
    int countDisplayPictureByCatid(@Param("cid") Long cid)

    @Query("select p from Picture p where p.display=false and p.catId=:cid")
    List<Picture> selectUnDisplayPictureByCatid(@Param(cid) Long cid);

    @Modifying
    @Query("update p from Picture p set p.display=:display where p.id=:id")
    void updatePictureDisplavinfo(
            @Param("id") Long id,
            @Param(("display") Boolean display)
            );

    @Modifying
    void deleteByid();

}
