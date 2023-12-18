package com.likhith.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.likhith.cart.dto.ProductCatalogResponse;

@FeignClient(name = "product-catalog-service", path = "/product-catalog")
public interface ProductCatalogClient {

	@GetMapping("/getProduct/{id}")
	ProductCatalogResponse getProduct(@PathVariable("id") String id);

}