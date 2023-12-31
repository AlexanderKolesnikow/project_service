package com.kite.kolesnikov.projectservice.repository;

import com.kite.kolesnikov.projectservice.jpa.StageInvitationJpaRepository;
import com.kite.kolesnikov.projectservice.entity.stage_invitation.StageInvitation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StageInvitationRepository {
    private final StageInvitationJpaRepository repository;
    private final TeamMemberRepository teamMemberRepository;

    public StageInvitation save(StageInvitation stageInvitation) {
        return repository.save(stageInvitation);
    }

    public StageInvitation findById(Long stageInvitationId) {
        return repository.findById(stageInvitationId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Stage invitation doesn't exist by id: %s", stageInvitationId))
        );
    }

    public List<StageInvitation> findAll() {
        return repository.findAll();
    }
}
