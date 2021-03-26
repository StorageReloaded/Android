package io.github.storagereloaded.api.impl;

import io.github.storagereloaded.api.Tag;

public class TagDummyImpl implements Tag {

	private int id = 1;
	private String name = "test-tag";
	private int color = 345156;
	private int icon = 24;

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
	public int getColor() {
		return color;
	}

	@Override
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public int getIcon() {
		return icon;
	}

	@Override
	public void setIcon(int icon) {
		this.icon = icon;
	}
}
