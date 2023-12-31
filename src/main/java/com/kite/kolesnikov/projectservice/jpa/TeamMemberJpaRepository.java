package com.kite.kolesnikov.projectservice.jpa;

import com.kite.kolesnikov.projectservice.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberJpaRepository extends JpaRepository<TeamMember, Long> {
    @Query(
        "SELECT tm FROM TeamMember tm JOIN tm.team t " +
        "WHERE tm.userId = :userId " +
        "AND t.project.id = :projectId"
    )
    Optional<TeamMember> findByUserIdAndProjectId(long userId, long projectId);

    List<TeamMember> findByUserId(long userId);
}
