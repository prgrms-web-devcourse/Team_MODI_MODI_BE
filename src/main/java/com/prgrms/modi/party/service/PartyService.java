package com.prgrms.modi.party.service;

import com.prgrms.modi.error.exception.NotFoundException;
import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.repository.OttRepository;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.domain.Rule;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.response.PartyBriefResponse;
import com.prgrms.modi.party.dto.response.PartyDetailResponse;
import com.prgrms.modi.party.dto.response.PartyIdResponse;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.dto.response.SharedAccountResponse;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.party.repository.RuleRepository;
import com.prgrms.modi.user.domain.Member;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.repository.UserRepository;
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

    private final RuleRepository ruleRepository;

    private final OttRepository ottRepository;

    private final UserRepository userRepository;

    private final MemberService memberService;

    public PartyService(
        PartyRepository partyRepository,
        RuleRepository ruleRepository,
        OttRepository ottRepository,
        UserRepository userRepository,
        MemberService memberService
    ) {
        this.partyRepository = partyRepository;
        this.ruleRepository = ruleRepository;
        this.ottRepository = ottRepository;
        this.userRepository = userRepository;
        this.memberService = memberService;
    }

    @Transactional(readOnly = true)
    public PartyDetailResponse getParty(long partyId) {
        Party party = partyRepository.getById(partyId);
        return PartyDetailResponse.from(party);
    }

    @Transactional(readOnly = true)
    public PartyListResponse getPartyList(long ottId, int size) {
        OTT ott = ottRepository.getById(ottId);
        LocalDate minStartDate = LocalDate.of(0, 1, 1);

        List<PartyBriefResponse> parties = this.getRecruitingParties(ott, minStartDate, Long.MAX_VALUE, size);

        return PartyListResponse.from(ott, parties);
    }

    @Transactional(readOnly = true)
    public PartyListResponse getPartyList(long ottId, int size, long lastPartyId) {
        OTT ott = ottRepository.getById(ottId);
        Party lastParty = partyRepository.getById(lastPartyId);

        List<PartyBriefResponse> parties = this.getRecruitingParties(ott, lastParty.getStartDate(), lastPartyId, size);

        return PartyListResponse.from(ott, parties);
    }

    @Transactional
    public PartyIdResponse createParty(CreatePartyRequest request, long userId) {
        Party newParty = createNewParty(request, userId);
        partyRepository.save(newParty);

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

    @Transactional(readOnly = true)
    public boolean notPartyMember(Long partyId, Long userId) {
        Party party = partyRepository.getById(partyId);
        User user = userRepository.getById(userId);

        for (Member member : party.getMembers()) {
            if (member.getUser().equals(user)) {
                return false;
            }
        }
        return true;
    }

    @Transactional(readOnly = true)
    public SharedAccountResponse getSharedAccount(long partyId) {
        Party party = partyRepository.getById(partyId);
        return SharedAccountResponse.from(party);
    }

    private List<PartyBriefResponse> getRecruitingParties(OTT ott, LocalDate startDate, long lastPartyId, int size) {
        PageRequest page = PageRequest.of(0, size);

        return partyRepository
            .findPartyPage(ott, PartyStatus.RECRUITING, startDate, lastPartyId, page)
            .stream()
            .map(PartyBriefResponse::from)
            .collect(Collectors.toList());
    }

    private Party findPartyWithOtt(Long partyId) {
        return partyRepository.findPartyWithOtt(partyId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 파티입니다."));
    }

    private Party createNewParty(CreatePartyRequest request, long userId) {
        OTT ott = ottRepository.getById(request.getOttId());

        Party newParty = new Party.Builder()
            .ott(ott)
            .partyMemberCapacity(request.getPartyMemberCapacity())
            .currentMember(1)
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .mustFilled(request.isMustFilled())
            .monthlyReimbursement(0)
            .remainingReimbursement(0)
            .status(PartyStatus.RECRUITING)
            .sharedId(request.getSharedId())
            .sharedPasswordEncrypted(request.getSharedPassword())
            .build();

        List<Rule> rules = request.getRules()
            .stream()
            .map(req -> ruleRepository.getById(req.getRuleId()))
            .collect(Collectors.toList());
        newParty.setRules(rules);

        User leader = userRepository.getById(userId);
        newParty.setLeaderMember(leader);

        return newParty;
    }

}
