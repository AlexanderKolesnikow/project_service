package com.kite.kolesnikov.projectservice.jpa;

import com.kite.kolesnikov.projectservice.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
