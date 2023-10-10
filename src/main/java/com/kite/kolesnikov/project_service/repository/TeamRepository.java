package com.kite.kolesnikov.project_service.repository;

import com.kite.kolesnikov.project_service.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
