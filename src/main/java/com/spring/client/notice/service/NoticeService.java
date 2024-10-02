package com.spring.client.notice.service;

import java.util.List;


import com.spring.admin.notice.domain.Notice;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;


public interface NoticeService {
	
	// 모든 공지사항 목록 조회
	public List<Notice> noticeList(Notice notice);

	// 공지사항 입력
    public void noticeInsert(Notice notice);
    
    // 공지사항 삭제
	public void noticeDelete(Notice notice);

	// 공지사항 수정
	/* public void noticeUpdate(Notice notice); */

    // 공지사항 조회수 증가
    public void noticeHitUpdate(Notice notice);
    
    public Notice getNotice(Long nbNo);
    
    // 페이징
    public PageResponseDTO<Notice> list (PageRequestDTO pageRequestDTO);

    void noticeUpdate(Notice notice);
   
    
}
