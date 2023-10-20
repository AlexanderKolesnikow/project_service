package com.kite.kolesnikov.projectservice.entity;

import java.util.List;

public enum TeamRole {
    OWNER,
    MANAGER,
    DEVELOPER,
    DESIGNER,
    TESTER,
    ANALYST,
    INTERN;

    public static List<TeamRole> getAll() {
        return List.of(TeamRole.values());
    }
}
