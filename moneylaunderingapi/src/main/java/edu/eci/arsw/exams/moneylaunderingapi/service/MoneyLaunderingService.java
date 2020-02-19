package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;

public interface MoneyLaunderingService {
    void updateAccountStatus(SuspectAccount suspectAccount) throws AccountException;
    SuspectAccount getAccountStatus(String accountId) throws AccountException;
    List<SuspectAccount> getSuspectAccounts() throws AccountException;
    void createAccount(SuspectAccount suspectAccount)throws AccountException ;
}
