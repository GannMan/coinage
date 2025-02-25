package controller.action;

import actor.Actor;
import game.Direction;

/**
 * Actors perform turns to change their facing direction. Actors can only turn one direction
 * grade at a time, but attempts to turn more than one grade will automatically repeat until the
 * target is reached. Passing {@code true} to {@code moveAfterTurning} will cause the complete
 * turning chain to be finalized with a movement in the given direction.
 */
public class Turning extends Action {


  protected final Direction turningTowards;

  public Turning(Actor actor, Direction turningTowards) {
    super(actor, null);
    this.turningTowards = turningTowards;
  }



  /**
   * Turning cannot currently fail.
   */
  @Override
  protected boolean validate() {
    return true;
  }


  /**
   * Turn the actor one direction grade towards the target direction.
   */
  @Override
  protected void apply() {

    final Direction actorFacing = getPerformer().getFacing();

    final int difference = actorFacing.ordinal() - turningTowards.ordinal();

    // Evaluate whether turning left or right will get there faster.
    if ((difference > 0 && difference <= 4) || difference < -4) {
      getPerformer().setFacing(actorFacing.getLeftNeighbor());
    } else {
      getPerformer().setFacing(actorFacing.getRightNeighbor());
    }

  }


  /**
   * Continue turning until the target direction is reached. Cancelling repeat does not interrupt
   * the turn, and if we were to attempt a move after the turn, we will still always perform one
   * movement in the target direction. The cancellation applies to any future moves.
   *
   * @return The next action, or null if none should follow.
   */
  @Override
  public Action attemptRepeat() {

    final boolean targetDirectionReached = getPerformer().getFacing() == turningTowards;

    if (targetDirectionReached) {
      return null;
    } else {

      final Turning next = new Turning(getPerformer(), turningTowards);

      if (hasFlag(ActionFlag.DO_NOT_REPEAT)) {
        next.doNotRepeat(); // Pass repeat cancellation along the chain, if there is one.
      }

      return next;

    }

  }


}