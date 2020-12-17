package com.ro.orders.service;

import com.ro.auth.data.model.User;
import com.ro.auth.data.repository.UserRepository;
import com.ro.orders.data.model.BonusesTransaction;
import com.ro.orders.data.model.Order;
import com.ro.orders.data.repository.BonusesTransactionRepository;
import com.ro.orders.exception.BonusesTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BonusesTransactionService {
    public BonusesTransaction addIncome(Order order, User user, int amount) {
        return makeTransaction(order, user, amount);
    }

    public BonusesTransaction addOutcome(Order order, User user, int amount) {
        if (user.getBonusesBalance() < amount) {
            throw new BonusesTransactionException("Недостаточно бонусов для списания");
        }
        return makeTransaction(order, user, -amount);
    }

    private BonusesTransaction makeTransaction(Order order, User user, int amount) {
        BonusesTransaction bonusesTransaction = new BonusesTransaction();
        bonusesTransaction.setUser(user);
        bonusesTransaction.setAmount(amount);
        bonusesTransaction.setOrder(order);

        return bonusesTransaction;
    }
}
