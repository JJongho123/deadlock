package org.deadlock;

import org.deadlock.enums.DeadlockCondition;
import org.deadlock.enums.ExecuteType;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // 데드락 시뮬레이터 실행
        StrategyContext simulatorContext = new StrategyContext(DeadlockCondition.HOLD_AND_WAIT, ExecuteType.SIMULATOR);
        simulatorContext.executeStrategy();

        // 솔루션 실행
        StrategyContext solutionContext = new StrategyContext(DeadlockCondition.CIRCULAR_WAIT, ExecuteType.SOLUTION);
        solutionContext.executeStrategy();


    }

}