package com.promotion.handwriting.repository.database;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {

    @EntityGraph(attributePaths = {"images"})
    @Query("select a from Ad a where a.type = :type'")
    List<Ad> findContents(@Param("type") AdType type);

    @EntityGraph(attributePaths = {"images"})
    Ad findAdWithImagesById(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("delete from Ad a where a.id = :ad_id")
    void deleteIgnoreReferenceById(@Param("ad_id") long adId);
}
