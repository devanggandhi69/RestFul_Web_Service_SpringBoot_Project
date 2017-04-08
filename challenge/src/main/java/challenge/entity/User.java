package challenge.entity;

import java.util.List;

public class User {
	private int id;
	private String name;
	private String content;
	
	/**
	 * 
	 */
	public User() {		
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
