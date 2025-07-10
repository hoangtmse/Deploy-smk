package com.swd.smk.controller;

import com.swd.smk.dto.Response;
import com.swd.smk.dto.TransactionDTO;
import com.swd.smk.model.Transaction;
import com.swd.smk.services.interfac.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    ITransactionService transactionService;

    @PostMapping("/admin/update-transaction/transaction/{id}")
    public ResponseEntity<Response> updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO) {
        Response response = transactionService.updateTransaction(id, transactionDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/admin/delete-transaction/{id}")
    public ResponseEntity<Response> deleteTransaction(@PathVariable Long id) {
        Response response = transactionService.deleteTransaction(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/get-transaction/{id}")
    public ResponseEntity<Response> getTransactionById(@PathVariable Long id) {
        Response response = transactionService.getTransactionById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/admin/get-all-transactions")
    public ResponseEntity<Response> getAllTransactions() {
        Response response = transactionService.getAllTransactions();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/user/get-transactions-by-member/{memberId}")
    public ResponseEntity<Response> getTransactionsByMemberId(@PathVariable Long memberId) {
        Response response = transactionService.getTransactionsByMemberId(memberId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
