package com.incode.backendservice.repository;


import com.incode.backendservice.domain.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Optional<Verification> findByVerificationId(String verificationId);


    @Query("""
            select count(v.id)> 0 from Verification v
            where v.verificationId = :verificationId
            """)
    Boolean existsByVerificationId(@Param("verificationId") String verificationId);
}
