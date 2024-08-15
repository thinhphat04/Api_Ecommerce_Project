package com.phat.api_flutter.service;

import com.phat.api_flutter.dto.AddToCartRequest;
import com.phat.api_flutter.dto.AddToCartResponse;
import com.phat.api_flutter.dto.CartProductDto;
import com.phat.api_flutter.models.CartProduct;
import com.phat.api_flutter.models.Product;
import com.phat.api_flutter.models.User;
import com.phat.api_flutter.repository.CartProductRepository;
import com.phat.api_flutter.repository.ProductRepository;
import com.phat.api_flutter.repository.UserRepository;
import com.phat.api_flutter.service.impl.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService implements ICartService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartProductRepository cartProductRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public List<CartProductDto> getUserCart(String userId) {
        User user = findUserById(userId);
        List<CartProduct> cartProducts = cartProductRepository.findAllById(user.getCart());

        if (cartProducts.isEmpty()) {
            return new ArrayList<>(); // Return an empty list
        }

        List<CartProductDto> cart = new ArrayList<>();

        return cartProducts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public int getUserCartCount(String userId) {
        User user = findUserById(userId);
        return user.getCart().size();
    }

    @Transactional
    public AddToCartResponse addToCart(String userId, AddToCartRequest request) {
        User user = findUserById(userId);

        // Find user's cart products
        List<CartProduct> userCartProducts = cartProductRepository.findAllById(user.getCart());

        // Check if the cart already contains a product with the same size and color
        CartProduct existingCartItem = userCartProducts.stream()
                .filter(item -> item.getProduct().equals(request.getProductId()) &&
                        item.getSelectedSize().equals(request.getSelectedSize()) &&
                        item.getSelectedColour().equals(request.getSelectedColour()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            // If the product already exists in the cart, increase the quantity
            Product product = findProductById(existingCartItem.getProduct());

            if (product.getCountInStock() >= existingCartItem.getQuantity() + 1) {
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                cartProductRepository.save(existingCartItem);

                // Update the product's stock
                product.setCountInStock(product.getCountInStock() - 1);
                productRepository.save(product);
                return convertToResponse(existingCartItem);
            } else {
                throw new RuntimeException("Out of stock!");
            }
        }

        // If the product is not in the cart, add a new CartProduct
        Product product = findProductById(request.getProductId());

        CartProduct cartProduct = new CartProduct();
        cartProduct.setProduct(product.getId());
        cartProduct.setProductName(product.getName());
        cartProduct.setProductImage(product.getImage());
        cartProduct.setProductPrice(product.getPrice());
        cartProduct.setSelectedSize(request.getSelectedSize());
        cartProduct.setSelectedColour(request.getSelectedColour());
        cartProduct.setQuantity(request.getQuantity());
        cartProduct.setReserved(true);

        cartProduct = cartProductRepository.save(cartProduct);

        // Add to the user's cart
        user.getCart().add(cartProduct.getId());
        userRepository.save(user);

        // Update product stock
        product.setCountInStock(product.getCountInStock() - cartProduct.getQuantity());
        productRepository.save(product);

        return convertToResponse(cartProduct);
    }

    private AddToCartResponse convertToResponse(CartProduct cartProduct) {
        return new AddToCartResponse(
                cartProduct.getId(),
                cartProduct.getProduct(),
                cartProduct.getQuantity(),
                cartProduct.getSelectedSize(),
                cartProduct.getSelectedColour(),
                cartProduct.getProductName(),
                cartProduct.getProductImage(),
                cartProduct.getProductPrice(),
                cartProduct.isReserved(),
                cartProduct.getReservationExpiry()
        );
    }

    @Transactional
    public CartProductDto getCartProductByUserIdAndCartProductId(String userId, String cartProductId) {
        User user = findUserById(userId);
        CartProduct cartProduct = findCartProductById(cartProductId);

        return convertToDto(cartProduct);
    }


    @Transactional
    public CartProductDto modifyProductQuantity(String userId, String cartProductId, int quantity) {
        // Fetch the User by ID
        User user = findUserById(userId);

        // Fetch the CartProduct by ID
        CartProduct cartProduct = findCartProductById(cartProductId);

        // Fetch the actual Product
        Product actualProduct = findProductById(cartProduct.getProduct());


        // Check if the requested quantity exceeds stock
        if (quantity > actualProduct.getCountInStock()) {
            throw new RuntimeException("Insufficient stock for the requested quantity");
        }

        // Update the CartProduct quantity
        cartProduct.setQuantity(quantity);
        cartProduct = cartProductRepository.save(cartProduct);

        return convertToDto(cartProduct);
    }

    @Transactional
    public void removeCartProduct(String userId, String cartProductId) {
        User user = findUserById(userId);

        CartProduct cartProduct = findCartProductById(cartProductId);

        user.getCart().remove(cartProductId);
        userRepository.save(user);

        cartProductRepository.delete(cartProduct);
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Product findProductById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private CartProduct findCartProductById(String cartProductId) {
        return cartProductRepository.findById(cartProductId)
                .orElseThrow(() -> new RuntimeException("Cart Product not found"));
    }

    private CartProductDto convertToDto(CartProduct cartProduct) {
        CartProductDto dto = new CartProductDto();
        dto.setId(cartProduct.getId());
        dto.setProduct(cartProduct.getProduct());
        dto.setQuantity(cartProduct.getQuantity());
        dto.setSelectedSize(cartProduct.getSelectedSize());
        dto.setSelectedColour(cartProduct.getSelectedColour());

        Product product = productRepository.findById(cartProduct.getProduct()).orElse(null);

        if (product == null) {
            dto.setProductExists(false);
            dto.setProductOutOfStock(true);
        } else {
            dto.setProductName(product.getName());
            dto.setProductImage(product.getImage());
            dto.setProductPrice(product.getPrice());

            boolean isOutOfStock = !cartProduct.isReserved() && product.getCountInStock() < cartProduct.getQuantity();
            dto.setProductExists(true);
            dto.setProductOutOfStock(isOutOfStock);
        }

        return dto;
    }
}
