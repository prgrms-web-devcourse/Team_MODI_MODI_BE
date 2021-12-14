package com.prgrms.modi.party.repository;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PartyRepository extends JpaRepository<Party, Long>, PartyRepositoryCustom {

    @Query("SELECT p "
        + "FROM Party p "
        + "WHERE p.ott = :ott "
        + "    AND p.status = :partyStatus "
        + "    AND p.currentMember < p.partyMemberCapacity "
        + "    AND ((p.startDate = :startDate AND p.id < :lastPartyId) OR (p.startDate > :startDate)) "
        + "ORDER BY "
        + "    p.startDate ASC, p.id DESC, p.createdAt DESC")
    List<Party> findPartyPage(
        OTT ott,
        PartyStatus partyStatus,
        LocalDate startDate,
        long lastPartyId,
        Pageable pageable
    );


    @Query("SELECT DISTINCT p FROM Party p JOIN FETCH p.members m JOIN FETCH m.user WHERE p.status = 'ONGOING'")
    List<Party> findOngoingParty();

    @Query(value = "SELECT DISTINCT p FROM Party p LEFT JOIN FETCH p.ott WHERE p.id = :id")
    Optional<Party> findPartyWithOtt(Long id);

    int countAllByStatusAndMembersUser(PartyStatus partyStatus, User user);

}
