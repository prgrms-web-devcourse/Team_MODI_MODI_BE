package com.prgrms.modi.ott.repository;

import com.prgrms.modi.ott.domain.OTT;
import com.prgrms.modi.ott.dto.CarouselInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OttRepository extends JpaRepository<OTT, Long> {

    @Query("select "
        + "     o.id as ottId, "
        + "     o.name as ottName, "
        + "     (select sum(p.currentMember) from Party p where o.id = p.ott.id and p.status = 'RECRUITING') as waitingForMatch, "
        + "     o.monthlyPrice as monthlyPrice "
        + " from "
        + "     OTT o")
    List<CarouselInfo> getCarouselList();

}
