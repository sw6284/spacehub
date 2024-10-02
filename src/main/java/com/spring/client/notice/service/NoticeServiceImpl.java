package com.spring.client.notice.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.spring.admin.notice.domain.Notice;
import com.spring.client.notice.repository.NoticeRepository;
import com.spring.common.vo.PageRequestDTO;
import com.spring.common.vo.PageResponseDTO;

import jakarta.transaction.Transactional;



@Service
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	private NoticeRepository noticeRepository;
	
	@Override
	public List<Notice> noticeList(Notice notice) {
		List<Notice> noticeList = null;
		noticeList = (List<Notice>) noticeRepository.findAll();
		return noticeList;
	}
	
	// 클래스 페이징처리
	@Override
	public PageResponseDTO<Notice> list(PageRequestDTO pageRequestDTO) {
		Pageable pageable = PageRequest.of(
				pageRequestDTO.getPage() -1, // 1페이지가 0이므로 주의
				pageRequestDTO.getSize(), Sort.by("nbNo").descending());
		Page<Notice> result = noticeRepository.findAll(pageable);
		List<Notice> noticeList = result.getContent().stream().collect(Collectors.toList());
		long totalCount = result.getTotalElements();
		PageResponseDTO<Notice> responseDTO = PageResponseDTO.<Notice>withAll().dtoList(noticeList)
				.pageRequestDTO(pageRequestDTO).totalCount(totalCount).build();
		return responseDTO;
	}
	
	@Override
	public void noticeInsert(Notice notice) {
		noticeRepository.save(notice);

	}
	
	 @Override
	    public void noticeDelete(Notice notice) {
	        noticeRepository.delete(notice);
	    }
	
	@Override
	public Notice getNotice(Long nbNo) {
	        Optional<Notice> NoticeOptional = noticeRepository.findById(nbNo);
	        Notice updateData = NoticeOptional.orElseThrow();
	        
	        return updateData;
	}
	
	
	@Override
	public void noticeHitUpdate(Notice notice) {
		Notice dataNotice = getNotice(notice.getNbNo());
		dataNotice.setNbHit(dataNotice.getNbHit()+1);
		noticeRepository.save(dataNotice);
	}

	@Override
    @Transactional
    public void noticeUpdate(Notice notice) {
        // 공지사항 엔티티를 데이터베이스에서 가져옵니다.
        Notice existingNotice = noticeRepository.findById(notice.getNbNo())
            .orElseThrow(() -> new IllegalArgumentException("Invalid notice Id: " + notice.getNbNo()));

        // 엔티티의 필드를 업데이트합니다.
        existingNotice.setNbTitle(notice.getNbTitle());
        existingNotice.setAdmNo(notice.getAdmNo());
        existingNotice.setNbContent(notice.getNbContent());
        existingNotice.setNbUpdate(LocalDateTime.now()); // 현재 시간으로 업데이트

        // 수정된 엔티티를 저장합니다.
        noticeRepository.save(existingNotice);
    }
	
	
}
