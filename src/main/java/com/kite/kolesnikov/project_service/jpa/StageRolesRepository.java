package com.kite.kolesnikov.project_service.jpa;

import com.kite.kolesnikov.project_service.model.stage.StageRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StageRolesRepository extends JpaRepository<StageRoles, Long> {
}
