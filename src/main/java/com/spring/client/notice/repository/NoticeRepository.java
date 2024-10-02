package com.spring.client.notice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.spring.admin.notice.domain.Notice;


public interface NoticeRepository extends JpaRepository<Notice, Long> {
	
	// 모든 레코드 조회
    @Query("SELECT n FROM Notice n")
	public List<Notice> noticeList(Notice notice);
    @Modifying
    @Query("DELETE FROM Notice n WHERE n.nbNo = ?1")
    public int noticeDelete(Long nbNo);
    
    // 공지사항 삽입 메소드
    default Notice insertNoticeBoard(Notice notice) {
        return save(notice);
    }
    
    // 조회수 증가
    @Modifying
    @Query("UPDATE Notice n SET n.nbHit = n.nbHit + 1 WHERE n.nbNo = ?1")
    void noticeHitUpdate(Long nbNo);
    
    // 페이징 처리 구문
    @Query("SELECT b FROM Notice b")
    public Page<Notice> noticeListPaging(Pageable pageable); 
}
	

