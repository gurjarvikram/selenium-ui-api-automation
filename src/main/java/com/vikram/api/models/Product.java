package com.vikram.api.models;

import java.io.File;

/**
 * Product creation payload. add-product is a multipart form rather than JSON, so this is
 * a builder for form fields rather than something Jackson serialises.
 */
public class Product {

	private String productName;
	private String productAddedBy;
	private String productCategory;
	private String productSubCategory;
	private String productPrice;
	private String productDescription;
	private String productFor;
	private File productImage;

	public static Product defaults(String userId, File image) {
		Product product = new Product();
		product.productName = "ZARA COAT 3";
		product.productAddedBy = userId;
		product.productCategory = "fashion";
		product.productSubCategory = "shirts";
		product.productPrice = "11500";
		product.productDescription = "Automated fixture product";
		product.productFor = "women";
		product.productImage = image;
		return product;
	}

	public Product withName(String productName) {
		this.productName = productName;
		return this;
	}

	public Product withPrice(String productPrice) {
		this.productPrice = productPrice;
		return this;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductAddedBy() {
		return productAddedBy;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public String getProductSubCategory() {
		return productSubCategory;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public String getProductFor() {
		return productFor;
	}

	public File getProductImage() {
		return productImage;
	}
}
