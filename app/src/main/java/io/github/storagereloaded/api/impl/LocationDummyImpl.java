package io.github.storagereloaded.api.impl;

import io.github.storagereloaded.api.Location;

public class LocationDummyImpl implements Location {

	private int id = 65;
	private String name = "Shelf";

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
}
