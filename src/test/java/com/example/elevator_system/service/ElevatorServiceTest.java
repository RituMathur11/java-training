package com.example.elevator_system.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.elevator_system.model.Direction;

class ElevatorServiceTest {

    private final ElevatorService elevatorService =
            new ElevatorService(new ElevatorScheduler());

    @Test
    void hallCallsCannotPointOutsideTheBuilding() {
        assertFalse(elevatorService.requestElevator(0, Direction.DOWN));
        assertFalse(elevatorService.requestElevator(9, Direction.UP));
    }

    @Test
    void destinationMustBelongToAnExistingElevatorAndFloor() {
        assertFalse(elevatorService.selectDestination(99, 4));
        assertFalse(elevatorService.selectDestination(1, 10));
        assertTrue(elevatorService.selectDestination(1, 4));
    }
}
