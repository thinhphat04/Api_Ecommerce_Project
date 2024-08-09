package com.phat.api_flutter.ImplementServices;

import com.phat.api_flutter.models.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();

    Product getOneProduct(String id);

    Product createProduct(Product product);

    Product updateProduct(String id, Product product);

    boolean deleteProduct(String id);
    List<Product> findByProductName(String productName);
}
