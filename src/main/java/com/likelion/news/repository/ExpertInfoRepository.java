package com.likelion.news.repository;

import com.likelion.news.entity.ExpertInfo;
import com.likelion.news.entity.enums.ExpertState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ExpertInfoRepository extends CrudRepository<ExpertInfo, Long> {
    @Query("SELECT e FROM ExpertInfo e WHERE e.state = :state")
    Page<ExpertInfo> findByState(ExpertState state, Pageable pageable);
}
