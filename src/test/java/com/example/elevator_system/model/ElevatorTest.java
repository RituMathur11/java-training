package com.example.elevator_system.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ElevatorTest {

    @Test
    void elevatorCanTravelToARequestedFloor() {
        Elevator elevator = new Elevator(1, 2);
        elevator.addStop(StopRequest.destination(5));

        elevator.moveOneStep();
        assertEquals(3, elevator.getCurrentFloor());
        assertEquals(Direction.UP, elevator.getDirection());

        elevator.moveOneStep();
        elevator.moveOneStep();
        assertEquals(5, elevator.getCurrentFloor());
        assertEquals(Direction.IDLE, elevator.getDirection());
        assertTrue(elevator.getStops().isEmpty());
    }

    @Test
    void elevatorReversesAfterServingTheLastStopInItsDirection() {
        Elevator elevator = new Elevator(1, 2);
        elevator.addStop(StopRequest.destination(0));
        elevator.addStop(StopRequest.destination(3));

        elevator.moveOneStep();
        assertEquals(3, elevator.getCurrentFloor());
        assertEquals(Direction.DOWN, elevator.getDirection());

        elevator.moveOneStep();
        assertEquals(2, elevator.getCurrentFloor());
        assertEquals(Direction.DOWN, elevator.getDirection());
    }

    @Test
    void elevatorRejectsStopsOutsideTheBuilding() {
        Elevator elevator = new Elevator(1, 2);

        assertFalse(elevator.addStop(StopRequest.destination(10)));
        assertFalse(elevator.addStop(null));
    }

    @Test
    void elevatorRequiresAValidStartingFloor() {
        assertThrows(IllegalArgumentException.class, () -> new Elevator(1, -1));
        assertThrows(IllegalArgumentException.class, () -> new Elevator(1, 10));
    }
}
