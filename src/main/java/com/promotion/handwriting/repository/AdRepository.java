package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {

    Ad findByType(@Param("type") AdType type);

    List<Ad> findALLByType(@Param("type") AdType type);

    @EntityGraph(attributePaths = {"images"})
    Ad findAdWithImagesById(@Param("id") Long id);
}
