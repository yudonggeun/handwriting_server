package com.promotion.handwriting.new_handwriting.repository;

import com.promotion.handwriting.new_handwriting.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, String> {
}
