package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Thêm các phương thức truy vấn tùy chỉnh nếu cần
    List<Notification> findByMemberId(Long memberId);

    List<Notification> findByMemberIdAndStatus(Long memberId, Status status);
}

