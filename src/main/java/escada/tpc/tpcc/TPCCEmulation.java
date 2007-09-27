package escada.tpc.tpcc;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;

/**
 * It extends the emulation class defining some methods according to the TPC-C
 * definition. Specifically, it defines the thinktime, keyingtime, process and
 * processIncrement.
 * 
 * @see Emulation
 */
public class TPCCEmulation extends Emulation {
	Logger logger = Logger.getLogger(TPCCEmulation.class);

	StateObject curTrans = null;

	// Pausable implementation
	private boolean paused = false;

	public void pause() {
		this.paused = true;
	}

	public void resume() {
		this.paused = false;
		synchronized (this) {
			this.notify();
		}
	}

	public void stopit() {
		setFinished(true);
	}

	public void initialize() {
	}

	public long thinkTime() {
		long r = RandGen.negExp(getRandom(), curTrans.getThinkTime() * 1000,
				0.36788, curTrans.getThinkTime() * 1000, 4.54e-5, curTrans
						.getThinkTime() * 1000);
		return (r);
	}

	public long keyingTime() {
		return (curTrans.getKeyingTime());
	}

	public void process(String hid) throws SQLException {
		try {
			while ((getMaxTransactions() == -1) || (getMaxTransactions() > 0)) {
				
				if (isFinished()) {
					logger.info("Client is returning.");
					return;
				}
				
				curTrans = getStateTransition().nextState();

				setKeyingTime(keyingTime());
				setThinkTime(getKeyingTime() + thinkTime());

				if (getStatusThinkTime()) {
					Thread.sleep(getThinkTime());
				}

				curTrans.requestProcess(this, hid);

				curTrans.postProcess(this, hid);

				// check if should pause
				synchronized (this) {
					while (paused) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							logger.error("Unable to wait on pause!", e);
						}
					}
				}
			}
		} catch (java.lang.InterruptedException it) {
		}
	}

	public Object processIncrement(String hid) throws SQLException {
		Object trans = null;

		if (isFinished()) {
			logger.info("Client is returning.");
			return (trans);
		}
		curTrans = getStateTransition().nextState();

		setKeyingTime(keyingTime());
		setThinkTime(getKeyingTime() + thinkTime());

		trans = curTrans.requestProcess(this, hid);

		curTrans.postProcess(this, hid);

		return (trans);
	}
}
// arch-tag: d08fc9ef-d774-4095-a9c0-41c500320bda
