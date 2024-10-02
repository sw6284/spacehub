package com.spring.admin.space.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.admin.space.domain.Space;

import jakarta.transaction.Transactional;

public interface SpaceRepository extends JpaRepository<Space, Long>{

	// 검색 - 메서드 쿼리 생성 : spName 필드 값과 정확히 일치하는 space 엔티티 검색
	Space findBySpName(String spName);
	Space findBySpNo(Long spNo);
	// 검색 - 메서드 부분 일치 검색 : spName필드에 특정 문자열 포함된 모든 space 엔티티 검색
	List<Space> findBySpNameContaining(String spName);

	// 단순히 모든 레코드 조회하는 거면 굳이 @Query 쓸 필요 없이 JPA 기본 제공 findAll() 사용하는 것도 괜춘. 
	public List<Space> findAll();
	
	//@Modifying	// 데이터 수정, 삭제 쿼리에 주로 씀. 
	@Query("SELECT b FROM Space b WHERE b.spNo =?1")
	public Space spaceDetail(Long spNo);

	@Modifying
	@Transactional
	@Query("UPDATE Space b SET b.spName = ?2 WHERE b.spNo = ?1")
	public int spaceUpdate(Long spNo, String spName);
	
	@Modifying
	@Transactional
	@Query("DELETE FROM Space b WHERE b.spNo = ?1")
	public int spaceDelete(Long spNo);

	// 조회수 증가
	@Modifying
	@Transactional
	@Query("UPDATE Space b set b.spHit = b.spHit+1 WHERE b.spNo = ?1")
	void spaceHitUpdate(Long spNo);
	
	// 조회수가 높은 순서로 상위 5개의 공간을 가져오는 쿼리
    @Query("SELECT s FROM Space s ORDER BY s.spHit DESC")
    List<Space> findTopSpacesByHitCount(Pageable pageable); 
    
    List<Space> findTop3ByOrderBySpHitDesc();
    
    
	//spNo, admNo, spName, spCapacity, spHourPrice, spKeyword, spMainImage(null)
}


