package com.swd.smk.services.interfac;

import com.swd.smk.dto.FeedBackDTO;
import com.swd.smk.dto.Response;

public interface IFeedbackService {
    Response getFeedbackById(Long id);
    Response getAllFeedbacks();
    Response createFeedback(Long id, FeedBackDTO feedBackDTO);
    Response updateFeedback(Long id  ,FeedBackDTO feedBackDTO);
    Response deleteFeedback(Long id);
}
