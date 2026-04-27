package com.example.demo.repository;


import com.example.demo.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends JpaRepository<Cat,Long> {

    @Modifying
    void deleteById(Long id);

    @Query("select c from Cat c where c.id=:id")
    Cat getById(@Param("id") Long id);

    @Query("select c from Cat c ")
    List<Cat> getAll();


    @Modifying
    @Query("update Cat c  set c.image=:image where c.id=:cid")
    void updateAgeInfoByCid(
            @Param("cid") Long cid,
            @Param("image") String image
    );


    @Modifying
    @Query("update Cat c  set c.age=:age where c.id=:cid")
    void updateAgeInfoByCid(
            @Param("cid") Long cid,
            @Param("age") int age
     );

    @Modifying
    @Query("update Cat c set c.health=:health where c.id=:cid")
    void updateHealthInfoByCid(
            @Param("cid") Long cid,
            @Param("health") String health
     );


    @Modifying
    @Query("update  Cat c set c.jueyu=:jueyu where c.id=:cid")
    void updateJueyutInfoByCid(
            @Param("cid") Long cid,
            @Param("jueyu") Boolean jueyu
     );

    @Modifying
    @Query("update Cat c set c.feed=:feed where c.id=:cid")
    void updateFeedInfoByCid(
            @Param("cid") Long cid,
            @Param("feed") Boolean feed
     );




    
}
