package com.example.elevator_system.service;

import com.example.elevator_system.model.Direction;
import com.example.elevator_system.model.Elevator;
import com.example.elevator_system.model.StopRequest;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElevatorService {

    private final List<Elevator> elevators;
    private final ElevatorScheduler scheduler;

    public ElevatorService(ElevatorScheduler scheduler) {
        this.scheduler = scheduler;

        // These starting floors make it easy to try the simulation.
        this.elevators = List.of(
                new Elevator(1, 0),
                new Elevator(2, 4),
                new Elevator(3, 9));
    }

    public synchronized boolean requestElevator(
            int floor, Direction direction) {

        if (!Elevator.isValidFloor(floor) || direction == null) {
            return false;
        }

        if (floor == Elevator.MAX_FLOOR
                && direction == Direction.UP) {
            return false;
        }

        if (floor == Elevator.MIN_FLOOR
                && direction == Direction.DOWN) {
            return false;
        }

        Elevator elevator = scheduler.selectElevator(
                elevators, floor, direction);

        if (elevator == null) {
            return false;
        }

        return elevator.addStop(StopRequest.hall(floor, direction));
    }

    public synchronized boolean selectDestination(
            int elevatorId, int floor) {

        if (!Elevator.isValidFloor(floor)) {
            return false;
        }

        Elevator elevator = findElevator(elevatorId);

        if (elevator == null) {
            return false;
        }

        return elevator.addStop(StopRequest.destination(floor));
    }

    public synchronized void step() {
        for (Elevator elevator : elevators) {
            elevator.moveOneStep();
        }
    }

    public synchronized List<Elevator> getElevators() {
        return List.copyOf(elevators);
    }

    private Elevator findElevator(int id) {
        for (Elevator elevator : elevators) {
            if (elevator.getId() == id) {
                return elevator;
            }
        }

        return null;
    }
}