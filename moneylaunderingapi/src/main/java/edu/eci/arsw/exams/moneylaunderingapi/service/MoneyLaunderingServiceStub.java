package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
@Service
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {

    List<SuspectAccount> cuentas;
    public MoneyLaunderingServiceStub() {
        cuentas=new CopyOnWriteArrayList<>();
        SuspectAccount a1=new SuspectAccount("a1",999);
        SuspectAccount a2=new SuspectAccount("a2",888);
        SuspectAccount a3=new SuspectAccount("a3",777);
        cuentas.add(a1);
        cuentas.add(a2);
        cuentas.add(a3);
    }

    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) throws AccountException {
        try{
            SuspectAccount cuenta=getAccountStatus(suspectAccount.getAccountId());
            cuenta.updateAmount(suspectAccount.getAmountOfSmallTransactions());
        } catch (AccountException e) {
            throw e;
        }
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) throws AccountException {
        for (SuspectAccount cuenta:cuentas) {
            if (cuenta.getAccountId().equals(accountId)) {
                return cuenta;
            }
        }
        throw new AccountException("Cuenta no encontrada");

    }

    @Override
    public List<SuspectAccount> getSuspectAccounts()throws AccountException {
        return cuentas;
    }

    @Override
    public void createAccount(SuspectAccount suspectAccount)throws AccountException {
        for (SuspectAccount cuenta:cuentas) {
            if (cuenta.getAccountId().equals(suspectAccount.getAccountId())) {
                throw new AccountException("cuenta ya creada");
            }
        }
        cuentas.add(suspectAccount);
    }
}
