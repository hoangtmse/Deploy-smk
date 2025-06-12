package com.swd.smk.services.interfac;

import com.swd.smk.dto.Response;
import com.swd.smk.dto.SmokingLogDTO;

public interface ISmokingLog {

    Response createSmokingLog(SmokingLogDTO smokingLogDTO);

    Response updateSmokingLog(Long smokingLogId, SmokingLogDTO smokingLogDTO);

    Response deleteSmokingLog(Long smokingLogId);

    Response getSmokingLogById(Long smokingLogId);

    Response getAllSmokingLogs();

}
