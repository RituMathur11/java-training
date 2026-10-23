package com.example.elevator_system.model;

public record StopRequest(
        int floor,
        Direction direction,
        StopType type) {
    public static StopRequest hall(int floor, Direction direction) {
        return new StopRequest(floor, direction, StopType.HALL);
    }

    public static StopRequest destination(int floor) {
        return new StopRequest(floor, Direction.IDLE, StopType.DESTINATION);
    }
}
