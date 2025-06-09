package com.swd.smk.services.interfac;

import com.swd.smk.dto.LoginRequest;
import com.swd.smk.dto.AdminDTO;
import com.swd.smk.dto.Response;

public interface IAdminService {

    Response registerAdmin(AdminDTO adminRequest);

    Response loginAdmin(AdminDTO loginRequest);

    Response getAllAdmins();

    Response deleteAdmin(Long adminId);

    Response getAdminById(Long adminId);

    Response updateAdmin(Long adminId, AdminDTO adminDTO);
}
