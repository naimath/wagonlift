package com.wagonlift.extensions;

public class MongoOptions extends com.mongodb.MongoOptions {

	public int getConnectionsPerHost() {
		return this.connectionsPerHost;
	}
	public void setConnectionsPerHost(int value) {
		this.connectionsPerHost = value;
	}

	public int getThreadsAllowedToBlockForConnectionMultiplier() {
		return this.threadsAllowedToBlockForConnectionMultiplier;
	}
	public void setThreadsAllowedToBlockForConnectionMultiplier(int value) {
		this.threadsAllowedToBlockForConnectionMultiplier  = value;
	}
	
	public void setSlaveOk(boolean paramBoolean) {
	    this.slaveOk = paramBoolean;
	}
	public boolean getSlaveOk() {
		return this.slaveOk;
	}

}