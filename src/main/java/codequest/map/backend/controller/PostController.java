package codequest.map.backend.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import codequest.map.backend.entity.PostEntity;
import codequest.map.backend.repository.PostRepository;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {
    
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @PostMapping
    public ResponseEntity<PostEntity> createPost(@RequestBody PostEntity post) {
        PostEntity savedPost = postRepository.save(post); // 게시글 저장

        // 위치 정보만 반환하는 API를 호출하거나 새로운 데이터를 전달
        updateCSVWithLocationData(); // CSV 업데이트 로직 실행 (메서드 설명 아래 참조)

        return ResponseEntity.ok(savedPost); // 저장된 게시글 반환
    }

    // CSV 업데이트를 수행하는 메서드
    private void updateCSVWithLocationData() {
        List<String> locations = postRepository.findAllLocations(); // 모든 위치 데이터 조회
        // CSV 작성 로직 추가 (ExportCSV와 비슷한 방식)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("addresses_with_coordinates.csv"))) {
            writer.write("ID,Address,SiGunGu,Latitude,Longitude\n"); // CSV 헤더 작성
            for (String location : locations) {
                // 좌표 데이터를 가져오는 로직 호출 (필요 시 getCoordinates 통합)
                String csvRow = createCSVRow(location);
                writer.write(csvRow + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CSV 행 생성 메서드 (이전에 설명한 메서드)
    private String createCSVRow(String location) {
        Map<String, String> coordinateData = getCoordinates(location); // 좌표 및 시군구 데이터 가져오기
        String address = location;
        String siGunGu = coordinateData.get("siGunGu");
        String lat = coordinateData.get("lat");
        String lng = coordinateData.get("lng");
        String id = String.valueOf(address.hashCode());

        return String.format("%s,%s,%s,%s,%s", id, address, siGunGu, lat, lng);
    }

        // 좌표 데이터를 가져오는 헬퍼 메서드
        private Map<String, String> getCoordinates(String address) {
            // 실제 카카오 API 호출 로직을 작성하세요.
            Map<String, String> data = new HashMap<>();
            data.put("siGunGu", "서울특별시 강남구");
            data.put("lat", "37.5665");
            data.put("lng", "126.9780");
            return data;
        }
    
    // CSV 데이터를 JSON으로 반환하는 메서드 추가
    @GetMapping("/csv-data")
    public ResponseEntity<List<Map<String, String>>> getCsvData() {
        List<Map<String, String>> responseData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("addresses_with_coordinates.csv"))) {
            String line;
            reader.readLine(); // 헤더 건너뜀
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Map<String, String> data = new HashMap<>();
                data.put("lat", parts[3]);
                data.put("lng", parts[4]);
                data.put("siGunGu", parts[2]);
                responseData.add(data);
            }
            return ResponseEntity.ok(responseData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public List<PostEntity> getAllPosts() {
        return postRepository.findAll();
    }

    @GetMapping("/addresses")
    public List<String> getAllAddresses() {
        // 모든 주소 값만 반환
        return postRepository.findAll()
                             .stream()
                             .map(PostEntity::getLocation) // 주소(location)만 추출
                             .collect(Collectors.toList());
    }


}
