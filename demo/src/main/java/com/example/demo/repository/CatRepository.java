package com.example.demo.repository;


import com.example.demo.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface CatRepository extends JpaRepository<Cat,Long> {

    @Modifying
    void deleteById();

    @Modifying
    @Query("update c from cat c set c.age=:age where c.id=:cid")
    void updateAgeInfoByCid(
            @Param("cid") Long cid;
            @Param("age") int age;
     );

    @Modifying
    @Query("update c from cat c set c.health=:health where c.id=:cid")
    void updateHealthInfoByCid(
            @Param("cid") Long cid;
    @Param("health") String health;
     );


    @Modifying
    @Query("update c from cat c set c.jueyu=:jueyu where c.id=:cid")
    void updateJueyutInfoByCid(
            @Param("cid") Long cid;
            @Param("jueyu") Boolean jueyu;
     );

    @Modifying
    @Query("update c from cat c set c.adopted=:adopt where c.id=:cid")
    void updateAdoptInfoByCid(
            @Param("cid") Long cid;
            @Param("adopt") Boolean adopt;
     );

    @Modifying
    @Query("update c from cat c set c.feed=:feed where c.id=:cid")
    void updateFeedInfoByCid(
            @Param("cid") Long cid;
            @Param("feed") Boolean feed;
     );



    
}
