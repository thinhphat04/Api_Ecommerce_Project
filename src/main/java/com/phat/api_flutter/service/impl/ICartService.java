package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.models.CartProduct;

import java.util.List;

public interface ICartService {
//    List<CartProduct> findCartByUserId(String userId);
    List<CartProduct> getCartProducts();
    CartProduct addCartProduct(CartProduct cartProduct);
    CartProduct findById(String id);
    CartProduct updateCartProduct(CartProduct cartProduct);
}
