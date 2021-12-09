package com.prgrms.modi.history.repository;

import com.prgrms.modi.history.domain.CommissionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommissionHistoryRepository extends JpaRepository<CommissionHistory, Long> {

    List<CommissionHistory> findAllByUserId(Long userId);

}
