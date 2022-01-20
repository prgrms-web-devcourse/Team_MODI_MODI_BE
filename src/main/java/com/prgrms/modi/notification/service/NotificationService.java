package com.prgrms.modi.notification.service;

import com.prgrms.modi.notification.domain.Notification;
import com.prgrms.modi.notification.dto.NotificationResponse;
import com.prgrms.modi.notification.dto.NotificationsResponse;
import com.prgrms.modi.notification.repository.EmitterRepository;
import com.prgrms.modi.notification.repository.NotificationRepository;
import com.prgrms.modi.party.domain.Party;
import com.prgrms.modi.user.domain.Member;
import com.prgrms.modi.user.repository.MemberRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final EmitterRepository emitterRepository;

    private final NotificationRepository notificationRepository;

    private final MemberRepository memberRepository;

    public NotificationService(EmitterRepository emitterRepository,
        NotificationRepository notificationRepository,
        MemberRepository memberRepository) {
        this.emitterRepository = emitterRepository;
        this.notificationRepository = notificationRepository;
        this.memberRepository = memberRepository;
    }

    public SseEmitter subscribe(Long userId, String lastEventId) {
        //어떤 데이터까지 제대로 전송되었는지 확인하기 위해
        String id = userId + "_" + System.currentTimeMillis();
        log.info("[*] emitter id: {}", id);

        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        log.info("[*] emitter test - emitter: {}", emitter.toString());
        log.info("[*] emitter test - emitter TIMEOUT: {}", emitter.getTimeout());

        //SseEmitter DEFAULT_TIMEOUT 만큼 연결 유지
        SseEmitter savedEmitter = emitterRepository.save(id, emitter);
        log.info("[*] emitter test - savedEmitter: {}", savedEmitter.toString());
        log.info("[*] emitter test - savedEmitter TIMEOUT: {}", savedEmitter.getTimeout());

        //모든 오류로 비동기 비정상 동작 시 SseEmitter 삭제
        savedEmitter.onCompletion(() -> emitterRepository.deleteById(id));
        savedEmitter.onTimeout(() -> emitterRepository.deleteById(id));

        //503 에러 방지를 위해 더미데이터 전송
        sendToClient(savedEmitter, id, "EventStream Created. [userId=" + userId + "]");

        log.info("[*] timeout: {}", savedEmitter.getTimeout());

        //클라이언트가 못 받은 event 있을 때는 전송(유실예방)
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(userId));
            events.entrySet().stream()
                .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                .forEach(entry -> sendToClient(savedEmitter, entry.getKey(), entry.getValue()));
        }

        log.info("[*] emitter test - 2: {}", savedEmitter.toString());
        log.info("[*] emitter timeout - 2: {}", savedEmitter.getTimeout());

        SseEmitter testEmitter = new SseEmitter(DEFAULT_TIMEOUT);
        log.info("[*] testEmitter: {}", testEmitter.getTimeout());
        log.info("[*] testEmitter: {}", testEmitter.toString());

        return savedEmitter;
    }

    public void send(Member member, String content, Party party) {
        Notification notification = createNotification(member, content, party);
        String id = String.valueOf(member.getUser().getId());
        notificationRepository.save(notification);

        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
            (key, emitter) -> {
                emitterRepository.saveEventCache(key, notification);
                sendToClient(emitter, key, NotificationResponse.from(notification));
            }
        );
    }

    private Notification createNotification(Member receiver, String content, Party party) {
        return new Notification(content, false, receiver, party);
    }

    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                .id(id)
                .name("sse")
                .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("Connection Fail");
        }
    }

    @Transactional
    public void readNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 알람입니다"));
        notification.read();
    }

    @Transactional
    public NotificationsResponse findAllById(Long userId) {
        List<Member> memberList = memberRepository.findMembersByUserId(userId);
        long unreadCount = 0L;
        List<NotificationResponse> notificationResponseList = new ArrayList<>();
        for (Member member : memberList) {
            List<Notification> notificationList = notificationRepository
                .findByMemberIdAndReadCheckFalse(member.getId());
            for (Notification notification : notificationList) {
                NotificationResponse from = NotificationResponse.from(notification);
                notificationResponseList.add(from);
            }
        }
        Collections.sort(notificationResponseList);
        unreadCount += notificationResponseList.size();

        return NotificationsResponse.of(notificationResponseList, unreadCount);
    }


}
