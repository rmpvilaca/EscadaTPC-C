package escada.tpc.common.resources;

public interface DatabaseResourcesMBean {

	public abstract String getDriver();

	public abstract void setDriver(String driver);

	public abstract String getConnectionString();

	public abstract void setConnectionString(String connString);

	public abstract String getUserName();

	public abstract void setUserName(String userName);

	public abstract String getPassword();

	public abstract void setPassword(String password);

}