package com.prgrms.modi.party.repository;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        Pageable pageable);

    @Query("SELECT DISTINCT p FROM Party p JOIN FETCH p.members m JOIN FETCH m.user WHERE p.status = 'ONGOING'")
    List<Party> findAllReimbursableParty();

    List<Party> findByStatus(PartyStatus status);

    int countAllByStatusAndMembersUser(PartyStatus partyStatus, User user);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Party p WHERE p.deletedAt < :dateTime")
    void deleteExpiredParties(LocalDateTime dateTime);

    @Query("SELECT COUNT(p) "
        + "FROM Party p "
        + "WHERE p.status = :partyStatus "
        + "    AND p.ott = :ott "
        + "    AND (p.currentMember < p.partyMemberCapacity)")
    long countAvailablePartyByOtt(PartyStatus partyStatus, OTT ott);

    @Transactional
    @Query("SELECT "
        + "     p "
        + " FROM "
        + "     Party p "
        + " WHERE "
        + "     p.startDate = :date AND "
        + "     p.mustFilled = TRUE AND "
        + "     p.currentMember < p.partyMemberCapacity "
        + "     AND p.deletedAt IS NULL ")
    List<Party> findNotGatherParties(LocalDate date);

    @Modifying
    @Transactional
    @Query("UPDATE Party p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.id = :id")
    void softDelete(Long id);

}
