package com.example.pasir_jurczak_michal.service;

import com.example.pasir_jurczak_michal.dto.DebtDTO;
import com.example.pasir_jurczak_michal.model.*;
import com.example.pasir_jurczak_michal.repository.DebtRepository;
import com.example.pasir_jurczak_michal.repository.GroupRepository;
import com.example.pasir_jurczak_michal.repository.TransactionRepository;
import com.example.pasir_jurczak_michal.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtService  {

    private final DebtRepository debtRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DebtService(DebtRepository debtRepository, GroupRepository groupRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        this.debtRepository = debtRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Debt> getGroupDebts(long groupId) {
        return debtRepository.findByGroupId(groupId);
    }

    public Debt createDebt(DebtDTO debtDTO) {
        Group group = groupRepository.findById(debtDTO.getGroupId())
                .orElseThrow(() -> new EntityNotFoundException("file znalezlong group o ID: " + debtDTO.getGroupId()));
        User debtor = userRepository.findById(debtDTO.getDebtorId())
                .orElseThrow(() -> new EntityNotFoundException("file znalezlong dluznika o ID: " + debtDTO.getDebtorId()));
        User creditor = userRepository.findById(debtDTO.getCreditorId())
                .orElseThrow(() -> new EntityNotFoundException("file znalezlong wierzyciela o ID: " + debtDTO.getCreditorId()));


        Debt debt = new Debt();
        debt.setGroup(group);
        debt.setDebtor(debtor);
        debt.setCreditor(creditor);
        debt.setAmount(debtDTO.getAmount());
        debt.setTitle(debtDTO.getTitle());

        return debtRepository.save(debt);
    }

    public void deleteDebt(long debtId, User currentUser) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new EntityNotFoundException("Dług o ID " + debtId + " nie istnieje."));

        if (!debt.getCreditor().getId().equals(currentUser.getId())) {
            throw new SecurityException("Tylko wierzyciel może usunąć ten dług.");
        }

        debtRepository.delete(debt);
    }

    public boolean markAsPaid(long debtId, User user) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono długu"));

        if (!debt.getDebtor().getId().equals(user.getId())) {
            throw new SecurityException("Nie jesteś dłużnikiem");
        }

        debt.setMarkedAsPaid(true);
        debtRepository.save(debt);
        return true;
    }


    public boolean confirmPayment(long debtId, User user) {  // 1 usage - new *
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono długu"));

        // Authorization check
        if (!debt.getCreditor().getId().equals(user.getId())) {
            throw new SecurityException("Nie jesteś wierzycielem");
        }

        // Validation check
        if (!debt.isMarkedAsPaid()) {
            throw new IllegalStateException("Dłużnik jeszcze nie oznaczył jako opłacone");
        }

        // Update debt status
        debt.setConfirmedByCreditor(true);
        debtRepository.save(debt);

        // Create creditor's income transaction
        Transaction incomeTx = new Transaction(
                debt.getAmount(),
                TransactionType.INCOME,
                "Spłata długu",  // tags
                "Spłata długu od: " + debt.getDebtor().getEmail(),  // notes
                debt.getCreditor()
        );
        transactionRepository.save(incomeTx);

        // Create debtor's expense transaction
        Transaction expenseTx = new Transaction(
                debt.getAmount(),
                TransactionType.EXPENSE,
                "Spłata długu",  // tags
                "Spłacono dług dla: " + debt.getCreditor().getEmail(),  // notes
                debt.getDebtor()
        );
        transactionRepository.save(expenseTx);

        return true;
    }
}
