package org.deadlock.solution;

import org.deadlock.database.DBConnectionManager;
import org.deadlock.interfaces.StrategyExecutor;

import java.sql.Connection;
import java.sql.Statement;

public class DeadlockHoldAndWaitSolution implements StrategyExecutor {
    @Override
    public void executeStrategy() {
        try (Connection conn1 = DBConnectionManager.getConnection();
             Connection conn2 = DBConnectionManager.getConnection()) {

            conn1.setAutoCommit(false);
            conn2.setAutoCommit(false);

            Thread t1 = new Thread(() -> {
                try (Statement stmt1 = conn1.createStatement()) {
                    stmt1.executeQuery("SELECT money FROM account WHERE id IN (1, 2) FOR UPDATE");
                    Thread.sleep(5000);
                    conn1.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread t2 = new Thread(() -> {
                try (Statement stmt2 = conn2.createStatement()) {
                    stmt2.executeQuery("SELECT money FROM account WHERE id IN (1, 2) FOR UPDATE");
                    Thread.sleep(5000);
                    conn2.commit();
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
