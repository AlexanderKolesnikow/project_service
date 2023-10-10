package com.kite.kolesnikov.project_service.repository;

import com.kite.kolesnikov.project_service.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
