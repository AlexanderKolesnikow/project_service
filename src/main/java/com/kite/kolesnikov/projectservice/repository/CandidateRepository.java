package com.kite.kolesnikov.projectservice.repository;

import com.kite.kolesnikov.projectservice.entity.candidate.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
