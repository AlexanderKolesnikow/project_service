package com.kite.kolesnikov.projectservice.jpa;

import com.kite.kolesnikov.projectservice.entity.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

    @Query(
            "SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END " +
                    "FROM Project p " +
                    "WHERE p.ownerId = :ownerId AND p.name = :name"
    )
    boolean existsByOwnerIdAndName(Long ownerId, String name);
}
