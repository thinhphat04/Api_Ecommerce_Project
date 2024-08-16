package com.phat.api_flutter.service.impl;

import com.phat.api_flutter.models.CartProduct;

public interface ICartProductService {
    CartProduct findCartById(String id);
}
