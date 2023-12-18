package com.likhith.cart.service;

import org.springframework.stereotype.Service;

import com.likhith.cart.document.Cart;
import com.likhith.cart.dto.ShoppingCartResponse;

@Service
public interface CartService {

	ShoppingCartResponse getCart(String userId);
	
	String createCart(String userId);

	String addToCart(Cart cart);

	String removeFromCart(Cart cart);

	String clearCart(String userId);

}