package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.dto.AddToCartRequest;
import com.phat.api_flutter.dto.AddToCartResponse;
import com.phat.api_flutter.dto.CartProductDto;
import com.phat.api_flutter.models.CartProduct;

import java.util.List;

public interface ICartService {
    List<CartProductDto> getUserCart(String userId);
    int getUserCartCount(String userId);
    AddToCartResponse addToCart(String userId, AddToCartRequest request);
    CartProductDto getCartProductByUserIdAndCartProductId(String userId, String cartProductId);
    CartProductDto modifyProductQuantity(String userId, String cartProductId, int quantity);
    void removeCartProduct(String userId, String cartProductId);
    CartProduct findById(String cartProductId);
    void deleteCartProduct(CartProduct cartProduct);
}
