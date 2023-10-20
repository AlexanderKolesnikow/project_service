package com.kite.kolesnikov.projectservice.jpa;

import com.kite.kolesnikov.projectservice.entity.resource.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
