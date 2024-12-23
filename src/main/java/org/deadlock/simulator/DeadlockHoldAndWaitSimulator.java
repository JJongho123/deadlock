package org.deadlock.simulator;

import org.deadlock.database.DBConnectionManager;
import org.deadlock.deadlockInterface.Deadlock;

import java.sql.Connection;
import java.sql.Statement;

public class DeadlockHoldAndWaitSimulator implements Deadlock {
    @Override
    public void executeStrategy() {
        try (Connection conn1 = DBConnectionManager.getConnection();
             Connection conn2 = DBConnectionManager.getConnection()) {

            conn1.setAutoCommit(false);
            conn2.setAutoCommit(false);

            Thread t1 = new Thread(() -> {
                try (Statement stmt1 = conn1.createStatement()) {
                    stmt1.executeQuery("SELECT money FROM account WHERE id = 1 FOR UPDATE");
                    Thread.sleep(1000);
                    stmt1.executeQuery("SELECT money FROM account WHERE id = 2 FOR UPDATE");
                    conn1.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread t2 = new Thread(() -> {
                try (Statement stmt2 = conn2.createStatement()) {
                    stmt2.executeQuery("SELECT money FROM account WHERE id = 2 FOR UPDATE");
                    Thread.sleep(1000);
                    stmt2.executeQuery("SELECT money FROM account WHERE id = 1 FOR UPDATE");
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
