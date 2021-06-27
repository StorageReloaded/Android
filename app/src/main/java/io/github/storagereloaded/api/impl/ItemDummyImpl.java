package io.github.storagereloaded.api.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.storagereloaded.api.Item;
import io.github.storagereloaded.api.Location;
import io.github.storagereloaded.api.Property;
import io.github.storagereloaded.api.Tag;

public class ItemDummyImpl implements Item {

	private int id = 36543547;
	private String name = "TestItem";
	private String description = "TestItem please ignore";
	private String image = "http://IP/PATH/img.png";
	private Location location = new LocationDummyImpl();
	private List<Tag> tags = new ArrayList<>();
	private int amount = 25;

	private List<Property> propertiesInternal = new ArrayList<>();
	private List<Property> propertiesCustom = new ArrayList<>();
	private Map<String, String> attachments = new HashMap<>();

	private Date lastEdited = new Date(3165416541646341L);
	private Date created = new Date(3165416541646341L);

	public ItemDummyImpl() {
		tags.add(new TagDummyImpl());
		propertiesInternal.add(new PropertyDummyImpl());
		attachments.put("doc1", "http://IP/PATH/file.ext");
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
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getImage() {
		return image;
	}

	@Override
	public void setImage(String image) {
		this.image = image;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public List<Tag> getTags() {
		return tags;
	}

	@Override
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public List<Property> getPropertiesInternal() {
		return propertiesInternal;
	}

	@Override
	public void setPropertiesInternal(List<Property> propertiesInternal) {
		this.propertiesInternal = propertiesInternal;
	}

	@Override
	public List<Property> getPropertiesCustom() {
		return propertiesCustom;
	}

	@Override
	public void setPropertiesCustom(List<Property> propertiesCustom) {
		this.propertiesCustom = propertiesCustom;
	}

	@Override
	public Map<String, String> getAttachments() {
		return attachments;
	}

	@Override
	public void setAttachments(Map<String, String> attachments) {
		this.attachments = attachments;
	}

	@Override
	public Date getLastEdited() {
		return lastEdited;
	}

	@Override
	public void setLastEdited(Date lastEdited) {
		this.lastEdited = lastEdited;
	}

	@Override
	public Date getCreated() {
		return created;
	}

	@Override
	public void setCreated(Date created) {
		this.created = created;
	}
}
