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


package escada.tpc.cluster;

import java.util.Random;

import escada.tpc.common.StateObject;
import escada.tpc.common.StateTransition;
import escada.tpc.common.util.RandGen;

/**
 * It defines the state machine of the TPC-C. First of all, it loads the
 * possible states according to the TPC-C's definitions and initialize the base
 * values used to calculate the thinktime and the keyingtime of each state
 * (transaction). It also defines the probability of occurrence for each state.
 */
public class CLUSTERStateTransition extends StateTransition {
	private final UpdateTrans update = new UpdateTrans();

	private final ReadOnlyTrans read = new ReadOnlyTrans();

	private final StateObject[] trans = { update, read };

	private Random objRand = new Random();

	protected int curState = 0;

	protected int oldState = 0;

	public CLUSTERStateTransition() {
		initStates();
	}

	public StateObject nextState() {
		StateObject nextState = null;
		int nrand = RandGen.nextInt(objRand, 100000);

		int probini = 0;
		int probend = trans[0].getProb() * 1000;
		for (int i = 0; i < CLUSTERConst.numState; i++) {
			if (nrand >= probini && nrand < probend) {
				nextState = trans[i];
				break;
			} else {
				nextState = trans[i];
				probend = probend + trans[i + 1].getProb() * 1000;
			}
		}
		curTrans = nextState;
		return nextState;
	}

	private void initStates() {
		for (int i = 0; i < CLUSTERConst.numState; i++) {
			trans[i].setProb();
			trans[i].setThinkTime();
			trans[i].setKeyingTime();
		}
	}

	public void loadStates(Object obj) {
	}
}
// arch-tag: 03e279d5-0ed6-46c8-b904-2484ad2746ca
