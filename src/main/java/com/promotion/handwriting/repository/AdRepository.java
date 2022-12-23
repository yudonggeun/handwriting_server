package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {

    @EntityGraph(attributePaths = {"images"})
    @Query("select a from Ad a where a.type = 'INTRO'")
    Ad findIntroAd();

    @EntityGraph(attributePaths = {"images"})
    @Query("select a from Ad a where a.type = 'CONTENT'")
    List<Ad> findContents();

    @EntityGraph(attributePaths = {"images"})
    Ad findAdWithImagesById(@Param("id") Long id);
}
