package com.encuestas;

public class ToDoItem {
	private int id;
	private String item;

	ToDoItem(int id, String item) {
		this.id = id;
		this.item = item;
	}

	public int getId() {
		return id;
	}

	public String getItem() {
		return item;
	}

	public String toString() {
		return getItem();
	}
}
