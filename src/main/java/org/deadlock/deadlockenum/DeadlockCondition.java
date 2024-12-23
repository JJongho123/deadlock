package org.deadlock.deadlockenum;

public enum DeadlockCondition {
    MUTUAL_EXCLUSION,   // 상호 배제 조건
    HOLD_AND_WAIT,      // 점유 대기 조건
    NO_PREEMPTION,      // 비선점 조건
    CIRCULAR_WAIT       // 순환 대기 조건
}