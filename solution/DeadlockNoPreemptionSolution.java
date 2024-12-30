package org.deadlock.solution;

import org.deadlock.database.DBConnectionManager;
import org.deadlock.interfaces.StrategyExecutor;

import java.sql.Connection;
import java.sql.Statement;

public class DeadlockNoPreemptionSolution implements StrategyExecutor {
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
                    if (!tryLock(stmt1, "SELECT * FROM account WHERE id = 2 FOR UPDATE")) {
                        System.out.println("Thread 1: Unable to lock account 2, rolling back...");
                        conn1.rollback();
                        return;
                    }

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
                    if (!tryLock(stmt2, "SELECT * FROM account WHERE id = 1 FOR UPDATE")) {
                        System.out.println("Thread 2: Unable to lock account 1, rolling back...");
                        conn2.rollback();
                        return;
                    }

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

    private boolean tryLock(Statement stmt, String query) {
        try {
            stmt.executeQuery(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
