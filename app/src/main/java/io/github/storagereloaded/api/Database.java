package io.github.storagereloaded.api;

import java.util.List;

public interface Database {

	public int getId();

	public void setId(int id);

	public String getName();

	public void setName(String name);

	public List<Location> getLocations();

	public void setLocations(List<Location> locations);
}
