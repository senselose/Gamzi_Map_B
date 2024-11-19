package codequest.map.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import codequest.map.backend.entity.TestEntity;

public interface TestRepository extends JpaRepository<TestEntity, String> {
    
}
