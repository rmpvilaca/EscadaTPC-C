package escada.tpc.common;

public class TPCConst {
	private static int numMinClients = 10;

	public static void setNumMinClients(int numMinClients) {
		TPCConst.numMinClients = numMinClients;
	}

	public static int getNumMinClients() {
		return numMinClients;
	}
}
