package org.deadlock;

import org.deadlock.deadlockInterface.Deadlock;
import org.deadlock.deadlockenum.DeadlockCondition;
import org.deadlock.deadlockenum.DeadlockType;
import org.deadlock.factory.DeadlockFactory;


public class StrategyContext {

    private final Deadlock strategy;

    public StrategyContext(DeadlockCondition condition, DeadlockType type) {
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
