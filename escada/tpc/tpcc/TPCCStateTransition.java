package escada.tpc.tpcc;

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
public class TPCCStateTransition extends StateTransition {
	private final DeliveryTrans delivery = new DeliveryTrans();

	private final NewOrderTrans neworder = new NewOrderTrans();

	private final OrderStatusTrans orderstatus = new OrderStatusTrans();

	private final PaymentTrans payment = new PaymentTrans();

	private final StockLevelTrans stocklevel = new StockLevelTrans();

	private final StateObject[] trans = { neworder, payment, orderstatus,
			delivery, stocklevel };

	private Random objRand = new Random();

	protected int curState = 0;

	protected int oldState = 0;

	public TPCCStateTransition() {
		initStates();
	}

	public StateObject nextState() {
		StateObject nextState = null;
		int nrand = RandGen.nextInt(objRand, 100000);

		int probini = 0;
		int probend = trans[0].getProb() * 1000;
		for (int i = 0; i < TPCCConst.numState; i++) {
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
		for (int i = 0; i < TPCCConst.numState; i++) {
			trans[i].setProb();
			trans[i].setThinkTime();
			trans[i].setKeyingTime();
		}
	}

	public void loadStates(Object obj) {
	}
}
// arch-tag: 466c4b36-320d-4a1a-81f7-41973434eb9f
