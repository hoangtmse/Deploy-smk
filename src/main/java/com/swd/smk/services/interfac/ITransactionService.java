package com.swd.smk.services.interfac;

import com.swd.smk.dto.Response;
import com.swd.smk.dto.TransactionDTO;
import com.swd.smk.model.Transaction;

public interface ITransactionService {

    Transaction createTransaction(Long memberId, Long packageId, String orderInfo, String bankCode, Double amount, String responseCode);

    Response updateTransaction(Long transactionId, TransactionDTO transactionDetails);

    Response deleteTransaction(Long transactionId);

    Response getTransactionById(Long transactionId);

    Response getAllTransactions();

    Response getTransactionsByMemberId(Long memberId);
}
