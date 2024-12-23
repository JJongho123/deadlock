package org.deadlock;

import org.deadlock.interfaces.StrategyExecutor;
import org.deadlock.enums.DeadlockCondition;
import org.deadlock.enums.ExecuteType;
import org.deadlock.factory.DeadlockFactory;


public class StrategyContext {

    private final StrategyExecutor strategy;

    public StrategyContext(DeadlockCondition condition, ExecuteType type) {
        this.strategy = DeadlockFactory.getStrategy(condition, type);
    }

    public void executeStrategy() {
        if (strategy != null) {
            strategy.executeStrategy();
        } else {
            System.out.println("strategy is empty !!! ");
        }
    }

}
