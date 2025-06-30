package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    // Thêm các phương thức truy vấn tùy chỉnh nếu cần
    List<Feedback> findByMemberId(Long memberId);

    List<Feedback> findByMemberIdAndStatus(Long memberId, Status status);

}
