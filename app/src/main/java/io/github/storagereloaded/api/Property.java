package io.github.storagereloaded.api;

public interface Property {

	public int getId();

	public void setId(int id);

	public String getName();

	public void setName(String name);

	public Object getValue();

	public void setValue(Object value);

	public DisplayType getDisplayType();

	public void setDisplayType(DisplayType displayType);

	public Integer getMinimum();

	public void setMinimum(Integer minimum);

	public Integer getMaximum();

	public void setMaximum(Integer maximum);
}
