package com.promotion.handwriting.repository.database;

import com.promotion.handwriting.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying(clearAutomatically = true)
    @Query("delete from Image m where m.ad.id = :ad_id")
    void deleteAllByAd(@Param("ad_id") long adId);

}
