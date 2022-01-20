package com.prgrms.modi.user.repository;

import com.prgrms.modi.user.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findMembersByUserIdAndDeletedAtIsNull(Long userId);

}
