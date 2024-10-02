package com.spring.admin.space.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.admin.space.domain.SpaceDetail;

import jakarta.transaction.Transactional;

@Repository
public interface SpaceDetailRepository extends JpaRepository<SpaceDetail, Long> {

	// spaceDetail 모두 조회
	public List<SpaceDetail> findAll();

	// 모든 게시물 조회 (JPQL)		
    @Query("SELECT d FROM SpaceDetail d")
    public List<SpaceDetail> spaceDetailList();

    // 특정 ID로 상세 페이지 조회 (spDetail에 해당하는 엔티티)
    @Query("SELECT d FROM SpaceDetail d WHERE d.spDetail = ?1")
    public SpaceDetail spaceDetailContent(Long spDetail);

    // SpaceDetail 업데이트 (spDetail에 해당하는 엔티티의 특정 필드를 업데이트)
    @Modifying
    @Transactional
    @Query("UPDATE SpaceDetail d SET d.spDescription = ?2, d.spEquipment = ?3, d.spStartTime = ?4 WHERE d.spDetail = ?1")
    public int spaceDetailUpdate(Long spDetail, String spDescription, String spEquipment, String spStartTime);

    // SpaceDetail 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM SpaceDetail d WHERE d.spDetail = ?1")
    public int spaceDetailDelete(Long spDetail);

    // 특정 공간 번호에 해당하는 SpaceDetail을 찾는 메서드
    //@Query("SELECT sd FROM SpaceDetail sd WHERE sd.space.spNo = :spNo")
	public Optional<SpaceDetail> findBySpaceSpNo(Long spNo);
	
	@Modifying
    @Transactional
    @Query("DELETE FROM SpaceDetail d WHERE d.spDetail = ?1")
	public void deleteBySpNo(Long spNo);
	
}
