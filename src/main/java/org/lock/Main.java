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

//            t1.start();
            t2.start();

//             t1.join();
             t2.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}