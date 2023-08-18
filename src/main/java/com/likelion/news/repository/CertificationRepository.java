package com.likelion.news.repository;

import com.likelion.news.entity.Certification;
import com.likelion.news.entity.ExpertInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends CrudRepository<Certification, Long> {
    List<Certification> findAllByExpertInfo(ExpertInfo expertInfo);

    List<Certification> deleteAllByExpertInfo(ExpertInfo expertInfo);
}
