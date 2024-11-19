package codequest.map.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import codequest.map.backend.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    
    // 모든 위치 데이터를 가져오는 쿼리 메서드
    @Query("SELECT p.location FROM PostEntity p")
    List<String> findAllLocations();
}
