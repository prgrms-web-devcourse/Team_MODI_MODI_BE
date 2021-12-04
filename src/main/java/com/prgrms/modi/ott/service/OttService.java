package com.prgrms.modi.ott.service;

import com.prgrms.modi.ott.converter.OttConverter;
import com.prgrms.modi.ott.dto.AllOttListResponse;
import com.prgrms.modi.ott.repository.OttRepository;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class OttService {

    private final OttRepository ottRepository;

    public OttService(OttRepository ottRepository) {
        this.ottRepository = ottRepository;
    }

    public AllOttListResponse getAllOtts() {

        return new AllOttListResponse(ottRepository.findAll().stream()
            .map(OttConverter::toOttResponse)
            .collect(Collectors.toList()));
    }

}
