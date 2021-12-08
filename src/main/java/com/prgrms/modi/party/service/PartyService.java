package com.prgrms.modi.party.service;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.service.OttService;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.request.RuleRequest;
import com.prgrms.modi.party.dto.response.PartyIdResponse;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.dto.response.PartyResponse;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.user.service.MemberService;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PartyService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PartyRepository partyRepository;

    private final OttService ottService;

    private final PartyRuleService partyRuleService;

    private final MemberService memberService;

    public PartyService(PartyRepository partyRepository, OttService ottService, PartyRuleService partyRuleService,
        MemberService memberService) {
        this.partyRepository = partyRepository;
        this.ottService = ottService;
        this.partyRuleService = partyRuleService;
        this.memberService = memberService;
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

    @Transactional
    public PartyIdResponse createParty(CreatePartyRequest request, Long userId) {
        Party newParty = saveParty(request);
        savePartyRule(newParty, request.getRules());
        saveLeader(newParty, userId);

        logger.info("created party {}", newParty);
        return PartyIdResponse.from(newParty);
    }

    private Party saveParty(CreatePartyRequest request) {
        Party newParty = new Party.Builder()
            .ott(ottService.findOtt(request.getOttId()))
            .partyMemberCapacity(request.getPartyMemberCapacity())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .mustFilled(request.isMustFilled())
            .sharedId(request.getSharedId())
            .sharedPasswordEncrypted(request.getSharedPassword())
            .build();
        partyRepository.save(newParty);
        return newParty;
    }

    private void savePartyRule(Party newParty, List<RuleRequest> ruleRequests) {
        for (RuleRequest ruleRequest : ruleRequests) {
            partyRuleService.savePartyRule(newParty, ruleRequest.getRuleId());
        }
    }

    private void saveLeader(Party newParty, Long userId) {
        memberService.saveLeaderMember(newParty, userId);
    }

}
