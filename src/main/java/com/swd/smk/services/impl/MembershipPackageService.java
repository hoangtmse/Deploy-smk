package com.swd.smk.services.impl;

import com.swd.smk.config.VnpayConfig;
import com.swd.smk.config.VnpayProperties;
import com.swd.smk.dto.MemberShipPackageDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.model.MembershipPackage;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.repository.MembershipPackageRepository;
import com.swd.smk.services.interfac.IMembershipPackage;
import com.swd.smk.utils.Converter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MembershipPackageService implements IMembershipPackage {

    @Autowired
    MembershipPackageRepository membershipPackageRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    VnpayConfig vnpayConfig;

    @Autowired
    VnpayProperties vnpayProperties;

    @Override
    public Response getMembershipPackageById(Long id) {
        Response response = new Response();
        try{
            Optional<MembershipPackage> packageOpt = membershipPackageRepository.findById(id);
            if (packageOpt.isEmpty()) {
                throw new OurException("Membership package not found");
            }
            response.setStatusCode(200);
            response.setMessage("Membership package retrieved successfully");
            response.setMembership_Package(Converter.convertMemberShipPackageDTO(packageOpt.get()));

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllMembershipPackages() {
        Response response = new Response();
        try{
            List<MembershipPackage> packages = membershipPackageRepository.findAll();
            if (packages.isEmpty()) {
                throw new OurException("No membership packages found");
            }
            List<MemberShipPackageDTO> memberShipPackageDTO = packages.stream()
                    .map(Converter::convertMemberShipPackageDTO)
                    .toList();

            response.setStatusCode(200);
            response.setMessage("Membership packages retrieved successfully");
            response.setMembership_Packages(memberShipPackageDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response createMembershipPackage(MemberShipPackageDTO memberShipPackageDTO) {
        Response response = new Response();
        try {
            if (memberShipPackageDTO.getPackageName() == null || memberShipPackageDTO.getPackageName().trim().isEmpty()) {
                throw new OurException("Tên gói không được để trống");
            }

            if (memberShipPackageDTO.getPrice() == null || memberShipPackageDTO.getPrice() < 5000.0) {
                throw new OurException("Giá gói phải từ 5.000 VNĐ trở lên");
            }

            // Tạo đối tượng MembershipPackage
            MembershipPackage membershipPackage = new MembershipPackage();
            membershipPackage.setPackageName(memberShipPackageDTO.getPackageName().trim());
            membershipPackage.setPrice(memberShipPackageDTO.getPrice());
            membershipPackage.setStatus(Status.ACTIVE);
            membershipPackage.setDescription(memberShipPackageDTO.getDescription());
            membershipPackage.setDateUpdated(LocalDate.now());
            membershipPackage.setDateCreated(LocalDate.now());

            membershipPackageRepository.save(membershipPackage);

            response.setStatusCode(200);
            response.setMessage("Tạo gói thành viên thành công");
            response.setMembership_Package(Converter.convertMemberShipPackageDTO(membershipPackage));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Lỗi máy chủ: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateMembershipPackage(Long id, MemberShipPackageDTO memberShipPackageDTO) {
        Response response = new Response();
        try{
            Optional<MembershipPackage> packageOpt = membershipPackageRepository.findById(id);
            if (packageOpt.isEmpty()) {
                throw new OurException("Membership package not found");
            }
            MembershipPackage membershipPackage = packageOpt.get();
            if (memberShipPackageDTO.getPackageName() != null) membershipPackage.setPackageName(memberShipPackageDTO.getPackageName());
            if(memberShipPackageDTO.getPrice() != null) membershipPackage.setPrice(memberShipPackageDTO.getPrice());
            if (memberShipPackageDTO.getDescription() != null)membershipPackage.setDescription(memberShipPackageDTO.getDescription());
            membershipPackage.setStatus(Status.ACTIVE);
            membershipPackage.setDateUpdated(LocalDate.now());
            membershipPackageRepository.save(membershipPackage);

            response.setStatusCode(200);
            response.setMessage("Membership package updated successfully");
            response.setMembership_Package(Converter.convertMemberShipPackageDTO(membershipPackage));

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteMembershipPackage(Long id) {
        Response response = new Response();
        try{
            Optional<MembershipPackage> packageOpt = membershipPackageRepository.findById(id);
            if (packageOpt.isEmpty()) {
                throw new OurException("Membership package not found");
            }
            MembershipPackage membershipPackage = packageOpt.get();
            membershipPackage.setStatus(Status.DELETED);
            membershipPackage.setDateUpdated(LocalDate.now());
            membershipPackageRepository.save(membershipPackage);

            response.setStatusCode(200);
            response.setMessage("Membership package deleted successfully");

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response buyMembershipPackage(Long memberId, Long packageId, HttpServletRequest request) {
        Response response = new Response();
        try {
            // 1. Kiểm tra tồn tại
            Optional<MembershipPackage> packageOpt = membershipPackageRepository.findById(packageId);
            Optional<Member> memberOpt = memberRepository.findById(memberId);
            if (packageOpt.isEmpty()) throw new OurException("Membership package not found");
            if (memberOpt.isEmpty()) throw new OurException("Member not found");

            MembershipPackage membershipPackage = packageOpt.get();
            Member member = memberOpt.get();

            // 2. Sinh txnRef và lưu tạm nếu cần
            String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
            long amount = (long) (membershipPackage.getPrice() * 100); // nhân 100 theo yêu cầu VNPAY
            String vnp_TmnCode = vnpayProperties.getTmnCode();
            String vnp_IpAddr = VnpayConfig.getIpAddress(request);

            // 3. Sinh URL thanh toán
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", VnpayConfig.vnp_Version);
            vnp_Params.put("vnp_Command", VnpayConfig.vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnpayProperties.getTmnCode());
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_OrderInfo", "Nap tien goi " + membershipPackage.getPackageName());
            vnp_Params.put("vnp_OrderType", "other");
            vnp_Params.put("vnp_BankCode", "VNBANK"); // hoặc bỏ nếu không chọn sẵn ngân hàng
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", vnpayProperties.getReturnUrl() + "?memberId=" + memberId + "&packageId=" + packageId);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr); // Thay thế bằng IP thực tế nếu cần

//            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

            ZoneId vnZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

            String vnp_CreateDate = LocalDateTime.now(vnZoneId).format(formatter);
            String vnp_ExpireDate = LocalDateTime.now(vnZoneId).plusMinutes(15).format(formatter);

            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

//            String vnp_CreateDate = formatter.format(cld.getTime());
//            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//
//            cld.add(Calendar.MINUTE, 15);
//            String vnp_ExpireDate = formatter.format(cld.getTime());
//            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

            // Build query and hash
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = vnp_Params.get(fieldName);

                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));

                if (i != fieldNames.size() - 1) {
                    hashData.append('&');
                    query.append('&');
                }
            }

            String vnp_SecureHash = VnpayConfig.hmacSHA512(vnpayProperties.getHashSecret(), hashData.toString());
            query.append("&vnp_SecureHash=").append(vnp_SecureHash);

            String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + query;
            response.setMessage("Payment URL generated successfully");
            response.setStatusCode(200);
            response.setToken(paymentUrl);
            System.out.println("✅ vnp_CreateDate: " + vnp_CreateDate);
            System.out.println("✅ vnp_ExpireDate: " + vnp_ExpireDate);
            System.out.println("✅ vnp_SecureHash: " + vnp_SecureHash);
            System.out.println("✅ paymentUrl: " + paymentUrl);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public boolean checkMembershipPackage(Long memberId) {
        try {
            Optional<Member> memberOpt = memberRepository.findById(memberId);
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found");
            }
            Member member = memberOpt.get();
            return member.getMembership_Package() != null && member.getMembership_Package().getStatus() == Status.ACTIVE;
        } catch (OurException e) {
            // Handle exception if needed
            return false;
        } catch (Exception e) {
            // Handle unexpected exceptions
            return false;
        }
    }

    @Override
    public Response getMembershipPackageByMemberId(Long memberId) {
        Response response = new Response();
        try {
            Optional<Member> memberOpt = memberRepository.findById(memberId);
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found with ID: " + memberId);
            }
            Member member = memberOpt.get();
            if (member.getMembership_Package() == null || member.getMembership_Package().getStatus() != Status.ACTIVE) {
                throw new OurException("Member does not have an active membership package");
            }
            MembershipPackage membershipPackage = member.getMembership_Package();
            response.setStatusCode(200);
            response.setMessage("Membership package retrieved successfully");
            response.setMembership_Package(Converter.convertMemberShipPackageDTO(membershipPackage));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }
}
