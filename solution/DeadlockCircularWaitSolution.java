package org.deadlock.solution;

import org.deadlock.database.DBConnectionManager;
import org.deadlock.interfaces.StrategyExecutor;

import java.sql.Connection;
import java.sql.Statement;

public class DeadlockCircularWaitSolution implements StrategyExecutor {
    @Override
    public void executeStrategy() {
        try (Connection conn1 = DBConnectionManager.getConnection();
             Connection conn2 = DBConnectionManager.getConnection()) {

            // 트랜잭션 수동 관리
            conn1.setAutoCommit(false);
            conn2.setAutoCommit(false);

            // Thread 1: 자원 1 -> 자원 2
            Thread t1 = new Thread(() -> {
                try (Statement stmt1 = conn1.createStatement()) {
                    System.out.println("Thread 1: Locking account 1");
                    stmt1.executeQuery("SELECT * FROM account WHERE id = 1 FOR UPDATE");

                    System.out.println("Thread 1: Locking account 2");
                    stmt1.executeQuery("SELECT * FROM account WHERE id = 2 FOR UPDATE");

                    conn1.commit();
                    System.out.println("Thread 1: Transaction committed");
                } catch (Exception e) {
                    try {
                        conn1.rollback();
                    } catch (Exception rollbackEx) {
                        rollbackEx.printStackTrace();
                    }
                    e.printStackTrace();
                }
            });

            // Thread 2: 자원 1 -> 자원 2 (순서 동일)
            Thread t2 = new Thread(() -> {
                try (Statement stmt2 = conn2.createStatement()) {
                    System.out.println("Thread 2: Locking account 1");
                    stmt2.executeQuery("SELECT * FROM account WHERE id = 1 FOR UPDATE");

                    System.out.println("Thread 2: Locking account 2");
                    stmt2.executeQuery("SELECT * FROM account WHERE id = 2 FOR UPDATE");

                    conn2.commit();
                    System.out.println("Thread 2: Transaction committed");
                } catch (Exception e) {
                    try {
                        conn2.rollback();
                    } catch (Exception rollbackEx) {
                        rollbackEx.printStackTrace();
                    }
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
