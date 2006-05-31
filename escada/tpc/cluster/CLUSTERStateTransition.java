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
