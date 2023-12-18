package com.likhith.cart.document;

import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.likhith.cart.dto.Product;

import lombok.Data;

@Document(collection = "cart")
@Data
public class Cart {

	@Id
	private String id;
	private String userId;
	private Set<String> productIds;
	@Transient
	private List<Product> products;
	private String subTotal;

}