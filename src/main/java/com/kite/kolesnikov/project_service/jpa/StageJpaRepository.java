package com.kite.kolesnikov.project_service.jpa;

import com.kite.kolesnikov.project_service.model.stage.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageJpaRepository extends JpaRepository<Stage, Long> {
}
