package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByAdId(@Param("adId") Long id);

    @Modifying
    @Query("delete from Image m where m.imageName = :imageName and m.ad = :ad")
    void deleteByImageNameAndAd(@Param("imageName") String imageName, @Param("ad") Ad ad);

    void deleteAllByAd(@Param("ad") Ad ad);
}
