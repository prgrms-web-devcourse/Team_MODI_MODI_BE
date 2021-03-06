package com.prgrms.modi.ott.service;

import com.prgrms.modi.error.exception.NotFoundException;
import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.dto.CarouselListResponse;
import com.prgrms.modi.ott.dto.CarouselResponse;
import com.prgrms.modi.ott.dto.OttListResponse;
import com.prgrms.modi.ott.dto.OttNameResponse;
import com.prgrms.modi.ott.dto.OttResponse;
import com.prgrms.modi.ott.repository.OttRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class OttService {

    private final OttRepository ottRepository;

    public OttService(OttRepository ottRepository) {
        this.ottRepository = ottRepository;
    }

    public OttListResponse getAll() {
        List<OttNameResponse> otts = ottRepository.findAll()
            .stream()
            .map(OttNameResponse::from)
            .collect(Collectors.toList());

        return OttListResponse.from(otts);
    }

    public OttResponse getOtt(Long id) {
        OTT ott = ottRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("요청하신 Ott를 찾지 못했습니다."));

        return OttResponse.from(ott);
    }

    public CarouselListResponse getCarouselList() {
        List<CarouselResponse> carouselList = ottRepository.getCarouselList()
            .stream()
            .map(CarouselResponse::from)
            .collect(Collectors.toList());

        return CarouselListResponse.from(carouselList);
    }

}
