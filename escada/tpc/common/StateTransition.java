/*
 * TPCC Client
 * Copyright (C) 2006 University of Minho
 * See http://gorda.di.uminho.pt/ for more information.
 *
 * Partially funded by the European Union Framework Programme for
 * Research and Technological Development, thematic priority
 * Information Society and Media, project GORDA (004758).
 * 
 * Contributors:
 *  - Rui Oliveira <rco@di.uminho.pt>
 *  - Jose Orlando Pereira <jop@di.uminho.pt>
 *  - Antonio Luis Sousa <als@di.uminho.pt>
 *  - Alfranio Tavares Correia Junior <alfranio@lsd.di.uminho.pt> 
 *  - Luis Soares <los@di.uminho.pt>
 *  - Ricardo Manuel Pereira Vilaca <rmvilaca@di.uminho.pt>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,
 * USA.
 */


package escada.tpc.common;

/**
 * It is used as a state transition manager, which means that it controls from
 * the current step which is the next step to be executed during the simulation.
 * 
 */
public abstract class StateTransition {
	protected StateObject curTrans = null;

	protected int curState = 0;

	protected int oldState = 0;

	/**
	 * It loads the states that compose the transitions of the simulation. Using
	 * an objetct, the user can define the appropriate class acording to its
	 * needs and problems.
	 * 
	 * @param Object
	 *            used to load the simulation's state.
	 */
	abstract public void loadStates(Object obj);

	/**
	 * It returns the simulation's current state. Using an object, we can define
	 * the sates according to our needs and problems. The current implementation
	 * uses the class StateObject in order to define a simulation's state.
	 * 
	 * @return the simulation's state
	 * @see getCurrentState
	 */
	public StateObject getCurrentStateObject() {
		return (curTrans);
	}

	/**
	 * It returns the number of the simulation's current state.
	 * 
	 * @return the number of the simulation's current state
	 */
	public int getCurrentState() {
		return (curState);
	}

	/**
	 * It returns the number last simulation's state.
	 * 
	 * @return the number of the simulation's current state
	 */
	public int getOldState() {
		return (oldState);
	}

	/**
	 * It returns the next simulation's state.
	 * 
	 * @return the next simulation's state
	 */
	abstract public StateObject nextState();
}
// arch-tag: 61f8f74e-e1b2-435f-8ea8-90f7e3e5f269

