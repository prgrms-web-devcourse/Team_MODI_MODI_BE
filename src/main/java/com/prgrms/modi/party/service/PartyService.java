package com.prgrms.modi.party.service;

import static java.time.temporal.ChronoUnit.MONTHS;

import com.prgrms.modi.error.exception.NotFoundException;
import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.service.OttService;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.request.RuleRequest;
import com.prgrms.modi.party.dto.response.PartyBriefResponse;
import com.prgrms.modi.party.dto.response.PartyDetailResponse;
import com.prgrms.modi.party.dto.response.PartyIdResponse;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.dto.response.SharedAccountResponse;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.user.domain.Member;
import com.prgrms.modi.user.domain.User;
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

    public PartyService(
        PartyRepository partyRepository,
        OttService ottService,
        PartyRuleService partyRuleService,
        MemberService memberService
    ) {
        this.partyRepository = partyRepository;
        this.ottService = ottService;
        this.partyRuleService = partyRuleService;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public PartyDetailResponse getParty(long partyId) {
        Party party = this.findParty(partyId);
        return PartyDetailResponse.from(party);
    }

    @Transactional(readOnly = true)
    public PartyListResponse getPartyList(long ottId, int size) {
        OTT ott = ottService.findOtt(ottId);
        LocalDate minStartDate = LocalDate.of(0, 1, 1);

        List<PartyBriefResponse> parties = getRecruitingParties(ott, minStartDate, Long.MAX_VALUE, size);

        return PartyListResponse.from(ott, parties);
    }

    @Transactional(readOnly = true)
    public PartyListResponse getPartyList(long ottId, int size, long lastPartyId) {
        OTT ott = ottService.findOtt(ottId);
        Party lastParty = partyRepository.getById(lastPartyId);

        List<PartyBriefResponse> parties = getRecruitingParties(ott, lastParty.getStartDate(), lastPartyId, size);

        return PartyListResponse.from(ott, parties);
    }

    private List<PartyBriefResponse> getRecruitingParties(OTT ott, LocalDate startDate, long lastPartyId, int size) {
        PageRequest page = PageRequest.of(0, size);

        return partyRepository
            .findPartyPage(ott, PartyStatus.RECRUITING, startDate, lastPartyId, page)
            .stream()
            .map(PartyBriefResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public PartyIdResponse createParty(CreatePartyRequest request, Long userId) {
        Party newParty = saveParty(request);
        savePartyRule(newParty, request.getRules());
        saveLeader(newParty, userId);

        logger.info("created party {}", newParty);
        return PartyIdResponse.from(newParty);
    }

    @Transactional
    public PartyIdResponse joinParty(Long userId, Long partyId) {
        User user = memberService.findUser(userId);
        Party party = this.findPartyWithOtt(partyId);

        user.deductPoint(party.getTotalPrice());
        party.increaseCurrentMemberCapacity();
        party.increaseMonthlyReimbursement(party.getOtt().getmonthlyPrice());
        party.increaseRemainingReimbursement(party.getTotalPrice());
        memberService.save(party, user);
        return PartyIdResponse.from(party);
    }

    private Party findPartyWithOtt(Long partyId) {
        return partyRepository.findPartyWithOtt(partyId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 파티입니다."));
    }

    private Party saveParty(CreatePartyRequest request) {
        OTT ott = ottService.findOtt(request.getOttId());
        Party newParty = new Party.Builder()
            .ott(ott)
            .partyMemberCapacity(request.getPartyMemberCapacity())
            .currentMember(1)
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .mustFilled(request.isMustFilled())
            .totalPrice(
                getTotalPartyPrice(
                    ott.getmonthlyPrice(),
                    (int) MONTHS.between(request.getStartDate(), request.getEndDate())
                )
            )
            .monthlyReimbursement(0)
            .remainingReimbursement(0)
            .status(PartyStatus.RECRUITING)
            .sharedId(request.getSharedId())
            .sharedPasswordEncrypted(request.getSharedPassword())
            .build();
        partyRepository.save(newParty);
        return newParty;
    }

    private int getTotalPartyPrice(int monthlyPrice, int period) {
        return monthlyPrice * period;
    }

    private void savePartyRule(Party newParty, List<RuleRequest> ruleRequests) {
        for (RuleRequest ruleRequest : ruleRequests) {
            partyRuleService.savePartyRule(newParty, ruleRequest.getRuleId());
        }
    }

    private void saveLeader(Party newParty, Long userId) {
        memberService.saveLeaderMember(newParty, userId);
    }

    @Transactional(readOnly = true)
    public boolean notPartyMember(Long partyId, Long userId) {
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 파티입니다"));

        User user = memberService.findUser(userId);

        for (Member member : party.getMembers()) {
            if (member.getUser().equals(user)) {
                return false;
            }
        }

        return true;
    }

    @Transactional(readOnly = true)
    public SharedAccountResponse getSharedAccount(long partyId) {
        Party party = this.findParty(partyId);
        return SharedAccountResponse.from(party);
    }

    private Party findParty(long partyId) {
        return partyRepository.findById(partyId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 파티입니다"));
    }

}
