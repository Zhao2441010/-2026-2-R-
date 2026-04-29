package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long>  {

//    @Modifying
//    @Query("u")

    @Modifying
    @Query("update User u set u.password =:password where u.id=:uid")
    void updatePasswordInfoByUid(
            @Param("uid") Long uid,
            @Param("password") String password

    );

    @Modifying
    @Query("update  User u set u.username =:username where u.id=:uid")
    void updateUsernameInfoByUid(
            @Param("uid") Long uid,
            @Param("username") String username
        );

    @Modifying
    @Query("update User u set u.realname =:realname where u.id=:uid")
    void updateRealnameInfoByUid(
            @Param("uid") Long uid,
            @Param("realname") String realname
        );

    @Modifying
    @Query("update User u set u.age =:age where u.id=:uid")
    void updateAgeInfoByUid(
            @Param("uid") Long uid,
            @Param("age") int age
        );

    @Modifying
    @Query("update User u set u.phoneNumber =:phoneNumber where u.id=:uid")
    void updatePhoneInfoByUid(
            @Param("uid") Long uid,
            @Param("phoneNumber") String phoneNumber
        );

    @Modifying
    @Query("update User u set u.address =:address where u.id=:uid")
    void updateAddressInfoByUid(
            @Param("uid") Long uid,
            @Param("address") String address
        );

    @Query("select u from User u where u.id=:id")
    User getUserById(@Param("id") Long id);

    @Query("select u from User u")
    List<User> getAll();

    @Query("select count(u) from User u")
    Long countAll();

    @Query("select u from User u where u.phoneNumber=:phonenumber")
    User getUserByPhoneNumber(@Param("phonenumber") String phonenumber);



}
