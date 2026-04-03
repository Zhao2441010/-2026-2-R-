package com.example.demo.repository;

import com.example.demo.entity.User;
import org.hibernate.query.SelectionQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>  {

    @Modifying
    @Query("update u from user u set u.password =:password where u.id=:uid")
    void updateInfoByUid(
            @Param("uid") Long uid,
            @Param("password") String password;

    );

    @Modifying
    @Query("update u from user u set u.username =:username where u.id=:uid")
    void updateUsernameInfoByUid(
            @Param("uid") Long uid,
            @Param("username") String username;
        );

    @Modifying
    @Query("update u from user u set u.realname =:realname where u.id=:uid")
    void updateInfoByUid(
            @Param("uid") Long uid,
            @Param("realname") String realname;
        );

    @Modifying
    @Query("update u from user u set u.age =:age where u.id=:uid")
    void updateInfoByUid(
            @Param("uid") Long uid,
            @Param("age") int age;
        );

    @Modifying
    @Query("update u from user u set u.phoneNumber =:phoneNumber where u.id=:uid")
    void updateInfoByUid(
            @Param("uid") Long uid,
            @Param("phoneNumber") String phoneNumber;
        );

    @Modifying
    @Query("update u from user u set u.address =:address where u.id=:uid")
    void updateInfoByUid(
            @Param("uid") Long uid,
            @Param("address") String address;
        );

}
