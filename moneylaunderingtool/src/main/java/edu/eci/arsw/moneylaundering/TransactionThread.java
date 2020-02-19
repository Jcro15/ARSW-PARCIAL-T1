package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.util.List;

public class TransactionThread extends Thread{
    int start;
    int end;
    List<File> transactionFiles;

    public TransactionThread(int start,int end, List<File> transactionFiles) {
    this.start=start;
    this.end=end;
    this.transactionFiles=transactionFiles;
    }

    @Override
    public void run(){
        for(int i=start;i<end;i++)
        {
            File transactionFile =transactionFiles.get(i);
            List<Transaction> transactions = MoneyLaundering.transactionReader.readTransactionsFromFile(transactionFile);
            for(Transaction transaction : transactions)
            {
                synchronized (MoneyLaundering.monitor){
                    if (MoneyLaundering.pause){
                        MoneyLaundering.pausedThreads.incrementAndGet();
                        try {
                            MoneyLaundering.monitor.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                MoneyLaundering.transactionAnalyzer.addTransaction(transaction);
            }
            MoneyLaundering.amountOfFilesProcessed.incrementAndGet();
        }
        MoneyLaundering.activeThreads.decrementAndGet();
    }
}
