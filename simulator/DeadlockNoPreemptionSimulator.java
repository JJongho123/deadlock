package org.deadlock.simulator;

import org.deadlock.database.DBConnectionManager;
import org.deadlock.interfaces.StrategyExecutor;

import java.sql.Connection;
import java.sql.Statement;

public class DeadlockNoPreemptionSimulator implements StrategyExecutor {

    @Override
    public void executeStrategy() {
        try (Connection conn1 = DBConnectionManager.getConnection();
             Connection conn2 = DBConnectionManager.getConnection()) {

            conn1.setAutoCommit(false);
            conn2.setAutoCommit(false);

            Thread t1 = new Thread(() -> {
                try (Statement stmt1 = conn1.createStatement()) {
                    System.out.println("Thread 1: Locking account 1");
                    stmt1.executeQuery("SELECT * FROM account WHERE id = 1 FOR UPDATE");

                    Thread.sleep(10);

                    System.out.println("Thread 1: Trying to lock account 2");
                    stmt1.executeQuery("SELECT * FROM account WHERE id = 2 FOR UPDATE");

                    conn1.commit();
                    System.out.println("Thread 1: Transaction committed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread t2 = new Thread(() -> {
                try (Statement stmt2 = conn2.createStatement()) {
                    System.out.println("Thread 2: Locking account 2");
                    stmt2.executeQuery("SELECT * FROM account WHERE id = 2 FOR UPDATE");

                    Thread.sleep(10);

                    System.out.println("Thread 2: Trying to lock account 1");
                    stmt2.executeQuery("SELECT * FROM account WHERE id = 1 FOR UPDATE");

                    conn2.commit();
                    System.out.println("Thread 2: Transaction committed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            t1.start();
            t2.start();

            t1.join();
            t2.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
