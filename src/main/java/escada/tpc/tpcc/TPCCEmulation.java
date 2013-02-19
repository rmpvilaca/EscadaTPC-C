/*
 * Copyright 2013 Universidade do Minho
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software   distributed under the License is distributed on an "AS IS" BASIS,   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and limitations under the License.
 */

package escada.tpc.tpcc;

import escada.tpc.common.Emulation;
import escada.tpc.common.StateObject;
import escada.tpc.common.util.RandGen;
import org.apache.log4j.Logger;

import java.sql.SQLException;

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
