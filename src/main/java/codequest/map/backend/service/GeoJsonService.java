package codequest.map.backend.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class GeoJsonService {
    
    private final ObjectMapper mapper = new ObjectMapper();

    public void mergeGeoJsonFiles(){
        List<String> geoJsonFiles = List.of(
            "geojson3/Busan.json",
            "geojson3/Chungbuk.json",
            "geojson3/Chungnam.json",
            "geojson3/Daegu.json",
            "geojson3/Daejeon.json",
            "geojson3/Gangwon.json",
            "geojson3/Gwangju.json",
            "geojson3/Gyeonggi.json",
            "geojson3/Gyeongbuk.json",
            "geojson3/Gyeongnam.json",
            "geojson3/Incheon.json",
            "geojson3/Jeju.json",
            "geojson3/Jeonbuk.json",
            "geojson3/Jeonnam.json",
            "geojson3/Sejong.json",
            "geojson3/Seoul.json",
            "geojson3/Ulsan.json"
        );
        String outputFile = "src/main/resources/geojson3/Merged_sigungu.json";

        try{
            ArrayNode mergedFeatures = mapper.createArrayNode();

            // 각 GeoJSON 파일의 features 배열을 mergedFeatures에 추가
            for(String filePath : geoJsonFiles){
                File file = new ClassPathResource(filePath).getFile();
                JsonNode rootNode = mapper.readTree(file);
                ArrayNode features = (ArrayNode) rootNode.get("features");

                for (JsonNode feature : features) {
                    String geometryType = feature.get("geometry").get("type").asText();
            
                    // Polygon과 MultiPolygon 구분
                    if ("Polygon".equals(geometryType)) {
                        System.out.println("Polygon detected in file: " + filePath);
                    } else if ("MultiPolygon".equals(geometryType)) {
                        System.out.println("MultiPolygon detected in file: " + filePath);
                    } else {
                        System.out.println("Unexpected geometry type: " + geometryType);
                    }
                }

                mergedFeatures.addAll(features);
            }

            // 최종 GeoJSON 객체 생성
            ObjectNode mergedGeoJson = mapper.createObjectNode();
            mergedGeoJson.put("type", "FeatureCollection");
            mergedGeoJson.set("features", mergedFeatures);

            // 합친 결과를 파일로 저장
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFile), mergedGeoJson);
            System.out.println("GeoJSON Files merged successfully into " + outputFile);
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
