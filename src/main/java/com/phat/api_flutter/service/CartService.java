package com.phat.api_flutter.service;

import com.phat.api_flutter.models.CartProduct;
import com.phat.api_flutter.repository.CartProductRepository;
import com.phat.api_flutter.service.impl.ICartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService implements ICartService {

    public CartProductRepository cartProductRepository;

    @Override
    public List<CartProduct> getCartProducts() {
        return cartProductRepository.findAll();
    }

    @Override
    public CartProduct addCartProduct(CartProduct cartProduct) {
        return null;
    }

    @Override
    public CartProduct findById(String id) {
        return cartProductRepository.findById(id).orElse(null);
    }

    @Override
    public CartProduct updateCartProduct(CartProduct cartProduct) {
        return null;
    }
}
