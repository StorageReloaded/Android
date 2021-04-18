package io.github.storagereloaded.api.impl;

import java.util.ArrayList;
import java.util.List;

import io.github.storagereloaded.api.Database;
import io.github.storagereloaded.api.Item;

public class DatabaseDummyImpl implements Database {

	private int id = 43;
	private String name = "Shelter";
	private List<Item> items = new ArrayList<>();

	public DatabaseDummyImpl() {
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
