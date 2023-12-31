package com.kite.kolesnikov.projectservice.repository;

import com.kite.kolesnikov.projectservice.entity.internship.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {
}
