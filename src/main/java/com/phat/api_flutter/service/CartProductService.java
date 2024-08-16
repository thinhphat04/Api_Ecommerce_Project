package com.phat.api_flutter.service;

import com.phat.api_flutter.models.CartProduct;
import com.phat.api_flutter.repository.CartProductRepository;
import com.phat.api_flutter.service.impl.ICartProductService;
import org.springframework.stereotype.Service;

@Service
public class CartProductService implements ICartProductService {
    public CartProductRepository cartProductRepository;
    @Override
    public CartProduct findCartById(String id) {
        return cartProductRepository.findById(id).orElse(null);
    }
}
