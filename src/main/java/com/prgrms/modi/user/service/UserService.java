package com.prgrms.modi.user.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.prgrms.modi.utils.UsernameGenerator.createRandomName;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.prgrms.modi.common.oauth2.info.OAuth2UserInfo;
import com.prgrms.modi.common.oauth2.info.OAuth2UserInfoFactory;
import com.prgrms.modi.common.oauth2.info.ProviderType;
import com.prgrms.modi.error.exception.NotFoundException;
import com.prgrms.modi.history.domain.CommissionDetail;
import com.prgrms.modi.history.domain.PointDetail;
import com.prgrms.modi.history.service.CommissionHistoryService;
import com.prgrms.modi.history.service.PointHistoryService;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.user.domain.Role;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.dto.PointAmountDto;
import com.prgrms.modi.user.dto.UserResponse;
import com.prgrms.modi.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;

    private final PointHistoryService pointHistoryService;

    private final CommissionHistoryService commissionHistoryService;

    public UserService(
        UserRepository userRepository,
        PointHistoryService pointHistoryService,
        CommissionHistoryService commissionHistoryService
    ) {
        this.userRepository = userRepository;
        this.pointHistoryService = pointHistoryService;
        this.commissionHistoryService = commissionHistoryService;
    }

    @Transactional(readOnly = true)
    public UserResponse getUserDetail(Long id) {
        checkArgument(id != null, "userId must be provided");
        return userRepository.findById(id)
            .map(UserResponse::from)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
    }

    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
        checkArgument(isNotEmpty(provider), "provider must be provided");
        checkArgument(isNotEmpty(providerId), "providerId must be provided");

        return userRepository.findByProviderAndProviderId(provider, providerId);
    }

    @Transactional
    public User join(OAuth2User oAuth2User, String provider) {
        checkArgument(oAuth2User != null, "OAuth2User must be provided");
        checkArgument(isNotEmpty(provider), "provider must be provided");
        String providerId = extractProviderId(oAuth2User, provider);

        return findByProviderAndProviderId(provider, providerId)
            .map(user -> {
                log.warn("Already exists: {} for provider: {} providerId: {}", user, provider, providerId);
                return user;
            })
            .orElseGet(() -> {
                ProviderType providerType = ProviderType.valueOf(provider.toUpperCase());
                OAuth2UserInfo userInfo = OAuth2UserInfoFactory
                    .getOAuth2UserInfo(providerType, oAuth2User.getAttributes());
                LocalDate dateOfBirth;
                if (userInfo.getBirthyear() == null) {
                    dateOfBirth = getDateOfBirth("1995", userInfo.getBirthDay());
                } else {
                    dateOfBirth = getDateOfBirth(userInfo.getBirthyear(), userInfo.getBirthDay());
                }
                String username = createRandomName();

                return userRepository.save(
                    new User(username, Role.USER, 0, provider, providerId, dateOfBirth)
                );
            });
    }

    private String extractProviderId(OAuth2User oAuth2User, String provider) {
        String providerId;
        if (provider.equals("naver")) {
            String attributes = oAuth2User.getName().substring(4);
            int index = attributes.indexOf(",");
            providerId = attributes.substring(0, index);
        } else {
            providerId = oAuth2User.getName();
        }
        return providerId;
    }

    private LocalDate getDateOfBirth(String birthyear, String birthday) {
        log.info("birthyear : {}, birthday : {}", birthyear, birthday);
        String dateOfBirth = birthyear + birthday.replaceAll("[^0-9]", "");
        return LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    public void saveCommissionHistory(CommissionDetail commissionDetail, Integer fee, User user) {
        commissionHistoryService.save(commissionDetail, fee, user);
    }

    public void savePointHistory(PointDetail pointDetail, Integer fee, User user) {
        pointHistoryService.save(pointDetail, fee, user);
    }

    @Transactional
    public PointAmountDto getUserPoints(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("유저가 없습니다."));
        return new PointAmountDto(user.getPoints());
    }

}
