package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import com.example.demo.entity.Adopt;

@Repository
public interface AdoptRepository extends JpaRepository<Adopt,Long> {



}
