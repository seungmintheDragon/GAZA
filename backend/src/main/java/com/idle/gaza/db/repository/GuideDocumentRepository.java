package com.idle.gaza.db.repository;

import com.idle.gaza.db.entity.GuideDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuideDocumentRepository extends JpaRepository<GuideDocument, Integer> {
    @Query(value = "SELECT g.* FROM user as u, guide_document as g WHERE g.user_id = u.user_id and u.state_code='US3'", nativeQuery = true)
    Optional<List<GuideDocument>> searchGuideRegisterList();
}
