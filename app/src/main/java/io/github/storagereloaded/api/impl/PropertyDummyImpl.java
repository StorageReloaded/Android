package io.github.storagereloaded.api.impl;

import io.github.storagereloaded.api.DisplayType;
import io.github.storagereloaded.api.Property;

public class PropertyDummyImpl implements Property {

	private int id = 5;
	private String name = "Mein EAN13";
	private Object value = 1568745165912L;
	private DisplayType displayType = DisplayType.EAN13;
	private Integer min = null;
	private Integer max = null;

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
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public DisplayType getDisplayType() {
		return displayType;
	}

	@Override
	public void setDisplayType(DisplayType displayType) {
		this.displayType = displayType;
	}

	@Override
	public Integer getMinimum() {
		return min;
	}

	@Override
	public void setMinimum(Integer min) {
		this.min = min;
	}

	@Override
	public Integer getMaximum() {
		return max;
	}

	@Override
	public void setMaximum(Integer max) {
		this.max = max;
	}
}
