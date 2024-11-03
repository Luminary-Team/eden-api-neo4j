package com.luminary.edenapineo4j.service;

import com.luminary.edenapineo4j.model.database.User;
import com.luminary.edenapineo4j.model.request.RegisterPurchaseRequest;
import com.luminary.edenapineo4j.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> registerPurchase(List<RegisterPurchaseRequest> request) {
        return request.stream()
                .map(registerPurchaseRequest ->
                    userRepository.createPurchaseRelationship(
                            registerPurchaseRequest.getPurchaserId(),
                            registerPurchaseRequest.getSellerId(),
                            registerPurchaseRequest.getProductId()
                    ))
            .toList();
    }
}
