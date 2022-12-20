package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
