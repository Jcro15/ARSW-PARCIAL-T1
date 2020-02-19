package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.AccountException;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class MoneyLaunderingController {
    @Autowired
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping(value = "/fraud-bank-accounts", method = GET)
    public ResponseEntity<?> offendingAccounts() {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getSuspectAccounts(), HttpStatus.ACCEPTED);
        } catch (AccountException e) {
            return new ResponseEntity<>("ERROR 500", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/fraud-bank-accounts/{accountId}", method = GET)
    public ResponseEntity<?> offendingAccounts(@PathVariable("accountId") String accountId) {
        try {
            return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(accountId), HttpStatus.ACCEPTED);
        } catch (AccountException e) {
            return new ResponseEntity<>("ERROR 404", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/fraud-bank-accounts", method = POST)
    @ResponseBody
    public ResponseEntity<?> registerAccount(@RequestBody SuspectAccount suspectAccount) {
        try {
            moneyLaunderingService.createAccount(suspectAccount);
            return new ResponseEntity<>( HttpStatus.ACCEPTED);
        } catch (AccountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
        }
    }

    @RequestMapping(value = "/fraud-bank-accounts/{accountId}", method = PUT)
    @ResponseBody
    public ResponseEntity<?> updateAccount(@RequestBody SuspectAccount suspectAccount) {
        try {
            moneyLaunderingService.updateAccountStatus(suspectAccount);
            return new ResponseEntity<>( HttpStatus.ACCEPTED);
        } catch (AccountException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }






}