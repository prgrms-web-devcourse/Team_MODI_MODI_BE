package com.prgrms.modi.ott.service;

import com.prgrms.modi.error.exception.NotFoundException;
import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.dto.OttListResponse;
import com.prgrms.modi.ott.dto.OttNameResponse;
import com.prgrms.modi.ott.dto.OttResponse;
import com.prgrms.modi.ott.repository.OttRepository;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OttService {

    private final OttRepository ottRepository;

    public OttService(OttRepository ottRepository) {
        this.ottRepository = ottRepository;
    }

    public OttListResponse getAll() {
        return OttListResponse.from(ottRepository.findAll().stream()
            .map(OttNameResponse::from)
            .collect(Collectors.toList()));
    }

    public OttResponse getOtt(Long ottId) {
        return OttResponse.from(
            ottRepository.findById(ottId)
                .orElseThrow(() -> new NotFoundException("요청하신 Ott를 찾지 못했습니다.")));
    }

    public OTT findOtt(Long id) {
        return ottRepository.findById(id).orElseThrow(() -> new NotFoundException("존재하지 않는 OTT 입니다"));
    }

}