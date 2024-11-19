package codequest.map.backend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import codequest.map.backend.entity.TestEntity;
import codequest.map.backend.repository.TestRepository;

@Service
public class TestService {
    
    @Autowired
    private TestRepository testRepository;

    public void saveTestEntity(TestEntity testEntity){
        testRepository.save(testEntity);
    }

    public List<TestEntity> getAllTestEntities(){
        return testRepository.findAll();
    }
}
