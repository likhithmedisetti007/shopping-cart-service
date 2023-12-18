package com.likhith.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.likhith.cart.document.Cart;
import com.likhith.cart.dto.ShoppingCartResponse;
import com.likhith.cart.service.CartService;

@RestController
@RequestMapping("/shopping-cart")
public class CartController {

	@Autowired
	CartService service;

	@GetMapping("/getCart/{userId}")
	public ResponseEntity<ShoppingCartResponse> getCart(@PathVariable("userId") String userId) {
		ShoppingCartResponse response = service.getCart(userId);
		if (ObjectUtils.isEmpty(response)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build();
		} else {
			return ResponseEntity.ok().body(response);
		}
	}

	@GetMapping("/createCart/{userId}")
	public ResponseEntity<String> createCart(@PathVariable("userId") String userId) {
		return ResponseEntity.ok().body(service.createCart(userId));
	}

	@PutMapping("/addToCart")
	public ResponseEntity<String> addToCart(@RequestBody Cart cart) {
		return ResponseEntity.ok().body(service.addToCart(cart));
	}

	@PatchMapping("/removeFromCart")
	public ResponseEntity<String> removeFromCart(@RequestBody Cart cart) {
		return ResponseEntity.ok().body(service.removeFromCart(cart));
	}

	@DeleteMapping("/clearCart/{userId}")
	public ResponseEntity<String> clearCart(@PathVariable("userId") String userId) {
		return ResponseEntity.ok().body(service.clearCart(userId));
	}

}