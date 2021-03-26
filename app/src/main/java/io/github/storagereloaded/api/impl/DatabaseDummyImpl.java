package io.github.storagereloaded.api.impl;

import java.util.ArrayList;
import java.util.List;

import io.github.storagereloaded.api.Database;
import io.github.storagereloaded.api.Location;

public class DatabaseDummyImpl implements Database {

	private int id = 43;
	private String name = "Shelter";
	private List<Location> locations = new ArrayList<Location>();

	public DatabaseDummyImpl() {
		locations.add(new LocationDummyImpl());
	}

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

	@Override
	public List<Location> getLocations() {
		return locations;
	}

	@Override
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
}
