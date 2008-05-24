package escada.tpc.common.clients.jmx;

import java.util.Hashtable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.log4j.Logger;

import escada.tpc.common.clients.ClientEmulation;
import escada.tpc.common.clients.jmx.ClientEmulationStartupMBean.Stage;

public class ServerControl {

	private final static Logger logger = Logger.getLogger(ServerControl.class);

	private final Hashtable<String, Stage> clientsStage = new Hashtable<String, Stage>();

	private final Hashtable<String, Vector<ClientEmulation>> clientsEmulation = new Hashtable<String, Vector<ClientEmulation>>();

	private final Hashtable<String, HashSet<String>> serverClients = new Hashtable<String, HashSet<String>>();

	private final Hashtable<String, Boolean> serverHealth = new Hashtable<String, Boolean>();

	private final Hashtable<String, String[]> clientConfiguration = new Hashtable<String, String[]>();

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

	public void removeClientEmulations(String key) {
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
		if (this.serverClients.containsKey(key)) {
			this.serverClients.remove(key);
			this.serverHealth.remove(key);
		} else {
			throw new InvalidTransactionException("Error removing server "
					+ key);
		}
	}

	public void clearServers() throws InvalidTransactionException {
		this.serverClients.clear();
		this.serverHealth.clear();
	}

	public int getNumberOfClients(String key)
	throws InvalidTransactionException {
		int ret = 0;
		if (key == null || key.equals("*")) {
			Iterator it = ((Hashtable) clientsEmulation.clone()).keySet()
			.iterator();
			while (it.hasNext()) {
				String keyValue = (String) it.next();
				Vector clients = (Vector) clientsEmulation.get(keyValue);
				if (clients != null) {
					ret = ret + clients.size();
				}
			}
		} else {
			Vector clients = clientsEmulation.get(key);
			if (clients != null) {
				ret = ret + clients.size();
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

			Iterator itServers = ((Hashtable) this.serverClients.clone())
			.keySet().iterator();

			while (itServers.hasNext()) {
				String keyValue = (String) itServers.next();

				HashSet server = serverClients.get(keyValue);
				if (server != null) {
					Iterator itClients = ((HashSet) server.clone()).iterator();
					while (itClients.hasNext()) {
						keyValue = (String) itClients.next();
						Vector clients = this.clientsEmulation.get(keyValue);
						ret = ret + (clients != null ? clients.size() : 0);
					}
				}
			}
		} else {
			HashSet<String> servers = serverClients.get(key);
			if (servers != null) {
				Iterator itClients = ((HashSet) servers.clone()).iterator();
				while (itClients.hasNext()) {
					String keyValue = (String) itClients.next();
					Vector clients = this.clientsEmulation.get(keyValue);
					ret = ret + (clients != null ? clients.size() : 0);
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

		HashSet<String> servers = serverClients.get(key);
		if (servers != null) {
			Iterator itClients = ((HashSet) servers.clone()).iterator();
			while (itClients.hasNext()) {
				String keyValue = (String) itClients.next();
				Vector clients = this.clientsEmulation.get(keyValue);
				Stage stg = this.clientsStage.get(keyValue);
				if (clients != null && stg != null && stg.equals(Stage.RUNNING)) {
					ret = keyValue;
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

			Iterator it = ((Hashtable) clientsEmulation.clone()).keySet()
			.iterator();
			while (it.hasNext()) {
				String keyValue = (String) it.next();

				Stage stg = this.clientsStage.get(keyValue);

				if (stg != null && stg.equals(Stage.RUNNING)) {

					this.clientsStage.put(keyValue, Stage.PAUSED);

					Vector<ClientEmulation> clients = clientsEmulation
					.get(keyValue);
					if (clients != null) {
						for (ClientEmulation e : clients) {
							e.pause();
						}
					}
				}
			}
		} else {

			Stage stg = this.clientsStage.get(key);
			if (stg != null && stg.equals(Stage.RUNNING)) {

				this.clientsStage.put(key, Stage.PAUSED);

				Vector<ClientEmulation> clients = clientsEmulation.get(key);
				if (clients != null) {
					for (ClientEmulation e : clients) {
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
			Iterator it = ((Hashtable) clientsEmulation.clone()).keySet()
			.iterator();
			while (it.hasNext()) {
				String keyValue = (String) it.next();

				Stage stg = this.clientsStage.get(keyValue);
				if (stg != null && stg.equals(Stage.PAUSED)) {

					this.clientsStage.put(keyValue, Stage.RUNNING);

					Vector<ClientEmulation> clients = clientsEmulation
					.get(keyValue);
					if (clients != null) {
						for (ClientEmulation e : clients) {
							e.resume();
						}
					}
				}
			}
		} else {
			Stage stg = this.clientsStage.get(key);
			if (stg != null && stg.equals(Stage.PAUSED)) {

				this.clientsStage.put(key, Stage.RUNNING);

				Vector<ClientEmulation> clients = clientsEmulation.get(key);
				if (clients != null) {
					for (ClientEmulation e : clients) {
						e.resume();
					}
				}
			} else {
				throw new InvalidTransactionException(key + " resume on " + stg);
			}
		}
	}

	public void stopClient(String key) throws InvalidTransactionException {
		if (key == null || key.equals("*")) {
			Iterator it = ((Hashtable) clientsEmulation.clone()).keySet()
			.iterator();
			while (it.hasNext()) {
				String keyValue = (String) it.next();
				Stage stg = this.clientsStage.get(keyValue);
				if (stg != null
						&& (stg.equals(Stage.RUNNING) || stg
								.equals(Stage.PAUSED))) {

					Vector<ClientEmulation> clients = clientsEmulation
					.get(keyValue);
					if (clients != null) {
						for (ClientEmulation e : clients) {
							e.stopit();
						}
					}

					this.clientsEmulation.remove(keyValue);
					this.clientsStage.remove(keyValue);

					String args[] = this.clientConfiguration.get(keyValue);
					int cont = 0;
					if (args != null) {
						while (cont < args.length) {
							if (args[cont].startsWith("jdbc")) {
								break;
							}
							cont++;
						}
						this.detachClientToServer(keyValue, args[cont]);
					}
				}
			}
		} else {
			Stage stg = this.clientsStage.get(key);
			if (stg != null
					&& (stg.equals(Stage.RUNNING) || stg.equals(Stage.PAUSED))) {

				Vector<ClientEmulation> clients = clientsEmulation.get(key);
				if (clients != null) {
					for (ClientEmulation e : clients) {
						e.stopit();
					}
				}

				this.clientsEmulation.remove(key);
				this.clientsStage.remove(key);
			} else {
				throw new InvalidTransactionException(key + " stop on " + stg);
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

		Iterator itServers = ((Hashtable) this.serverClients.clone()).keySet()
		.iterator();

		while (itServers.hasNext()) {
			String keyServers = (String) itServers.next();

			if (serverHealth.get(keyServers)) {
				int sum = 0;
				HashSet<String> servers = serverClients.get(keyServers);
				if (servers != null) {
					Iterator itClients = ((HashSet) servers.clone()).iterator();
					while (itClients.hasNext()) {

						String keyValue = (String) itClients.next();

						Vector clients = this.clientsEmulation.get(keyValue);
						sum = sum + (clients != null ? clients.size() : 0);
					}
					if (sum < min) {
						ret = keyServers;
						min = sum;
					}
				}
			}
		}
		return (ret);
	}
}
