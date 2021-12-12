package com.prgrms.modi.party.repository;

import static com.prgrms.modi.party.domain.QParty.party;
import static com.prgrms.modi.user.domain.QMember.member;

import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.user.dto.QUserPartyBriefResponse;
import com.prgrms.modi.user.dto.UserPartyBriefResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;

public class PartyRepositoryImpl implements PartyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PartyRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<UserPartyBriefResponse> findAllPartiesByStatusAndUserId(Long userId, PartyStatus status, Integer size,
        Long lastPartyId) {
        return queryFactory
            .select(new QUserPartyBriefResponse(party.id, party.status, party.ott.id, party.ott.name, party.startDate,
                party.endDate, member.isLeader, party.monthlyReimbursement, party.remainingReimbursement,
                (party.totalPrice.castToNum(Integer.class).divide(party.period.castToNum(Integer.class))), party.totalPrice
            ))
            .from(member)
            .leftJoin(party).on(party.id.eq(member.party.id))
            .where(member.user.id.eq(userId)
                .and(party.status.eq(status)))
            .where(
                ltPartyId(lastPartyId)
            )
            .orderBy(member.createdAt.desc())
            .limit(size)
            .fetch();
    }

    private BooleanExpression ltPartyId(Long lastPartyId) {
        if (lastPartyId == null) {
            return null;
        }
        return party.id.lt(lastPartyId);
    }

}
