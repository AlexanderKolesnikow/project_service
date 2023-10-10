package com.kite.kolesnikov.project_service.repository;

import com.kite.kolesnikov.project_service.model.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}