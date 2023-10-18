package com.kite.kolesnikov.projectservice.repository;

import com.kite.kolesnikov.projectservice.entity.vacancy.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
}
