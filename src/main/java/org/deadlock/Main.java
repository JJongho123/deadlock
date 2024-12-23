package org.deadlock;

import org.deadlock.deadlockenum.DeadlockCondition;
import org.deadlock.deadlockenum.DeadlockType;
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

        // 데드락 시뮬레이터 실행
        StrategyContext simulatorContext = new StrategyContext(DeadlockCondition.HOLD_AND_WAIT, DeadlockType.SIMULATOR);
        simulatorContext.executeStrategy();

        // 솔루션 실행
        StrategyContext solutionContext = new StrategyContext(DeadlockCondition.CIRCULAR_WAIT, DeadlockType.SOLUTION);
        solutionContext.executeStrategy();


    }

}