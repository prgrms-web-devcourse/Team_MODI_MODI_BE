package com.prgrms.modi.user.service;

import static com.google.common.base.Preconditions.checkArgument;
import static com.prgrms.modi.utils.UsernameGenerator.createRandomName;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import com.prgrms.modi.common.oauth2.info.OAuth2UserInfo;
import com.prgrms.modi.common.oauth2.info.OAuth2UserInfoFactory;
import com.prgrms.modi.common.oauth2.info.ProviderType;
import com.prgrms.modi.error.exception.InvalidUsernameException;
import com.prgrms.modi.error.exception.NotEnoughAgeException;
import com.prgrms.modi.error.exception.NotFoundException;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.party.domain.PartyStatus;
import com.prgrms.modi.party.dto.response.PartyDetailResponse;
import com.prgrms.modi.party.repository.PartyRepository;
import com.prgrms.modi.user.domain.Role;
import com.prgrms.modi.user.domain.User;
import com.prgrms.modi.user.dto.PointAmountResponse;
import com.prgrms.modi.user.dto.UserPartyBriefResponse;
import com.prgrms.modi.user.dto.UserPartyListResponse;
import com.prgrms.modi.user.dto.UserResponse;
import com.prgrms.modi.user.dto.UsernameResponse;
import com.prgrms.modi.user.repository.UserRepository;
import com.prgrms.modi.utils.UsernameGenerator;
import java.util.ArrayList;
import java.util.List;
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

    private final PartyRepository partyRepository;


    public UserService(UserRepository userRepository, PartyRepository partyRepository) {
        this.userRepository = userRepository;
        this.partyRepository = partyRepository;
    }

    @Transactional(readOnly = true)
    public UserResponse getUserDetail(long id) {
        return userRepository.findById(id)
            .map(UserResponse::from)
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
                if (userInfo.isAdult()) {
                    String username = createRandomName();
                    User newUser = new User(username, Role.USER, 0, provider, providerId);
                    return userRepository.save(newUser);
                }
                throw new NotEnoughAgeException("MODI는 만 19세 이상부터 사용할 수 있습니다");
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

    @Transactional(readOnly = true)
    public PointAmountResponse getUserPoints(long userId) {
        return userRepository.findById(userId)
            .map(user -> new PointAmountResponse(user.getPoints()))
            .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
    }

    @Transactional(readOnly = true)
    public PartyDetailResponse getUserPartyDetail(Long partyId) {
        log.info("[*] partyId: {}", partyId);
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 파티입니다"));

        return PartyDetailResponse.from(party);
    }

    @Transactional(readOnly = true)
    public UserPartyListResponse getUserPartyList(
        Long userId,
        PartyStatus partyStatus,
        Integer size,
        Long lastSortingId
    ) {
        List<UserPartyBriefResponse> parties = partyRepository
            .findAllPartiesByStatusAndUserIdAndDeletedAtIsNull(userId, partyStatus, size, lastSortingId);

        User user = userRepository.getById(userId);
        int totalSize = partyRepository
            .countAllByStatusAndMembersUser(partyStatus, user);

        return new UserPartyListResponse(parties, totalSize);
    }

    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
    }

    public List<UsernameResponse> generateUsernames(int size) {
        List<UsernameResponse> usernames = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String generatedUsername = UsernameGenerator.createRandomName();
            usernames.add(new UsernameResponse(generatedUsername));
        }

        return usernames;
    }

    @Transactional
    public void changeUsername(long userId, String username) {
        if (UsernameGenerator.isInvalidUsername(username)) {
            throw new InvalidUsernameException("올바르지 않은 닉네임 형식입니다");
        }

        User user = userRepository.getById(userId);
        user.changeUsername(username);
        userRepository.save(user);
    }

}
