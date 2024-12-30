package org.deadlock.solution;

import org.deadlock.database.DBConnectionManager;
import org.deadlock.interfaces.StrategyExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DeadlockMutualExclusionSolution implements StrategyExecutor {
    @Override
    public void executeStrategy() {
        try (Connection conn1 = DBConnectionManager.getConnection();
             Connection conn2 = DBConnectionManager.getConnection()) {

            // 트랜잭션 수동 관리
            conn1.setAutoCommit(false);
            conn2.setAutoCommit(false);

            // Thread 1: 데이터 읽기
            Thread t1 = new Thread(() -> {
                try (Statement stmt1 = conn1.createStatement()) {
                    System.out.println("Thread 1: Reading account 1 balance");
                    ResultSet rs = stmt1.executeQuery("SELECT balance FROM account WHERE id = 1");
                    if (rs.next()) {
                        System.out.println("Thread 1: Balance = " + rs.getDouble("balance"));
                    }
                    conn1.commit();
                    System.out.println("Thread 1: Transaction committed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // Thread 2: 데이터 업데이트
            Thread t2 = new Thread(() -> {
                try (Statement stmt2 = conn2.createStatement()) {
                    System.out.println("Thread 2: Updating account 1 balance");
                    stmt2.executeUpdate("UPDATE account SET balance = balance + 100 WHERE id = 1");

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
