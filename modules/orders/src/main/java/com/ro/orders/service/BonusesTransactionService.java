package com.ro.orders.service;

import com.ro.auth.data.model.User;
import com.ro.auth.data.repository.UserRepository;
import com.ro.orders.data.model.BonusesTransaction;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.repository.BonusesTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BonusesTransactionService {
    private final BonusesTransactionRepository bonusesTransactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public BonusesTransactionService(BonusesTransactionRepository bonusesTransactionRepository,
                                     UserRepository userRepository) {
        this.bonusesTransactionRepository = bonusesTransactionRepository;
        this.userRepository = userRepository;
    }

    public BonusesTransaction addIncome(Order order, User user, int amount) {
        return makeTransaction(order, user, amount);
    }

    public BonusesTransaction addOutcome(Order order, User user, int amount) {
        return makeTransaction(order, user, -amount);
    }

    @Transactional
    private BonusesTransaction makeTransaction(Order order, User user, int amount) {
        BonusesTransaction bonusesTransaction = new BonusesTransaction();
        bonusesTransaction.setUser(user);
        bonusesTransaction.setAmount(amount);
        bonusesTransaction.setOrder(order);
        bonusesTransaction = bonusesTransactionRepository.save(bonusesTransaction);

        updateUserBonusesBalance(user, bonusesTransaction);

        return bonusesTransaction;
    }

    private void updateUserBonusesBalance(User user, BonusesTransaction bonusesTransaction) {
        user.setBonuses(user.getBonuses() + bonusesTransaction.getAmount());
        userRepository.updateBonuses(user.getId(), user.getBonuses());
    }
}
