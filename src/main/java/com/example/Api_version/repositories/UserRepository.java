package com.example.Api_version.repositories;

import com.example.Api_version.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCodeUser(String codeUser);
    Optional<User> findByLastnameAndFirstnameAndEmail(String lastname, String firstname, String email);
}
