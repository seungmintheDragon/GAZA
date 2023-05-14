package com.idle.gaza.db.repository;

import com.idle.gaza.db.entity.Code;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CodeRepository extends JpaRepository<Code, Integer> {

    @Query(value = "select * from code where parent_id = (select code_id from code where description=:name)", nativeQuery = true)
    List<Code> getCodeList(@Param("name") String name);

}
