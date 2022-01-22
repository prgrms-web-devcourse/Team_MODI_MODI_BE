package com.prgrms.modi.party.service;

import static com.prgrms.modi.history.service.CommissionHistoryService.COMMISSION_PERCENTAGE;

import com.prgrms.modi.history.domain.CommissionDetail;
import com.prgrms.modi.history.domain.PointDetail;
import com.prgrms.modi.history.service.CommissionHistoryService;
import com.prgrms.modi.history.service.PointHistoryService;
import com.prgrms.modi.notification.service.NotificationService;
import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.repository.OttRepository;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.domain.Rule;
import com.prgrms.modi.party.dto.request.CreatePartyRequest;
import com.prgrms.modi.party.dto.request.UpdateSharedAccountRequest;
import com.prgrms.modi.party.dto.response.PartyBriefResponse;
import com.prgrms.modi.party.dto.response.PartyDetailResponse;
import com.prgrms.modi.party.dto.response.PartyIdResponse;
import com.prgrms.modi.party.dto.response.PartyListResponse;
import com.prgrms.modi.party.dto.response.SharedAccountResponse;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.party.repository.RuleRepository;
import com.prgrms.modi.user.domain.Member;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.repository.MemberRepository;
import com.prgrms.modi.user.repository.UserRepository;
import com.prgrms.modi.utils.Encryptor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
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

    private final MemberRepository memberRepository;

    private final CommissionHistoryService commissionHistoryService;

    private final PointHistoryService pointHistoryService;

    private final NotificationService notificationService;

    private final Encryptor encryptor;

    public PartyService(
        PartyRepository partyRepository,
        RuleRepository ruleRepository,
        OttRepository ottRepository,
        UserRepository userRepository,
        MemberRepository memberRepository,
        CommissionHistoryService commissionHistoryService,
        PointHistoryService pointHistoryService,
        NotificationService notificationService, Encryptor encryptor) {
        this.partyRepository = partyRepository;
        this.ruleRepository = ruleRepository;
        this.ottRepository = ottRepository;
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.commissionHistoryService = commissionHistoryService;
        this.pointHistoryService = pointHistoryService;
        this.notificationService = notificationService;
        this.encryptor = encryptor;
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
        long partyTotalSize = partyRepository.countAvailablePartyByOtt(PartyStatus.RECRUITING, ott);

        return PartyListResponse.from(ott, partyTotalSize, parties);
    }

    @Transactional(readOnly = true)
    public PartyListResponse getPartyList(long ottId, int size, long lastPartyId) {
        OTT ott = ottRepository.getById(ottId);
        Party lastParty = partyRepository.getById(lastPartyId);

        List<PartyBriefResponse> parties = this.getRecruitingParties(ott, lastParty.getStartDate(), lastPartyId, size);
        long partyTotalSize = partyRepository.countAvailablePartyByOtt(PartyStatus.RECRUITING, ott);

        return PartyListResponse.from(ott, partyTotalSize, parties);
    }

    @Transactional
    public PartyIdResponse createParty(CreatePartyRequest request, long userId) {
        Party newParty = createNewParty(request, userId);
        partyRepository.save(newParty);

        return PartyIdResponse.from(newParty);
    }

    @Transactional
    public PartyIdResponse joinParty(long userId, long partyId) {
        User user = userRepository.getById(userId);
        Party party = partyRepository.getById(partyId);

        this.participate(user, party);
        Member leader = getPartyLeader(party);
        String participantNickName = user.getUsername();
        notificationService.send(leader, participantNickName + "님이 파티원으로 참여했습니다.", party);

        logger.info("User {} joined party {}", user, party);
        return PartyIdResponse.from(party);
    }

    private Member getPartyLeader(Party party) {
        Member leader = null;
        List<Member> members = party.getMembers();
        for (Member member : members) {
            if (member.isLeader()) {
                leader = member;
            }
        }
        return leader;
    }

    @Transactional(readOnly = true)
    public boolean isPartyMember(long partyId, long userId) {
        Party party = partyRepository.getById(partyId);
        User user = userRepository.getById(userId);

        for (Member member : party.getMembers()) {
            if (member.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public SharedAccountResponse getSharedAccount(long partyId) {
        Party party = partyRepository.getById(partyId);
        String sharedId = party.getSharedId();
        String sharedPassword = encryptor.decrypt(party.getSharedPasswordEncrypted(), party.getOtt().getId());

        return new SharedAccountResponse(sharedId, sharedPassword);
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
    public void reimburseAll(LocalDate date) {
        Predicate<Party> isReimburseDay =
            party -> party.getStartDate().getDayOfMonth() == date.getDayOfMonth()
                || (isLastDay(party.getStartDate()) && isLastDay(date));

        List<Party> parties = partyRepository.findAllReimbursableParty().stream()
            .filter(isReimburseDay)
            .collect(Collectors.toList());

        for (Party party : parties) {
            reimburse(party);
        }
        logger.info("Reimbursement is over");
    }

    @Transactional
    public void deleteNotGatherParties(LocalDate today) {
        List<Party> willDeletedParties = partyRepository.findNotGatherParties(today);
        willDeletedParties.forEach(
            (p) -> {
                memberRepository.softDeleteByPartyId(p.getId());
                partyRepository.softDelete(p.getId());
            }
        );
        logger.info("Deleted the party that wasn't enough member");
    }

    @Transactional
    public void changeToOngoing(LocalDate today) {
        partyRepository.findByStatus(PartyStatus.RECRUITING).stream()
            .filter(party -> party.getStartDate().isEqual(today))
            .forEach(party -> party.changeStatus(PartyStatus.ONGOING));

        logger.info("The status of the party that starts today has changed");
    }

    @Transactional
    public void changeToFinish(LocalDate today) {
        partyRepository.findByStatus(PartyStatus.ONGOING).stream()
            .filter(party -> Objects.equals(party.getEndDate(), today))
            .forEach(party -> party.changeStatus(PartyStatus.FINISHED));

        logger.info("The status of the party that ends today has changed");
    }

    @Transactional
    public PartyIdResponse updateSharedAccount(long partyId, UpdateSharedAccountRequest request) {
        Party party = partyRepository.getById(partyId);
        String encryptedPassword = encryptor.encrypt(request.getSharedPassword(), party.getOtt().getId());
        party.changeSharedAccount(encryptedPassword);
        partyRepository.save(party);
        if (party.getStartDate().isBefore(LocalDate.now())) {
            List<Member> members = party.getMembers();
            for (Member member : members) {
                if (!member.isLeader()) {
                    notificationService.send(member, "공유계정 정보가 변경되었습니다.", party);
                }
            }
        }

        return PartyIdResponse.from(party);
    }

    @Transactional(readOnly = true)
    public boolean isPartyLeader(Long partyId, Long userId) {
        Party party = partyRepository.getById(partyId);
        User user = userRepository.getById(userId);
        for (Member member : party.getMembers()) {
            if (member.isLeader() && member.getUser().equals(user)) {
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteParty(Long partyId) {
        Party party = partyRepository.getById(partyId);

        if (!(party.getMembers().size() == 1 && party.getStartDate().isAfter(LocalDate.now()))) {
            throw new IllegalStateException("삭제할 수 없는 파티입니다");
        }
        partyRepository.deleteById(partyId);
    }

    @Transactional
    public void hardDeleteExpiredParties(LocalDateTime dateTime) {
        partyRepository.deleteExpiredParties(dateTime);
        logger.info("Hard delete has completed");
    }

    private Party createNewParty(CreatePartyRequest request, long userId) {
        OTT ott = ottRepository.getById(request.getOttId());
        String encryptedPassword = encryptor.encrypt(
            request.getSharedPassword(), ott.getId());

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
            .sharedPasswordEncrypted(encryptedPassword)
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

    private void participate(User user, Party party) {
        int totalPrice = party.getTotalPrice();
        int commission = (int) (totalPrice * COMMISSION_PERCENTAGE);
        int monthlyPrice = party.getOtt().getMonthlyPrice();

        party.addMember(user, monthlyPrice, totalPrice);
        user.deductPoint(totalPrice + commission);

        commissionHistoryService.save(CommissionDetail.PARTICIPATE, totalPrice, user);
        pointHistoryService.save(PointDetail.PARTICIPATE, totalPrice + commission, user);
    }

    private void reimburse(Party party) {
        List<Member> members = party.getMembers();
        for (Member member : members) {
            if (member.isLeader()) {
                User user = member.getUser();
                int reimbursementAmount = party.reimburse();
                user.addPoints(reimbursementAmount);
                pointHistoryService.save(PointDetail.REIMBURSE, reimbursementAmount, user);
            }
        }
    }

    private boolean isLastDay(LocalDate localDate) {
        return localDate.getDayOfMonth() == YearMonth.from(localDate).lengthOfMonth();
    }

}
