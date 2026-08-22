package com.vikram.api.models;

import java.io.File;
import java.util.UUID;

/**
 * Product creation payload. add-product is a multipart form rather than JSON, so this is
 * a builder for form fields rather than something Jackson serialises.
 */
public class Product {

	/**
	 * The backend rejects anything outside this range with a 500 and a ValidatorError:
	 * "Product Name must be 3 to 20 characters long". Checking locally turns that into a
	 * clear failure at the call site instead of an opaque server error.
	 */
	private static final int MIN_NAME_LENGTH = 3;
	private static final int MAX_NAME_LENGTH = 20;

	private String productName;
	private String productAddedBy;
	private String productCategory;
	private String productSubCategory;
	private String productPrice;
	private String productDescription;
	private String productFor;
	private File productImage;

	/**
	 * A fixture product with a name no other test can match.
	 *
	 * The name used to be "ZARA COAT 3", which is also a real catalogue product the UI
	 * tests add to the cart. Under parallel execution the fixture would appear in the
	 * catalogue alongside the real one, and getProductByName takes the first match -- so
	 * a UI test could add the API test's fixture and then have it deleted mid-run.
	 */
	public static Product defaults(String userId, File image) {
		Product product = new Product();
		// 8 hex characters keeps the name unique across parallel runs and inside the
		// 20-character limit the backend enforces.
		product.productName = "FIXTURE-" + UUID.randomUUID().toString().substring(0, 8);
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
		this.productName = validateName(productName);
		return this;
	}

	private static String validateName(String name) {
		if (name == null || name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
			throw new IllegalArgumentException("Product name must be " + MIN_NAME_LENGTH + " to "
					+ MAX_NAME_LENGTH + " characters (backend contract), got: "
					+ (name == null ? "null" : name.length() + " characters"));
		}
		return name;
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
