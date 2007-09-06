package escada.tpc.common;

import java.sql.SQLException;

public abstract class Emulation extends EmulationConfiguration implements PausableEmulation {
	/**
	 * It sets up the emulator.
	 * 
	 */
	public abstract void initialize();

	/**
	 * It returns the calculated thinktime, according to the properties of the
	 * benchmark, using the value defined by the setThinkTime and retrieved with
	 * the getKeyingTime.
	 * 
	 * @return the thinktime
	 * @see getThinkTime,setThinkTime
	 */
	public abstract long thinkTime();

	/**
	 * It returns the calculated keyingtime, according to the properties of the
	 * benchmark, using the value defined by the setKeyingTime and retrieved
	 * with the getKeyingTime.
	 * 
	 * @see getKeyingTime,setKeyingTime
	 */
	public abstract long keyingTime();

	/**
	 * It proceeds with the emulation according to the host to which it belongs
	 * and based on the benchmark's properties.
	 * 
	 * @param int
	 *            host to which the emulator is attached to.
	 * @see run,processIncrement
	 */
	public abstract void process(String hid) throws SQLException;

	/**
	 * It proceeds with the emulation according to the host to which it belongs
	 * and based on the benchmark's properties.
	 * 
	 * @see run,processIncrement
	 */
	public void process() throws SQLException {
		process(getHostId());
	}

	/**
	 * In contrast to the process method, it executes just one transaction per
	 * call according to the host to which it belongs and based on the
	 * benchmark's properties.
	 * 
	 * @param int
	 *            host to which the emulator is attached to.
	 * @see run,process
	 */
	public abstract Object processIncrement(String hid) throws SQLException;

	/**
	 * In contrast to the process method, it executes just one transaction per
	 * call according to the host to which it belongs and based on the
	 * benchmark's properties.
	 * 
	 * @see run,process
	 */
	public Object processIncrement() throws SQLException {
		return (processIncrement(getHostId()));
	}
}
// arch-tag: 001ca60a-aae1-48e1-8c23-681cc4dde63f
