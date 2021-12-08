package com.prgrms.modi.user.repository;

import com.prgrms.modi.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
