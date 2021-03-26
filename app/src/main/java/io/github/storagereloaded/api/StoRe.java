package io.github.storagereloaded.api;

import java.util.Arrays;
import java.util.List;

import io.github.storagereloaded.api.impl.DatabaseDummyImpl;

public class StoRe {

	private String sessionId;

	public StoRe(String sessionId) {
		this.sessionId = sessionId;
	}

	public List<Database> getDatabases() {
		return Arrays.asList((Database) new DatabaseDummyImpl());
	}

	public boolean sync(List<Database> databases) {
		return false;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public static StoRe auth(String username, String password) {
		return new StoRe("abcd1234");
	}
}
