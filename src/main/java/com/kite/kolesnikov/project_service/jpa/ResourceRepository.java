package com.kite.kolesnikov.project_service.jpa;

import com.kite.kolesnikov.project_service.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
