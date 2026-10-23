package com.example.elevator_system.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.elevator_system.model.Direction;
import com.example.elevator_system.model.Elevator;
import com.example.elevator_system.model.StopRequest;

class ElevatorSchedulerTest {

    @Test
    void choosesTheClosestIdleElevator() {
        Elevator closestElevator = new Elevator(1, 3);
        Elevator fartherElevator = new Elevator(2, 8);
        ElevatorScheduler scheduler = new ElevatorScheduler();

        Elevator selectedElevator = scheduler.selectElevator(
                List.of(fartherElevator, closestElevator),
                4,
                Direction.UP);

        assertEquals(closestElevator, selectedElevator);
    }

    @Test
    void prefersTheLowerIdWhenElevatorsHaveTheSameCost() {
        Elevator higherId = new Elevator(2, 3);
        Elevator lowerId = new Elevator(1, 5);
        ElevatorScheduler scheduler = new ElevatorScheduler();

        Elevator selectedElevator = scheduler.selectElevator(
                List.of(higherId, lowerId),
                4,
                Direction.UP);

        assertEquals(lowerId, selectedElevator);
    }

    @Test
    void addsCostForAnElevatorMovingAwayFromTheCaller() {
        Elevator movingAway = new Elevator(1, 2);
        movingAway.addStop(StopRequest.destination(8));
        movingAway.moveOneStep();
        Elevator idleAndFarther = new Elevator(2, 8);
        ElevatorScheduler scheduler = new ElevatorScheduler();

        Elevator selectedElevator = scheduler.selectElevator(
                List.of(movingAway, idleAndFarther),
                1,
                Direction.DOWN);

        assertEquals(idleAndFarther, selectedElevator);
    }
}
