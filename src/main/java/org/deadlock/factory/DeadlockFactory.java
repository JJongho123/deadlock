package org.deadlock.factory;

import org.deadlock.interfaces.StrategyExecutor;
import org.deadlock.enums.DeadlockCondition;
import org.deadlock.enums.ExecuteType;
import org.deadlock.simulator.DeadlockCircularWaitSimulator;
import org.deadlock.simulator.DeadlockHoldAndWaitSimulator;
import org.deadlock.simulator.DeadlockMutualExclusionSimulator;
import org.deadlock.simulator.DeadlockNoPreemptionSimulator;
import org.deadlock.solution.DeadlockCircularWaitSolution;
import org.deadlock.solution.DeadlockHoldAndWaitSolution;
import org.deadlock.solution.DeadlockMutualExclusionSolution;
import org.deadlock.solution.DeadlockNoPreemptionSolution;

public class DeadlockFactory {

    public static StrategyExecutor getStrategy(DeadlockCondition condition, ExecuteType type) {
        if (type == ExecuteType.SIMULATOR) {
            return switch (condition) {

                case CIRCULAR_WAIT -> new DeadlockCircularWaitSimulator();
                case HOLD_AND_WAIT -> new DeadlockHoldAndWaitSimulator();
                case MUTUAL_EXCLUSION -> new DeadlockMutualExclusionSimulator();
                case NO_PREEMPTION -> new DeadlockNoPreemptionSimulator();
                default -> throw new IllegalArgumentException("Unknown DeadlockCondition: " + condition);

            };
        } else if (type == ExecuteType.SOLUTION) {
            return switch (condition) {

                case CIRCULAR_WAIT -> new DeadlockCircularWaitSolution();
                case HOLD_AND_WAIT -> new DeadlockHoldAndWaitSolution();
                case MUTUAL_EXCLUSION -> new DeadlockMutualExclusionSolution();
                case NO_PREEMPTION -> new DeadlockNoPreemptionSolution();
                default -> throw new IllegalArgumentException("Unknown DeadlockCondition: " + condition);

            };
        } else {
            throw new IllegalArgumentException("Unknown DeadlockType: " + type);
        }
    }
}
