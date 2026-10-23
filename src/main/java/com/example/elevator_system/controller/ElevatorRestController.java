package com.example.elevator_system.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.elevator_system.model.Direction;
import com.example.elevator_system.model.Elevator;
import com.example.elevator_system.model.StopRequest;
import com.example.elevator_system.service.ElevatorService;

@RestController
@RequestMapping("/api/elevators")
public class ElevatorRestController {
    private final ElevatorService elevatorService;

    public ElevatorRestController(
            ElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }

    public record HallCallRequest(int floor, Direction direction) {
    }

    public record DestinationRequest(int floor) {
    }

    public record ElevatorStatus(
            int id,
            int currentFloor,
            Direction direction,
            List<StopRequest> pendingStops) {
    }

    @PostMapping("/request")
    public ResponseEntity<Boolean> requestElevator(
            @RequestBody HallCallRequest request) {

        if (request == null || request.direction() == null) {
            return ResponseEntity.badRequest().body(false);
        }

        boolean accepted = elevatorService.requestElevator(
                request.floor(), request.direction());

        if (!accepted) {
            return ResponseEntity.badRequest().body(false);
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping("/{id}/destinations")
    public ResponseEntity<Boolean> selectDestination(
            @PathVariable int id,
            @RequestBody DestinationRequest request) {

        if (request == null) {
            return ResponseEntity.badRequest().body(false);
        }

        boolean accepted = elevatorService.selectDestination(
                id, request.floor());

        if (!accepted) {
            return ResponseEntity.badRequest().body(false);
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping("/step")
    public ResponseEntity<Void> step() {
        elevatorService.step();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<ElevatorStatus> getStatus() {
        List<ElevatorStatus> elevatorStatuses = new ArrayList<>();

        for (Elevator elevator : elevatorService.getElevators()) {
            elevatorStatuses.add(new ElevatorStatus(
                    elevator.getId(),
                    elevator.getCurrentFloor(),
                    elevator.getDirection(),
                    List.copyOf(elevator.getStops())));
        }

        return elevatorStatuses;
    }
}
