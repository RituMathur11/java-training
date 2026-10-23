package com.example.elevator_system.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.elevator_system.model.Direction;
import com.example.elevator_system.model.Elevator;

@Component
public class ElevatorScheduler {
    public Elevator selectElevator(
            List<Elevator> elevators,
            int requestedFloor,
            Direction requestedDirection) {

        return elevators.stream()
                .min(Comparator
                        .comparingInt((Elevator e) -> estimateCost(e, requestedFloor,
                                requestedDirection))
                        .thenComparingInt(Elevator::getId))
                .orElse(null);
    }

    private int estimateCost(
            Elevator elevator,
            int requestedFloor,
            Direction requestedDirection) {

        int currentFloor = elevator.getCurrentFloor();
        Direction currentDirection = elevator.getDirection();
        int distance = elevator.distanceTo(requestedFloor);

        // A stationary elevator at the requested floor is ideal.
        if (currentFloor == requestedFloor) {
            return 0;
        }

        // An idle elevator can head directly to the caller.
        if (elevator.isIdle() || currentDirection == Direction.IDLE) {
            return distance;
        }

        boolean movingTowardCaller = currentDirection == Direction.UP
                ? requestedFloor > currentFloor
                : requestedFloor < currentFloor;

        boolean sameDirection = currentDirection == requestedDirection;

        if (movingTowardCaller && sameDirection) {
            return distance;
        }

        // Prefer elevators that can serve the call without
        // first reversing or travelling away from the caller.
        return distance + 10;
    }
}
