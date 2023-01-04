package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
