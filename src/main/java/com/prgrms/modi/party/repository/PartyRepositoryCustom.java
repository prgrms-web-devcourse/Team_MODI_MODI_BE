package com.prgrms.modi.party.repository;

import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.user.dto.UserPartyBriefResponse;
import java.util.List;

public interface PartyRepositoryCustom {

    List<UserPartyBriefResponse> findAllPartiesByStatusAndUserId(Long userId, PartyStatus status, Integer size,
        Long lastPartyId);

}
