package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(String userId);

    @Override
    @Query(value = "select count(*) from (select * from user limit 0, 1) a", nativeQuery = true)
    long count();
}
