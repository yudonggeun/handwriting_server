package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from Image m where m.ad = :ad")
    void deleteAllByAd(@Param("ad") Ad ad);
}
