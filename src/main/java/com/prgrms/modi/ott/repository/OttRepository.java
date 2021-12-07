package com.prgrms.modi.ott.repository;

import com.prgrms.modi.ott.domain.OTT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OttRepository extends JpaRepository<OTT, Long> {

}
