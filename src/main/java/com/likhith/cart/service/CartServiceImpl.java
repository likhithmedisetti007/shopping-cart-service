package com.likhith.cart.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.likhith.cart.client.ProductCatalogClient;
import com.likhith.cart.document.Cart;
import com.likhith.cart.dto.Product;
import com.likhith.cart.dto.ProductCatalogResponse;
import com.likhith.cart.dto.ShoppingCartResponse;
import com.likhith.cart.repository.CartRepository;

@Component
public class CartServiceImpl implements CartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ProductCatalogClient productCatalogClient;

	@Override
	public ShoppingCartResponse getCart(String userId) {

		ShoppingCartResponse shoppingCartResponse = new ShoppingCartResponse();
		Optional<Cart> cart = cartRepository.findByUserId(userId);

		if (cart.isPresent()) {
			BeanUtils.copyProperties(cart.get(), shoppingCartResponse);

			if (!CollectionUtils.isEmpty(shoppingCartResponse.getProductIds())) {
				List<ProductCatalogResponse> productCatalogResponseList = shoppingCartResponse.getProductIds().stream()
						.map(productId -> productCatalogClient.getProduct(productId)).collect(Collectors.toList());

				if (!CollectionUtils.isEmpty(productCatalogResponseList)) {
					shoppingCartResponse.setProducts(productCatalogResponseList.stream().map(productCatalogResponse -> {
						Product product = new Product();
						BeanUtils.copyProperties(productCatalogResponse, product);
						return product;
					}).collect(Collectors.toList()));

					shoppingCartResponse.setSubTotal(String.valueOf(productCatalogResponseList.stream()
							.mapToInt(productCatalogResponse -> Integer.parseInt(productCatalogResponse.getPrice()))
							.sum()));
				}
			}
			return shoppingCartResponse;
		} else {
			return null;
		}
	}

	@Override
	public String createCart(String userId) {

		String message = null;
		Optional<Cart> cartFromDB = cartRepository.findByUserId(userId);

		if (cartFromDB.isEmpty()) {
			Cart cart = new Cart();
			cart.setUserId(userId);
			cartRepository.save(cart);
			message = "Cart created successfully";
		} else {
			message = "There is a cart already available for the user";
		}

		return message;

	}

	@Override
	public String addToCart(Cart cart) {

		String message = null;
		Optional<Cart> cartFromDB = cartRepository.findByUserId(cart.getUserId());

		if (cartFromDB.isPresent()) {
			if (!CollectionUtils.isEmpty(cartFromDB.get().getProductIds())) {
				List<String> newProductIds = cart.getProductIds().stream()
						.filter(productId -> !cartFromDB.get().getProductIds().contains(productId))
						.collect(Collectors.toList());
				List<String> commonProductIds = cart.getProductIds().stream()
						.filter(productId -> cartFromDB.get().getProductIds().contains(productId))
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(newProductIds)) {
					cartFromDB.get().getProductIds().addAll(cart.getProductIds());
					cartRepository.save(cartFromDB.get());
					message = "ProductIds: " + newProductIds + " Added to cart and ProductIds: " + commonProductIds
							+ " were already available in cart";
				} else {
					message = "Nothing new for adding to cart";
				}

			} else {
				cartFromDB.get().setProductIds(cart.getProductIds());
				cartRepository.save(cartFromDB.get());
				message = "ProductIds: " + cart.getProductIds() + " Added to cart";
			}
		} else {
			message = "No cart available for the user";
		}
		return message;
	}

	@Override
	public String removeFromCart(Cart cart) {

		String message = null;
		Optional<Cart> cartFromDB = cartRepository.findByUserId(cart.getUserId());

		if (cartFromDB.isPresent()) {
			if (!CollectionUtils.isEmpty(cartFromDB.get().getProductIds())) {
				List<String> removableProductIds = cart.getProductIds().stream()
						.filter(productId -> cartFromDB.get().getProductIds().contains(productId))
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(removableProductIds)) {
					cartFromDB.get().getProductIds().removeAll(cart.getProductIds());
					cartRepository.save(cartFromDB.get());
					message = "ProductIds: " + cart.getProductIds() + " removed from cart";
				} else {
					message = "Nothing to remove from cart";
				}
			} else {
				message = "No products available in cart to remove";
			}
		} else {
			message = "Cart is not available";
		}
		return message;
	}

	@Override
	public String clearCart(String userId) {

		Optional<Cart> cartFromDB = cartRepository.findByUserId(userId);

		if (cartFromDB.isEmpty()) {
			Cart cart = new Cart();
			cart.setUserId(userId);
			cartRepository.save(cart);
			return "Cart is created with empty";
		} else {
			if (CollectionUtils.isEmpty(cartFromDB.get().getProductIds())) {
				return "Cart is already empty";
			} else {
				cartFromDB.get().getProductIds().clear();
				cartRepository.save(cartFromDB.get());
				return "Cart cleared";
			}
		}
	}

}