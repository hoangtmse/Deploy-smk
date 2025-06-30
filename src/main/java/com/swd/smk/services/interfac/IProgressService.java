package com.swd.smk.services.interfac;

import com.swd.smk.dto.PostDTO;
import com.swd.smk.dto.ProgressDTO;
import com.swd.smk.dto.Response;

public interface IProgressService {
    Response createProgress(ProgressDTO progressDTO);

    Response updateProgress(Long progressId, ProgressDTO progressDTO);

    Response deleteProgress(Long progressId);

    Response getProgressById(Long progressId);

    Response getAllProgresses();
    Response getProgressesByMemberId(Long memberId);
}
