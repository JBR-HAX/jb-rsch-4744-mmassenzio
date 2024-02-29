package org.jetbrains.assignment.model;

import java.util.ArrayList;
import java.util.List;

public record Location(int x, int y) {

  public Location move(Movement move) {
    return switch (move.direction()) {
      case EAST -> new Location(x + move.steps(), y);
      case WEST -> new Location(x - move.steps(), y);
      case NORTH -> new Location(x, y + move.steps());
      case SOUTH -> new Location(x, y - move.steps());
    };
  }

  public List<Movement> moveTo(Location newLocation) {
    List<Movement> moves = new ArrayList<>();
    // Movements are only horizontal/vertical, so we may need up to two, if
    // the new location is diagonal to the current position.
    // We arbitrarily choose to move horizontally first:
    Direction dir;
    if (newLocation.x != x) {
      if (newLocation.x > x) {
        dir = Direction.EAST;
      } else {
        dir = Direction.WEST;
      }
      moves.add(new Movement(dir, Math.abs(newLocation.x - x)));
    }
    if (newLocation.y != y) {
      if (newLocation.y > y) {
        dir = Direction.NORTH;
      } else {
        dir = Direction.SOUTH;
      }
      moves.add(new Movement(dir, Math.abs(newLocation.y - y)));
    }
    return moves;
  }
}
