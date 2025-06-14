package com.swd.smk.services.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MembershipPackageService implements IMembershipPackage {

    @Autowired
    MembershipPackageRepository membershipPackageRepository;

    @Autowired
    MemberRepository memberRepository;

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
        try{
            // Validate the input data

            // Check if the package name is provided
            MembershipPackage membershipPackage = new MembershipPackage();
            membershipPackage.setPackageName(memberShipPackageDTO.getPackageName());
            membershipPackage.setPrice(memberShipPackageDTO.getPrice());
            membershipPackage.setStatus(Status.ACTIVE);
            membershipPackage.setDescription(memberShipPackageDTO.getDescription());
            membershipPackage.setDateUpdated(LocalDate.now());
            membershipPackage.setDateCreated(LocalDate.now());
            membershipPackageRepository.save(membershipPackage);
            response.setStatusCode(200);
            response.setMessage("Membership package created successfully");
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
    public Response buyMembershipPackage(Long memberId, Long packageId) {
        Response response = new Response();
        try {
            // Kiểm tra member và package tồn tại
            Optional<MembershipPackage> packageOpt = membershipPackageRepository.findById(packageId);
            if (packageOpt.isEmpty()) {
                throw new OurException("Membership package not found");
            }
            Optional<Member> memberOpt = memberRepository.findById(memberId);
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found");
            }
            // Thực hiện logic mua gói (tạo bản ghi purchase, cập nhật trạng thái, ...)
            MembershipPackage membershipPackage = packageOpt.get();
            Member member = memberOpt.get();
            // Giả sử chúng ta có một phương thức để xử lý việc mua gói
            member.setMembership_Package(membershipPackage);
            memberRepository.save(member);

            response.setStatusCode(200);
            response.setMessage("Membership package purchased successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
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


}
