package io.github.storagereloaded.api;

import java.util.List;

public interface Location {

	public int getId();

	public void setId(int id);

	public String getName();

	public void setName(String name);

	public List<Item> getItems();

	public void setItems(List<Item> items);
}
