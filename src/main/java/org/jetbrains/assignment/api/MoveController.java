package org.jetbrains.assignment.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.assignment.model.Location;
import org.jetbrains.assignment.model.Movement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(
    produces = "application/json",
    consumes = "application/json")

public class MoveController {

  @PostMapping("/locations")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<List<Location>> move(@RequestBody List<Movement> moves) {
    log.debug("Moving robot: {}", moves);
    List<Location> positions = new ArrayList<>();
    positions.add(new Location(0, 0));
    moves
        .forEach(move -> {
          Location curPos = positions.get(positions.size() - 1);
          Location newPos = curPos.move(move);
          positions.add(newPos);
        });
    return ResponseEntity.created(URI.create("test"))
        .body(positions);
  }

  @PostMapping("/moves")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<List<Movement>> visit(@RequestBody List<Location> locations) {
    log.debug("Making robot visit all locations: {}", locations);
    List<Movement> result = new ArrayList<>();
    if (locations.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    Location current = locations.get(0);
    for (Location loc : locations) {
      var moves = current.moveTo(loc);
      result.addAll(moves);
      current = loc;
    }
    return ResponseEntity.created(URI.create("test"))
        .body(result);
  }
}
