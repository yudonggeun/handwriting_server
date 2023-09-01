package com.promotion.handwriting.repository.database;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {

    @EntityGraph(attributePaths = {"images"})
    List<Ad> findByType(AdType type);

    @EntityGraph(attributePaths = {"images"})
    Ad findWithImageById(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    void deleteById(long id);
}
