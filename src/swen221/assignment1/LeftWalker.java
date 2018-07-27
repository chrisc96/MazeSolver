package swen221.assignment1;

import java.util.ArrayList;
import java.util.List;

import maze.*;

/**
 * An implementation of the left walker, which you need to complete according to
 * the specification given in the assignment handout.
 * 
 */
public class LeftWalker extends Walker {

	// Initial position faces north
	private Direction currentDirection = Direction.NORTH;

	boolean init = true;

	// Memorisation fields
	List<boardSquare> visitedPos = new ArrayList<>();
	// Set initially to arbitrary 0,0. Doesn't matter that we don't know exact location
	// as all locations are relative to this
	boardSquare currentSquare = new boardSquare(0,0);

	public LeftWalker() {
		super("Left Walker");
	}

	@Override
	protected Direction move(View v) {
		pause(200);

		System.out.println(currentSquare.getX() + " " + currentSquare.getY());

		if (init) {
			// Upon initialisation and instances of squares already been visited
			// spins clockwise until left wall found, otherwise just goes north and repeats
			return findLeftWall(v);
		}
		else {
			// Follows the left wall

			if (canTurnLeft(v)) {
				turnLeft();
			}
			else {
				// While you cannot move in the current direction
				while (blockAhead(v)) {
					if (canTurnLeft(v)) {
						turnLeft();
						break;
					}
					else clockWiseRotate();
				}
			}

			// Removes the current square that the walker is at if it has been visited already
			boardSquare toBeRemoved = null;
			for (boardSquare bs : visitedPos) {
				if (bs.equals(currentSquare) && (this.currentDirection == bs.getExitDir())) {
					toBeRemoved = bs;
					// Require this rotation to avoid looping back again
					clockWiseRotate();
					init = true;
				}
			}
			visitedPos.remove(toBeRemoved);

			setSquare(this.currentDirection);
			return this.currentDirection;
		}
	}

	/**
	 * Upon beginning the maze, we need to spin the walker around to calibrate it so that
	 * it finds a left wall to hang onto, if there is one, otherwise we move until we find one
	 * @param v View
	 * @return Direction
	 */
	public Direction findLeftWall(View v) {
		// loops until left wall found
		if (!hasAWallSurrounding(v)) {
			setSquare(Direction.NORTH);
			return Direction.NORTH;
		}
		else {
			while (canTurnLeft(v)) {
				clockWiseRotate();
			}
			init = false;
			setSquare(this.currentDirection);
			return currentDirection;
		}
	}

	/**
	 * Based upon the current direction, it checks whether the walker can turn left.
	 * If it can, it returns true, else, false.
	 * @param v View
	 * @return boolean
	 */
	public boolean canTurnLeft(View v) {
		switch(this.currentDirection){
			case NORTH:
				return v.mayMove(Direction.WEST);
			case SOUTH:
				return v.mayMove(Direction.EAST);
			case WEST:
				return v.mayMove(Direction.SOUTH);
			case EAST:
				return v.mayMove(Direction.NORTH);
			default: return false;
		}
	}

	/**
	 * If there are no adjoining walls at all, the walker moves north (one step) and keeps searching for a left wall.
	 * @param v View
	 * @return boolean
	 */
	public boolean hasAWallSurrounding(View v) {
		// If you can't move in any of these directions, there must be a wall there
		if (!v.mayMove(Direction.NORTH)) return true;
		if (!v.mayMove(Direction.SOUTH)) return true;
		if (!v.mayMove(Direction.EAST)) return true;
		if (!v.mayMove(Direction.WEST)) return true;
		return false;
	}

	/**
	 * Checks whether the walker can continue in the same direction as it is travelling or if it is blocked
	 * @param v View
	 * @return true
	 */
	public boolean blockAhead(View v) {
		return !v.mayMove(this.currentDirection);
	}

	/**
	 * Sets the current direction by getting the left turn of the current direction.
	 * I.E 	North would turn to West,
	 * 		East would turn to North
	 * 		Etc.
	 */
	public void turnLeft() {
		switch (this.currentDirection) {
			case NORTH:
				this.currentDirection = Direction.WEST;
				break;
			case EAST:
				this.currentDirection = Direction.NORTH;
				break;
			case SOUTH:
				this.currentDirection = Direction.EAST;
				break;
			case WEST:
				this.currentDirection = Direction.SOUTH;
				break;
		}
	}

	/**
	 * Sets the current direction by rotating clockwise based upon the current direction
	 */
	public void clockWiseRotate() {
		switch (this.currentDirection) {
			case NORTH:
				this.currentDirection = Direction.EAST;
				break;
			case SOUTH:
				this.currentDirection = Direction.WEST;
				break;
			case EAST:
				this.currentDirection = Direction.SOUTH;
				break;
			case WEST:
				this.currentDirection = Direction.NORTH;
				break;
		}
	}

	/**
	 * Sets the current square that the walker is on and based on the direction parameter passed,
	 * sets the current square's exit direction to that parameter.
	 * It is then added to the visited position list, or in the instance that the square has already been visited,
	 * it is re-added to the list with its new exit direction
	 * @param dir Direction
	 */
	public void setSquare(Direction dir) {
		currentSquare.setExitDir(dir);
		visitedPos.add(currentSquare);
		switch(dir){
			case NORTH:
				currentSquare = new boardSquare(currentSquare.getX(),currentSquare.getY()+1);
				break;
			case SOUTH:
				currentSquare = new boardSquare(currentSquare.getX(),currentSquare.getY()-1);
				break;
			case WEST:
				currentSquare = new boardSquare(currentSquare.getX()-1,currentSquare.getY());
				break;
			case EAST:
				currentSquare = new boardSquare(currentSquare.getX()+1,currentSquare.getY());
		}
	}

	private class boardSquare {
		public int x,y;
		public Direction exitDir;

		public boardSquare(int x, int y) {
			this.x = x;
			this.y = y;
		}

		// Check equality between two squares based on their x/y positions.
		public boolean equals(boardSquare other){
			return this.x==other.getX() && this.y==other.getY();
		}

		public Direction getExitDir() {
			return exitDir;
		}

		public void setExitDir(Direction exitDir) {
			this.exitDir = exitDir;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
}