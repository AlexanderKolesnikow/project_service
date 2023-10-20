package com.kite.kolesnikov.projectservice.jpa;

import com.kite.kolesnikov.projectservice.entity.stage.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageJpaRepository extends JpaRepository<Stage, Long> {
}
