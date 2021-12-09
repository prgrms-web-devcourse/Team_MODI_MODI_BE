package com.prgrms.modi.history.repository;

import com.prgrms.modi.history.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {

    List<PointHistory> findAllByUserId(Long userId);

}
