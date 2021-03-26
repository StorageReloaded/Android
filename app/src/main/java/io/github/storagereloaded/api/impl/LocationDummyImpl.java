package io.github.storagereloaded.api.impl;

import java.util.ArrayList;
import java.util.List;

import io.github.storagereloaded.api.Item;
import io.github.storagereloaded.api.Location;

public class LocationDummyImpl implements Location {

	private int id = 65;
	private String name = "Shelf";
	private List<Item> items = new ArrayList<Item>();

	public LocationDummyImpl() {
		items.add(new ItemDummyImpl());
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
	public List<Item> getItems() {
		return items;
	}

	@Override
	public void setItems(List<Item> items) {
		this.items = items;
	}
}
