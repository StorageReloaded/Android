package io.github.storagereloaded.api;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Item {

	public int getId();

	public void setId(int id);

	public String getName();

	public void setName(String name);

	public String getDescription();

	public void setDescription(String description);

	public String getImage();

	public void setImage(String image);

	public Location getLocation();

	public void setLocation(Location location);

	public List<Tag> getTags();

	public void setTags(List<Tag> tags);

	public int getAmount();

	public void setAmount(int amount);

	public List<Property> getPropertiesInternal();

	public void setPropertiesInternal(List<Property> properties);

	public List<Property> getPropertiesCustom();

	public void setPropertiesCustom(List<Property> properties);

	public Map<String, String> getAttachments();

	public void setAttachments(Map<String, String> attachments);

	public Date getLastEdited();

	public void setLastEdited(Date lastEdited);

	public Date getCreated();

	public void setCreated(Date created);
}
