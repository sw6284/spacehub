package com.spring.admin.space.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.admin.space.domain.SpaceDetail;
import com.spring.admin.space.domain.SpaceImg;

import jakarta.transaction.Transactional;

@Repository
public interface SpaceImgRepository extends JpaRepository<SpaceImg, Long> {

    // spImg 필드와 정확히 일치하는 SpaceImg 엔티티 검색
    SpaceImg findBySpImg(String spImg);

    // spImg 필드에 특정 문자열을 포함한 SpaceImg 엔티티 목록 검색
    List<SpaceImg> findBySpImgContaining(String spImg);

    //공간의 이미지를 조회하는 메서드를 제공, getSpaceImgsBySpaceIdh 사용해서 이미지 리스트 가져옴
    SpaceImg findBySpaceDetailSpaceSpNo(Long spNo);
    
    //주어진 SpaceDetail 객체와 연결된 모든 SpaceImg 엔티티를 반환하는 메서드
	List<SpaceImg> findBySpaceDetail(SpaceDetail spaceDetail);
	
    // 모든 SpaceImg 레코드를 조회
    @Query("SELECT i FROM SpaceImg i")
    public List<SpaceImg> spaceImgList();

    // 특정 apiNo에 해당하는 SpaceImg 조회
    @Query("SELECT i FROM SpaceImg i WHERE i.apiNo = ?1")
    public SpaceImg spaceImgContent(Long apiNo);

    // 특정 apiNo에 해당하는 SpaceImg를 업데이트
    @Modifying
    @Transactional
    @Query("UPDATE SpaceImg i SET i.spImg = ?2 WHERE i.apiNo = ?1")
    public int spaceImgUpdate(Long apiNo, String spImg);

    // 특정 apiNo에 해당하는 SpaceImg 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM SpaceImg i WHERE i.apiNo = ?1")
    public int spaceImgDelete(Long apiNo);

}
