package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.models.Banking;

public interface BankingRepo extends JpaRepository<Banking, Integer> {
	@Query("FROM Banking WHERE acc_no = :acc_no")
    Banking findByAcc_no(@Param("acc_no") int acc_no);

    @Query("FROM Banking WHERE acc_no = :acc_no AND ifsc = :ifsc")
    Banking findByAcc_noAndIfsc(@Param("acc_no") int acc_no, @Param("ifsc") String ifsc);
}

