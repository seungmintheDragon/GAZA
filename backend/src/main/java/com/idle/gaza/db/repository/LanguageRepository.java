package com.idle.gaza.db.repository;

import com.idle.gaza.db.entity.Guide;
import com.idle.gaza.db.entity.GuideLanguage;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<GuideLanguage, Integer> {

    /* 해당 언어의 코드명 가져오기 */
    @Query(value="select name from code where description=:description", nativeQuery = true)
    String searchCode(@Param("description") String description);

//    @Query(value="delete from guide_language where guide_id=:guideId and guide_language_id=:langId", nativeQuery = true)
//    int deleteLang(@Param("guideId") int guideId, @Param("langId") int langId);

    @Transactional
    int deleteByLanguageId(int lang_id);

    List<GuideLanguage> findByGuide(Guide guide);

    @Query(value="select description from code where name=:code", nativeQuery = true)
    String searchCodeName(@Param("code") String code);
}
