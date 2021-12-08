package com.prgrms.modi.user.repository;

import com.prgrms.modi.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    @Query(value = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.pointHistorys WHERE u.id = :userId")
    Optional<User> findUserWithPointHistory(Long userId);

}
