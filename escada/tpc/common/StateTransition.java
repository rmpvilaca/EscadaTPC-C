package escada.tpc.common;

/**
* It is used as a state transition manager, which means that
* it controls from the current step which is the next step
* to be executed during the simulation.
* 
**/
public abstract class StateTransition {
  protected StateObject curTrans = null;
  protected int curState = 0;
  protected int oldState = 0;

  /**
  * It loads the states that compose the transitions of the simulation.
  * Using an objetct, the user can define the appropriate class acording
  * to its needs and problems.
  *
  * @param Object used to load the simulation's state.
  **/
  abstract public void loadStates(Object obj);

  /**
  * It returns the simulation's current state. Using an object,
  * we can define the sates according to our needs and problems. 
  * The current implementation uses the class StateObject in order to
  * define a simulation's state.
  *
  * @return the simulation's state
  * @see getCurrentState
  **/
  public StateObject getCurrentStateObject() {
    return (curTrans);
  }

  /**
  * It returns the number of the simulation's current state.
  *
  * @return the number of the simulation's current state
  **/
  public int getCurrentState() {
    return (curState);
  }

  /**
  * It returns the number last simulation's state.
  *
  * @return the number of the simulation's current state
  **/
  public int getOldState() {
    return (oldState);
  }

  /**
  * It returns the next simulation's state.
  *
  * @return the next simulation's state
  **/
  abstract public StateObject nextState();
}
// arch-tag: 61f8f74e-e1b2-435f-8ea8-90f7e3e5f269

