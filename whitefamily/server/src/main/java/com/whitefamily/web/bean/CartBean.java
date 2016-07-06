package com.whitefamily.web.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "cartBean", eager = false)
@SessionScoped
public class CartBean {

	private Cart cart;

	public CartBean() {
		super();
		cart = new Cart();
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	
	
	
}
