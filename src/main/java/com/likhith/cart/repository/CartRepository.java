package com.likhith.cart.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.likhith.cart.document.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {

	Optional<Cart> findByUserId(String userId);
}