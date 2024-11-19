package codequest.map.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import codequest.map.backend.entity.TestEntity;
import codequest.map.backend.service.TestService;

@RestController
public class TestCotroller {
    
    @Autowired
    private TestService testService;

    @PostMapping("/api/test")
    public void saveTestEntity(@RequestBody TestEntity testEntity){
        System.out.println(testEntity);
        testService.saveTestEntity(testEntity);
    }

    @GetMapping("/api/test")
    public List<TestEntity> gTestEntities(){
        return testService.getAllTestEntities();
    }

}
