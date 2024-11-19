package codequest.map.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import codequest.map.backend.service.GeoJsonService;

@SpringBootTest
public class GeoJsonServiceTest {
    
    @Autowired
    private GeoJsonService geoJsonService;

    @Test
    public void testMergeGeoJsonFiles(){
        geoJsonService.mergeGeoJsonFiles();
        System.out.println("GeoJSON files merged successfully.");
    }
}
