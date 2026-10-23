package com.example.elevator_system.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Elevator {

    public static final int MAX_FLOOR = 9;
    public static final int MIN_FLOOR = 0;

    private final int id;
    private int currentFloor;
    private Direction direction = Direction.IDLE;

    private final Set<StopRequest> stops = new HashSet<>();

    public Elevator(int id, int initialFloor) {
        if (!isValidFloor(initialFloor)) {
            throw new IllegalArgumentException(
                    "Initial floor must be between "
                            + MIN_FLOOR + " and " + MAX_FLOOR);
        }
        this.id = id;
        this.currentFloor = initialFloor;
    }

    public static boolean isValidFloor(int floor) {
        return floor >= MIN_FLOOR && floor <= MAX_FLOOR;
    }

    public synchronized boolean addStop(StopRequest request) {
        if (request == null || !isValidFloor(request.floor())) {
            return false;
        }

        // The elevator is already at this floor, so no stop is needed.
        if (request.floor() == currentFloor) {
            return true;
        }

        stops.add(request);
        return true;
    }

    public synchronized void moveOneStep() {
        removeCurrentFloorRequests();

        if (stops.isEmpty()) {
            direction = Direction.IDLE;
            return;
        }

        if (direction == Direction.IDLE) {
            setDirectionTowardNearestStop();
        }

        if (!hasStopsAhead()) {
            reverseDirection();
        }

        currentFloor += direction == Direction.UP ? 1 : -1;
        removeCurrentFloorRequests();

        if (stops.isEmpty()) {
            direction = Direction.IDLE;
        } else if (!hasStopsAhead()) {
            reverseDirection();
        }
    }

    private void setDirectionTowardNearestStop() {
        int nearestFloor = currentFloor;
        int shortestDistance = Integer.MAX_VALUE;

        for (StopRequest stop : stops) {
            int distance = Math.abs(stop.floor() - currentFloor);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                nearestFloor = stop.floor();
            }
        }

        if (nearestFloor > currentFloor) {
            direction = Direction.UP;
        } else if (nearestFloor < currentFloor) {
            direction = Direction.DOWN;
        }
    }

    private void reverseDirection() {
        if (direction == Direction.UP) {
            direction = Direction.DOWN;
        } else {
            direction = Direction.UP;
        }
    }

    private void removeCurrentFloorRequests() {
        stops.removeIf(stop -> stop.floor() == currentFloor);
    }

    private boolean hasStopsAhead() {
        for (StopRequest stop : stops) {
            if (direction == Direction.UP && stop.floor() > currentFloor) {
                return true;
            }
            if (direction == Direction.DOWN && stop.floor() < currentFloor) {
                return true;
            }
        }

        return false;
    }

    public int getId() {
        return id;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public Direction getDirection() {
        return direction;
    }

    public synchronized Set<StopRequest> getStops() {
        return Collections.unmodifiableSet(new HashSet<>(stops));
    }

    public synchronized int distanceTo(int floor) {
        return Math.abs(currentFloor - floor);
    }

    public synchronized boolean isIdle() {
        return stops.isEmpty();
    }
}
