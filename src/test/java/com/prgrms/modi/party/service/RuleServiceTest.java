package com.prgrms.modi.party.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.modi.party.domain.Rule;
import com.prgrms.modi.party.dto.response.RuleListResponse;
import com.prgrms.modi.party.repository.RuleRepository;
import com.prgrms.modi.utils.MockCreator;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RuleServiceTest {

    @InjectMocks
    RuleService ruleService;

    @Mock
    RuleRepository ruleRepository;

    @Test
    @DisplayName("모든 규칙 태그를 조회할 수 있다")
    public void getAllRule() {
        // Given
        int ruleSize = 5;
        List<Rule> rules = new ArrayList<>();
        for (long i = 0; i < ruleSize; i++) {
            rules.add(MockCreator.getRuleFixture(i));
        }

        when(ruleRepository.findAll()).thenReturn(rules);

        // When
        RuleListResponse ruleList = ruleService.getAllRule();

        // Then
        verify(ruleRepository, times(1)).findAll();
        assertThat(ruleList.getRules().size(), equalTo(ruleSize));
    }

}
