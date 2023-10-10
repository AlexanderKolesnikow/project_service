package com.kite.kolesnikov.project_service.repository;

import com.kite.kolesnikov.project_service.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {
}
