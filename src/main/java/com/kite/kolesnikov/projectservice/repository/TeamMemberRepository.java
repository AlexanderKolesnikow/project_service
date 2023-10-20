package com.kite.kolesnikov.projectservice.repository;

import com.kite.kolesnikov.projectservice.jpa.TeamMemberJpaRepository;
import com.kite.kolesnikov.projectservice.entity.TeamMember;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TeamMemberRepository {
    private final TeamMemberJpaRepository jpaRepository;

    public TeamMember findById(Long id) {
        return jpaRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Team member doesn't exist by id: %s", id)));
    }

    public TeamMember findByUserIdAndProjectId(long userId, long projectId) {
        return jpaRepository.findByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Team member: %s doesn't belong to the project: %s", userId, projectId)));
    }
}
