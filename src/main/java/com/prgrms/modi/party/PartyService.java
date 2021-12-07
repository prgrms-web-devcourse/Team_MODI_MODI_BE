package com.prgrms.modi.party;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.service.OttService;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.dto.response.PartyResponse;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartyService {

    private final PartyRepository partyRepository;

    private final OttService ottService;

    public PartyService(PartyRepository partyRepository, OttService ottService) {
        this.partyRepository = partyRepository;
        this.ottService = ottService;
    }

    @Transactional(readOnly = true)
    public PartyListResponse getPartyList(Long ottId, Integer size) {
        OTT ott = ottService.findOtt(ottId);
        LocalDate minDate = LocalDate.of(0, 1, 1);
        PageRequest page = PageRequest.of(0, size);

        return PartyListResponse.from(
            ott,
            partyRepository
                .findAllRecruitingParty(ott, PartyStatus.RECRUITING, minDate, Long.MAX_VALUE, page)
                .stream()
                .map(PartyResponse::from)
                .collect(Collectors.toList())
        );
    }

    @Transactional(readOnly = true)
    public PartyListResponse getPartyList(Long ottId, Integer size, Long lastPartyId) {
        OTT ott = ottService.findOtt(ottId);
        Party lastParty = partyRepository.getById(lastPartyId);
        PageRequest page = PageRequest.of(0, size);

        return PartyListResponse.from(
            ott,
            partyRepository
                .findAllRecruitingParty(ott, PartyStatus.RECRUITING, lastParty.getStartDate(), lastPartyId, page)
                .stream()
                .map(PartyResponse::from)
                .collect(Collectors.toList())
        );
    }

}
