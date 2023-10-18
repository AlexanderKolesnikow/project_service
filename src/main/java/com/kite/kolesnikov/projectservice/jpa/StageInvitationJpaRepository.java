package com.kite.kolesnikov.projectservice.jpa;

import com.kite.kolesnikov.projectservice.entity.TeamMember;
import com.kite.kolesnikov.projectservice.entity.stage.Stage;
import com.kite.kolesnikov.projectservice.entity.stage_invitation.StageInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StageInvitationJpaRepository extends JpaRepository<StageInvitation, Long>, JpaSpecificationExecutor<StageInvitation> {

    boolean existsByAuthorAndInvitedAndStage(TeamMember author, TeamMember invited, Stage stage);

    boolean existsByInvitedAndStage(TeamMember invited, Stage stage);
}
