package org.lock;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@SpringBootApplication
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        String url = "jdbc:mysql://localhost:3306/test2";
        String user = "root";
        String password = "";

        try (Connection conn1 = DriverManager.getConnection(url, user, password);
             Connection conn2 = DriverManager.getConnection(url, user, password)) {

            conn1.setAutoCommit(false);
            conn2.setAutoCommit(false);


            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try (Statement stmt1 = conn1.createStatement()) {
                        stmt1.executeQuery("SELECT money FROM account WHERE id = 1 FOR UPDATE");
                        System.out.println("트랜잭션1 lock");

                        Thread.sleep(1000);

                        System.out.println("Transaction 1 trying to lock id = 2...");
                        stmt1.executeQuery("SELECT money FROM account WHERE id = 2 FOR UPDATE");

                        conn1.commit();
                        System.out.println("Transaction 1 committed");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try (Statement stmt2 = conn2.createStatement()) {
                        stmt2.executeQuery("SELECT money FROM account WHERE id = 2 FOR UPDATE");
                        System.out.println("Transaction 2 locked id = 2");

                        Thread.sleep(1000);

                        System.out.println("Transaction 2 trying to lock id = 1...");
                        stmt2.executeQuery("SELECT money FROM account WHERE id = 1 FOR UPDATE");

                        conn2.commit();
                        System.out.println("Transaction 2 committed");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            ////////////

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });

            thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}