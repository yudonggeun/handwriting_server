package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdRepository extends JpaRepository<Ad, Long> {
}
