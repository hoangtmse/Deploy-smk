package com.swd.smk.services.impl;

import com.swd.smk.config.VnpayProperties;
import com.swd.smk.dto.Response;
import com.swd.smk.dto.TransactionDTO;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.model.MembershipPackage;
import com.swd.smk.model.Transaction;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.repository.MembershipPackageRepository;
import com.swd.smk.repository.TransactionRepository;
import com.swd.smk.services.interfac.ITransactionService;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService implements ITransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private VnpayProperties vnpayProperties;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembershipPackageRepository membershipPackageRepository;


    @Override
    public Transaction createTransaction(Long memberId, Long packageId, String orderInfo, String bankCode, Double amount, String responseCode) {

        try {
            // Ghi log thông tin giao dịch
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setBankCode(bankCode);
            transaction.setOrderInfo(orderInfo);
            transaction.setResponseCode(responseCode);
            transaction.setStatus(Status.PENDING);
            transaction.setDateCreated(LocalDateTime.now());

            Member member = memberRepository.findById(memberId).orElse(null);
            MembershipPackage membershipPackage = membershipPackageRepository.findById(packageId).orElse(null);

            if (responseCode != null && responseCode.equals("00")) {
                transaction.setStatus(Status.COMPLETED);
                if (member != null && membershipPackage != null) {
                    member.setMembership_Package(membershipPackage);
                    member.setJoinDate(LocalDate.now());
                    memberRepository.save(member);
                } else {
                    throw new OurException("Member or package not found");
                }
            } else {
                throw new OurException("Transaction failed: " + responseCode);
            }
            // Gán member và package vào transaction nếu có
            transaction.setMember(member);
            transaction.setMembershipPackage(membershipPackage);
            transactionRepository.save(transaction);
            return transaction;
        }
        catch (Exception e) {
            throw new OurException("Error creating transaction: " + e.getMessage());
        }
    }

    @Override
    public Response updateTransaction(Long transactionId, TransactionDTO transactionDetails) {
        Response response = new Response();
        try{
            Transaction existingTransaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new OurException("Transaction not found with ID: " + transactionId));
            // Cập nhật các trường cần thiết
            if (transactionDetails.getOrderInfo() != null) {
                existingTransaction.setOrderInfo(transactionDetails.getOrderInfo());
            }
            if (transactionDetails.getBankCode() != null) {
                existingTransaction.setBankCode(transactionDetails.getBankCode());
            }
            if (transactionDetails.getAmount() != null) {
                existingTransaction.setAmount(transactionDetails.getAmount());
            }
            if (transactionDetails.getResponseCode() != null) {
                existingTransaction.setResponseCode(transactionDetails.getResponseCode());
            }
            if (transactionDetails.getStatus() != null) {
                existingTransaction.setStatus(transactionDetails.getStatus());
            }
            existingTransaction.setDateCreated(LocalDateTime.now());
            transactionRepository.save(existingTransaction);
            response.setStatusCode(200);
            response.setMessage("Transaction updated successfully.");
            response.setTransaction(Converter.convertTransactionToDTO(existingTransaction));

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
    public Response deleteTransaction(Long transactionId) {
        Response response = new Response();
        try {
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new OurException("Transaction not found with ID: " + transactionId));
            transaction.setStatus(Status.DELETED);
            transactionRepository.save(transaction);
            Member member = transaction.getMember();
            if (member != null) {
                member.setMembership_Package(null); // Clear membership package
                member.setJoinDate(null); // Clear join date
                memberRepository.save(member);
            }
            response.setStatusCode(200);
            response.setMessage("Transaction deleted successfully.");
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
    public Response getTransactionById(Long transactionId) {
        Response response = new Response();
        try {
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new OurException("Transaction not found with ID: " + transactionId));
            response.setStatusCode(200);
            response.setMessage("Transaction retrieved successfully.");
            response.setTransaction(Converter.convertTransactionToDTO(transaction));
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
    public Response getAllTransactions() {
        Response response = new Response();
        try {
            List<Transaction> transactions = transactionRepository.findAll();
            if (!transactions.iterator().hasNext()) {
                throw new OurException("No transactions found.");
            }

            List<TransactionDTO> transactionDTOS = transactions.stream()
                    .map(Converter::convertTransactionToDTO)
                    .toList();
            response.setStatusCode(200);
            response.setMessage("Transactions retrieved successfully.");
            response.setTransactions(transactionDTOS);
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
    public Response getTransactionsByMemberId(Long memberId) {
    Response response = new Response();
        try {
            List<Transaction> transactions = transactionRepository.findByMemberId(memberId);
            if (transactions.isEmpty()) {
                throw new OurException("No transactions found for member with ID: " + memberId);
            }
            List<TransactionDTO> transactionDTOS = transactions.stream()
                    .map(Converter::convertTransactionToDTO)
                    .toList();
            response.setStatusCode(200);
            response.setMessage("Transactions retrieved successfully.");
            response.setTransactions(transactionDTOS);
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
