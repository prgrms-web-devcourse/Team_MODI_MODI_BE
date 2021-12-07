package com.prgrms.modi.ott.service;

import com.prgrms.modi.ott.dto.OttListResponse;
import com.prgrms.modi.ott.dto.OttNameResponse;
import com.prgrms.modi.ott.repository.OttRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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

}
