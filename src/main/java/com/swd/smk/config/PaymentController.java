package com.swd.smk.config;

import com.swd.smk.dto.Response;
import com.swd.smk.dto.TransactionStatusDTO;
import com.swd.smk.enums.Status;
import com.swd.smk.model.Member;
import com.swd.smk.model.MembershipPackage;
import com.swd.smk.model.Notification;
import com.swd.smk.model.Transaction;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.repository.MembershipPackageRepository;
import com.swd.smk.repository.NotificationRepository;
import com.swd.smk.repository.TransactionRepository;
import com.swd.smk.services.interfac.ITransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/public/payment")
public class PaymentController {

    @Autowired
    private VnpayConfig vnpayConfig;

    @Autowired
    MembershipPackageRepository membershipPackageRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    VnpayProperties vnpayProperties;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ITransactionService transactionService;

    @Autowired
    NotificationRepository notificationRepository;

    @GetMapping("/create-payment")
    public ResponseEntity<?> createPayment(HttpServletRequest request) {
        long amount = 1000000; // 10,000 VND

        String vnp_TxnRef = VnpayConfig.getRandomNumber(8);
        String vnp_TmnCode = vnpayProperties.getTmnCode();
        String vnp_IpAddr = VnpayConfig.getIpAddress(request);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VnpayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VnpayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", "VNBANK"); // hoặc bỏ nếu không chọn sẵn ngân hàng
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Nap tien don hang " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "other"); // BẮT BUỘC
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnpayProperties.getReturnUrl()); // BẮT BUỘC
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

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

        Response response = new Response();
        response.setMessage("Payment URL generated successfully");
        response.setStatusCode(200);
        response.setToken(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/vnpay-return")
    public ResponseEntity<?> transaction(
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "packageId", required = false) Long packageId,
            @RequestParam(value = "vnp_Amount", required = false) String amount,
            @RequestParam(value = "vnp_BankCode", required = false) String bankCode,
            @RequestParam(value = "vnp_OrderInfo", required = false) String orderInfo,
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode) {

        TransactionStatusDTO transactionStatus = new TransactionStatusDTO();
        Double amountExchange =  Double.parseDouble(amount) / 100.0; // Chuyển đổi từ đồng sang tiền tệ

        Transaction transaction =  transactionService.createTransaction(memberId, packageId, orderInfo, bankCode, amountExchange, responseCode);
        if (transaction == null) {
            transactionStatus.setMessage("Transaction creation failed");
            transactionStatus.setStatus("FAILED");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(transactionStatus);
        }
        transactionStatus.setMessage("Transaction created successfully");
        transactionStatus.setStatus("COMPLETED");

        // Create a notification for the transaction
        Notification notification = new Notification();
        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (memberOpt.isPresent()) {
            notification.setMember(memberOpt.get());
            notification.setStatus(Status.ACTIVE);
            notification.setTitle("Transaction Successful");
            notification.setMessage("Your transaction with ID " + transaction.getId() + " has been completed successfully.");
            notification.setSentDate(LocalDateTime.now());
            notificationRepository.save(notification);
        }

        return ResponseEntity.status(HttpStatus.OK).body(transactionStatus);
    }


}
