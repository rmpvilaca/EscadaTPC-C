package escada.tpc.common.clients.jmx;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;

import escada.tpc.common.clients.ClientEmulation;
import escada.tpc.common.clients.jmx.ClientEmulationStartupMBean.Stage;

public class ServerControl {

	private final static Logger logger = Logger.getLogger(ServerControl.class);

	private final HashMap<String, Stage> clientsStage = new HashMap<String, Stage>();

	private final HashMap<String, Vector<ClientEmulation>> clientsEmulation = new HashMap<String, Vector<ClientEmulation>>();

	private final HashMap<String, String[]> clientConfiguration = new HashMap<String, String[]>();

	private final HashMap<String, HashSet<String>> serverClients = new HashMap<String, HashSet<String>>();

	private final HashMap<String, Boolean> serverHealth = new HashMap<String, Boolean>();

	public Stage getClientStage(String key) {
		return (clientsStage.get(key));
	}

	public void setClientStage(String key, Stage stage) {
		clientsStage.put(key, stage);
	}

	public void setClientEmulations(String key, Vector<ClientEmulation> ebs) {
		this.clientsEmulation.put(key, ebs);
	}

	public Vector<ClientEmulation> getClientEmulations(String key) {
		return (this.clientsEmulation.get(key));
	}

	public void removeClientStage(String key) {
		this.clientsStage.remove(key);
	}

	public void removeClientEmulation(String key) {
		this.clientsEmulation.remove(key);
	}

	public void setClientConfiguration(String key, String args[]) {
		this.clientConfiguration.put(key, args);
	}

	public String[] getClientConfiguration(String key) {
		return (this.clientConfiguration.get(key));
	}

	public void removeClientConfiguration(String key) {
		this.clientConfiguration.remove(key);
	}

	public void attachClientToServer(String client, String machine) {
		HashSet<String> s = this.serverClients.get(machine);
		s.add(client);
	}

	public void detachClientToServer(String client, String machine) {
		HashSet<String> s = this.serverClients.get(machine);
		s.remove(client);
	}

	public boolean isServerHealth(String machine) {
		boolean ret = false;
		if (this.serverHealth.containsKey(machine)
				&& this.serverHealth.get(machine)) {
			ret = true;
		}
		return (ret);
	}

	public void setServerHealth(String machine, boolean health) {
		if (this.serverHealth.containsKey(machine)) {
			this.serverHealth.put(machine, health);
		}
	}

	public HashSet<String> getClients() {
		HashSet<String> ret = new HashSet<String>();
		ret.addAll(this.clientsEmulation.keySet());
		return (ret);
	}

	public void addServer(String key) throws InvalidTransactionException {
		if (!this.serverClients.containsKey(key)) {
			this.serverClients.put(key, new HashSet<String>());
			this.serverHealth.put(key, false);
		} else {
			throw new InvalidTransactionException("Error adding server " + key);
		}
	}

	public void removeServer(String key) throws InvalidTransactionException {
		if (serverClients.containsKey(key)) {
			this.serverClients.remove(key);
			this.serverHealth.remove(key);
		} else {
			throw new InvalidTransactionException("Error removing server "
					+ key);
		}
	}

	public int getNumberOfClients(String key)
			throws InvalidTransactionException {
		int ret = 0;
		if (key == null || key.equals("*")) {
			Iterator<String> it = clientsEmulation.keySet().iterator();
			while (it.hasNext()) {
				String keyValue = it.next();
				ret = ret + clientsEmulation.get(keyValue).size();
			}
		} else {
			if (clientsEmulation.containsKey(key)) {
				ret = ret + clientsEmulation.get(key).size();
			} else {
				throw new InvalidTransactionException(
						"Error getting number of clients for " + key);
			}
		}
		return (ret);
	}

	public int getNumberOfClientsOnServer(String key)
			throws InvalidTransactionException {
		int ret = 0;

		if (key == null || key.equals("*")) {

			Iterator<String> itServers = this.serverClients.keySet().iterator();

			while (itServers.hasNext()) {
				String keyValue = itServers.next();

				Iterator<String> itClients = serverClients.get(keyValue)
						.iterator();
				while (itClients.hasNext()) {

					keyValue = itClients.next();
					ret = ret
							+ (this.clientsEmulation.get(keyValue) != null ? this.clientsEmulation
									.get(keyValue).size()
									: 0);
				}
			}
		} else {
			if (serverClients.containsKey(key)) {
				Iterator<String> itClients = serverClients.get(key).iterator();
				while (itClients.hasNext()) {
					String keyValue = itClients.next();
					ret = ret
							+ (this.clientsEmulation.get(keyValue) != null ? this.clientsEmulation
									.get(keyValue).size()
									: 0);
				}
			} else {
				throw new InvalidTransactionException(
						"Error getting number of clients for " + key);
			}
		}
		return (ret);
	}

	public String findServerClient(String key) {
		String ret = null;

		if (serverClients.containsKey(key)) {
			HashSet<String> s = this.serverClients.get(key);
			Iterator<String> it = s.iterator();
			while (it.hasNext()) {
				String client = it.next();
				if (this.clientsEmulation.containsKey(client)
						&& this.clientsStage.get(client).equals(Stage.RUNNING)) {
					ret = client;
					break;
				}
			}
		}

		return (ret);
	}

	public HashSet<String> getServers() throws InvalidTransactionException {
		return (new HashSet<String>(this.serverClients.keySet()));
	}

	public void pauseClient(String key) throws InvalidTransactionException {
		if (key == null || key.equals("*")) {
			Iterator<String> it = clientsEmulation.keySet().iterator();
			while (it.hasNext()) {
				String keyValue = it.next();

				if (this.clientsStage.get(keyValue) != null
						&& this.clientsStage.get(keyValue)
								.equals(Stage.RUNNING)) {

					this.clientsStage.put(keyValue, Stage.PAUSED);

					if (clientsEmulation.get(keyValue) != null) {
						for (ClientEmulation e : clientsEmulation.get(keyValue)) {
							e.pause();
						}
					}
				}
			}
		} else {

			if (this.clientsStage.get(key) != null
					&& this.clientsStage.get(key).equals(Stage.RUNNING)) {

				this.clientsStage.put(key, Stage.PAUSED);

				if (clientsEmulation.get(key) != null) {
					for (ClientEmulation e : clientsEmulation.get(key)) {
						e.pause();
					}

				}
			} else {
				throw new InvalidTransactionException(key + " pause on "
						+ this.clientsStage.get(key));
			}
		}
	}

	public void resumeClient(String key) throws InvalidTransactionException {
		if (key == null || key.equals("*")) {
			Iterator<String> it = clientsEmulation.keySet().iterator();
			while (it.hasNext()) {
				String keyValue = it.next();

				if (this.clientsStage.get(keyValue) != null
						&& this.clientsStage.get(keyValue).equals(Stage.PAUSED)) {

					this.clientsStage.put(keyValue, Stage.RUNNING);

					if (clientsEmulation.get(keyValue) != null) {
						for (ClientEmulation e : clientsEmulation.get(keyValue)) {
							e.resume();
						}
					}
				}
			}
		} else {
			if (this.clientsStage.get(key) != null
					&& this.clientsStage.get(key).equals(Stage.PAUSED)) {

				this.clientsStage.put(key, Stage.RUNNING);

				if (clientsEmulation.get(key) != null) {
					for (ClientEmulation e : clientsEmulation.get(key)) {
						e.resume();
					}
				}
			} else {
				throw new InvalidTransactionException(key + " resume on "
						+ this.clientsStage.get(key));
			}
		}
	}

	public void stopClient(String key) throws InvalidTransactionException {
		if (key == null || key.equals("*")) {
			Iterator it = ((HashMap) clientsEmulation.clone()).keySet().iterator();
			while (it.hasNext()) {
				String keyValue = (String) it.next();
				if (this.clientsStage.get(keyValue) != null
						&& (this.clientsStage.get(keyValue).equals(
								Stage.RUNNING) || this.clientsStage.get(
								keyValue).equals(Stage.PAUSED))) {

					if (clientsEmulation.get(keyValue) != null) {
						for (ClientEmulation e : clientsEmulation.get(keyValue)) {
							e.stopit();
						}
					}

					this.clientsEmulation.remove(keyValue);
					this.clientsStage.remove(keyValue);
					String args[] = this.clientConfiguration.remove(keyValue);
					int cont = 0;
					while (cont < args.length) {
						if (args[cont].startsWith("jdbc")) {
							break;
						}
						cont++;
					}
					this.detachClientToServer(keyValue, args[cont]);
				}
			}
		} else {
			if (this.clientsStage.get(key) != null
					&& (this.clientsStage.get(key).equals(Stage.RUNNING) || this.clientsStage
							.get(key).equals(Stage.PAUSED))) {

				if (clientsEmulation.get(key) != null) {
					for (ClientEmulation e : clientsEmulation.get(key)) {
						e.stopit();
					}
				}

				this.clientsEmulation.remove(key);
				this.clientsStage.remove(key);
			} else {
				throw new InvalidTransactionException(key + " stop on "
						+ this.clientsStage.get(key));
			}
		}
	}

	public String findFreeClient() {
		String ret = null;
		TreeSet<String> keyRet = new TreeSet<String>(this.clientsEmulation
				.keySet());
		if (keyRet.size() > 0) {
			String info[] = keyRet.last().split("-");
			int next = Integer.parseInt(info[1]) + 1;
			ret = "client-" + next;
		} else {
			ret = "client-0";
		}
		return (ret);
	}

	public String findFreeMachine() {
		String ret = null;
		int min = Integer.MAX_VALUE;

		Iterator<String> itServers = serverClients.keySet().iterator();
		while (itServers.hasNext()) {
			String keyServers = itServers.next();
			if (serverHealth.get(keyServers)) {
				int sum = 0;
				Iterator<String> itClients = serverClients.get(keyServers)
						.iterator();
				while (itClients.hasNext()) {

					String keyValue = itClients.next();
					sum = sum
							+ (this.clientsEmulation.get(keyValue) != null ? this.clientsEmulation
									.get(keyValue).size()
									: 0);
				}
				if (sum < min) {
					ret = keyServers;
					min = sum;
				}
			}
		}
		return (ret);
	}
}
