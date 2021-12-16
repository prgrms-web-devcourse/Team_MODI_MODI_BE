package com.prgrms.modi.common.scheduler;

import com.prgrms.modi.party.service.PartyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReimbursementScheduler {

    private static Logger log = LoggerFactory.getLogger(ReimbursementScheduler.class);

    private final PartyService partyService;

    public ReimbursementScheduler(PartyService partyService) {
        this.partyService = partyService;
    }

    @Scheduled(cron = "0 0 05 * * ?")
    public void reimburse() {
        LocalDate today = LocalDate.now();
        partyService.reimburseAll(today);
        partyService.changeRecruitingStatus(today);
        partyService.changeFinishStatus(today);
    }
}
