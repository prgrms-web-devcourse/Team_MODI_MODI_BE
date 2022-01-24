package com.prgrms.modi.user.repository;

import com.prgrms.modi.user.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findMembersByUserIdAndDeletedAtIsNull(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE "
        + "     Member m "
        + " SET "
        + "     m.deletedAt = CURRENT_TIMESTAMP "
        + " WHERE "
        + "     m.party.id = :id")
    void softDeleteByPartyId(Long id);

}
