package controller.action;

import actor.Actor;
import actor.attribute.Attribute;
import game.Direction;
import game.Game;

import java.awt.Color;

/**
 * Actors perform moves to change their own location in the world. Their movement speed is
 * defined by how long the delay to perform a movement is for them, which is derived from their
 * reflex attribute. This delay is magnified if the actor is not facing the direction in
 * which it is trying to move, albeit less so if the direction is immediately adjacent (i.e.
 * northwest and northeast for north).
 *
 * Passing {@code true} to {@code isWalking} will reduce movement to half speed.
 */
public class Moving extends Action {


  public final static int BASELINE_RANK = 5;
  public final static int BEATS_AT_BASELINE = 4;
  public final static int DISTANCE_ADJUSTMENT_DIVISOR = 2;




  private final Direction movingIn;
  private final boolean isWalking;

  public Moving(Actor actor, Direction movingIn, boolean isWalking) {
    super(actor,
        Game.getActiveWorld().offsetCoordinateBySquares(
            actor.getCoordinate(), movingIn.relativeX, movingIn.relativeY));
    this.movingIn = movingIn;
    this.isWalking = isWalking;
  }

  @Override
  public Color getIndicatorColor() {
    return Color.WHITE;
  }



  /**
   * At {@code BASELINE_RANK} reflex, an actor will suffer a {@code BEATS_AT_BASELINE} action
   * delay. For every {@code DISTANCE_ADJUSTMENT_DIVISOR} ranks above or below
   * {@code BASELINE_RANK}, the actor suffers one less or one more beat of action delay,
   * respectively.
   */
  @Override
  public int calcDelayToPerform() {

    // Take the actor's reflex rank.
    final int actorReflex = getPerformer().readAttributeLevel(Attribute.REFLEX).ordinal();

    // Determine distance from BASELINE_RANK.
    final int distanceFromBaseline = actorReflex - BASELINE_RANK;

    // Subtract adjustment from baseline to get normal delay to perform.
    final int calculatedDelay =
        BEATS_AT_BASELINE - (distanceFromBaseline / DISTANCE_ADJUSTMENT_DIVISOR);


    // Determine facing adjustment.
    if (isWalking) {
      return calculatedDelay * 2; // Walking always takes twice as long.
    }

    if (getIsMovingInFacedDirection()) {
      return calculatedDelay; // No adjustment necessary.
    }

    if (getIsMovingInAdjacentDirection()) {
      return (int) (calculatedDelay * 1.5); // Adjacent directions take 50% longer to move in.
    }

    else {
      return calculatedDelay * 2; // All other directions take twice as long to move in.
    }

  }

  private boolean getIsMovingInFacedDirection() {
    return getPerformer().getFacing() == movingIn;
  }

  private boolean getIsMovingInAdjacentDirection() {
    final Direction actorFacing = getPerformer().getFacing();
    return movingIn == actorFacing.getLeftNeighbor() || movingIn == actorFacing.getRightNeighbor();
  }



  /**
   * Moving will fail if the target coordinate is blocked or if the actor is somehow relocated
   * before completing the movement.
   */
  @Override
  protected boolean validate() {

    final boolean targetIsBlocked = getTarget() == null || getTarget().getSquare().isBlocked();
    final boolean actorHasRelocated = !getOrigin().getSquare().getAll().contains(getPerformer());

    return !targetIsBlocked && !actorHasRelocated;

  }


  /**
   * Remove the actor from its original location, add it to the new location, and update its stored
   * coordinate accordingly. If this move crosses area borders, it gains the {@code
   * ACTOR_CHANGED_AREA} flag.
   */
  @Override
  protected void apply() {

    getOrigin().getSquare().pull(getPerformer());
    getTarget().getSquare().put(getPerformer());
    getPerformer().setCoordinate(getTarget());

    if (getOrigin().area != getTarget().area) {
      addFlag(ActionFlag.ACTOR_CHANGED_AREA);
    }

  }


  /**
   * Movement will always repeat on success unless {@code doNotRepeat()} is called.
   */
  @Override
  public Action attemptRepeat() {
    if (hasFlag(ActionFlag.DO_NOT_REPEAT)) {
      return null;
    } else {
      return new Moving(getPerformer(), movingIn, isWalking);
    }
  }


}
