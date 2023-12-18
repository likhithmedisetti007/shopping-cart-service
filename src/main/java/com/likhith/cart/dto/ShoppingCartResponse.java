package com.likhith.cart.dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartResponse {

	private String id;
	private String userId;
	private Set<String> productIds;
	private List<Product> products;
	private String subTotal;

}