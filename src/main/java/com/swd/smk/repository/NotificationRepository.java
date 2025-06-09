package com.swd.smk.repository;

import com.swd.smk.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Thêm các phương thức truy vấn tùy chỉnh nếu cần
}

