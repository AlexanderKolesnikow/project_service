package com.kite.kolesnikov.projectservice.repository;

import com.kite.kolesnikov.projectservice.jpa.StageJpaRepository;
import com.kite.kolesnikov.projectservice.entity.stage.Stage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StageRepository {
    private final StageJpaRepository jpaRepository;

    public Stage save(Stage stage) {
        return jpaRepository.save(stage);
    }

    public void delete(Stage stage) {
        jpaRepository.delete(stage);
    }

    public Stage getById(Long stageId) {
        return jpaRepository.findById(stageId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Stage not found by id: %s", stageId))
        );
    }

    public List<Stage> findAll() {
        return jpaRepository.findAll();
    }
}
