package com.vikram.api.models;

import java.util.List;

public class Orders {

	private List<OrderDetail> orders;

	public Orders() {
	}

	public Orders(List<OrderDetail> orders) {
		this.orders = orders;
	}

	public List<OrderDetail> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDetail> orders) {
		this.orders = orders;
	}
}
