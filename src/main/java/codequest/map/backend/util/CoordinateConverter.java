package codequest.map.backend.util;

import java.io.File;
import java.io.IOException;

import org.locationtech.proj4j.CRSFactory;
import org.locationtech.proj4j.CoordinateReferenceSystem;
import org.locationtech.proj4j.CoordinateTransform;
import org.locationtech.proj4j.CoordinateTransformFactory;
import org.locationtech.proj4j.ProjCoordinate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


// 좌표계 변환 메서드
public class CoordinateConverter {
    public static void main(String[] args) {
        // 입력 및 출력 파일 경로 설정
        String inputFilePath = "C:/ProjectCodeQuest/backend/src/main/resources/geojson/merged_si_gungu.json";
        String outputFilePath = "C:/ProjectCodeQuest/backend/src/main/resources/geojson/converted_si_gungu.json";
        convertGeoJsonCoordinates(inputFilePath, outputFilePath);
    }

    // GeoJSON 좌표 변환 메서드
    public static void convertGeoJsonCoordinates(String inputFilePath, String outputFilePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(new File(inputFilePath));

            // Proj4J 설정: 원본 좌표계(EPSG:5179)와 대상 좌표계(WGS84)
            CRSFactory crsFactory = new CRSFactory();
            CoordinateReferenceSystem sourceCRS = crsFactory.createFromName("EPSG:5179");
            CoordinateReferenceSystem targetCRS = crsFactory.createFromName("EPSG:4326");
            CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
            CoordinateTransform transform = ctFactory.createTransform(sourceCRS, targetCRS);

            // GeoJSON 구조에서 각 좌표 변환
            ArrayNode features = (ArrayNode) rootNode.get("features");
            for (JsonNode feature : features) {
                ArrayNode coordinates = (ArrayNode) feature.get("geometry").get("coordinates").get(0);

                for (int i = 0; i < coordinates.size(); i++) {
                    // 각 좌표 변환
                    ArrayNode point = (ArrayNode) coordinates.get(i);
                    double x = point.get(0).asDouble();
                    double y = point.get(1).asDouble();

                    ProjCoordinate srcCoord = new ProjCoordinate(x, y);
                    ProjCoordinate destCoord = new ProjCoordinate();

                    // 좌표 변환 수행
                    transform.transform(srcCoord, destCoord);

                    // 변환된 WGS84 좌표를 업데이트
                    point.set(0, destCoord.x); // 경도
                    point.set(1, destCoord.y); // 위도
                }
            }

            // 변환된 GeoJSON을 파일로 저장
            mapper.writeValue(new File(outputFilePath), rootNode);
            System.out.println("Coordinates successfully converted and saved to " + outputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
