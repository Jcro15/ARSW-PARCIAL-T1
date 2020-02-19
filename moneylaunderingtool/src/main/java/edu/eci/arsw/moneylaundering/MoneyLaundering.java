package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering
{
    public static TransactionAnalyzer transactionAnalyzer;
    public static TransactionReader transactionReader;
    private int amountOfFilesTotal;
    public static AtomicInteger amountOfFilesProcessed;
    public static AtomicInteger activeThreads;
    public static Object monitor=new Object();
    public static boolean pause=false;
    public static AtomicInteger pausedThreads=new AtomicInteger(0);


    public MoneyLaundering()
    {
        transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        activeThreads=new AtomicInteger();
    }

    public void processTransactionData(int numberOfThreads)
    {
        amountOfFilesProcessed.set(0);
        List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        TransactionThread[] hilos=new TransactionThread[numberOfThreads];
        int start=0;
        int end=0;
        int step=amountOfFilesTotal/numberOfThreads;
        for (int i = 0; i <numberOfThreads ; i++) {
            end=start+step;
            if(i==0)end+=amountOfFilesTotal%numberOfThreads;
            hilos[i]=new TransactionThread(start,end,transactionFiles);
            hilos[i].start();
            System.out.println(start+" "+end);
            start=end;

        }

    }

    public List<String> getOffendingAccounts()
    {
        return transactionAnalyzer.listOffendingAccounts();
    }

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args) throws InterruptedException {
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        int numberOfThreads=5;
        activeThreads.set(numberOfThreads);
        moneyLaundering.processTransactionData(numberOfThreads);

        while(numberOfThreads>0)
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(pause){
                pause=false;
                System.out.println("running");
                pausedThreads.set(0);
                synchronized (monitor){
                    monitor.notifyAll();
                }
            }
            else{
                System.out.println("paused");
                pause=true;
                while(activeThreads.get()!=pausedThreads.get()){
                    Thread.sleep(1);
                }

                System.out.println("Found "+moneyLaundering.getOffendingAccounts().size()+" suspect accounts");

            }

            if(line.contains("exit"))
                break;

        }
        String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
        List<String> offendingAccounts = moneyLaundering.getOffendingAccounts();
        String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
        message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
        System.out.println(message);

    }


}
