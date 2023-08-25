package com.example.Api_version.event.token;

import com.example.Api_version.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    Optional<VerificationToken> findByUser(User user);
}
