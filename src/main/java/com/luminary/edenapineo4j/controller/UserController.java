package com.luminary.edenapineo4j.controller;

import com.luminary.edenapineo4j.model.database.User;
import com.luminary.edenapineo4j.model.request.RegisterPurchaseRequest;
import com.luminary.edenapineo4j.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    @PostMapping("/registerPurchase")
    public ResponseEntity<List<User>> registerPurchase(@RequestBody List<RegisterPurchaseRequest> request) {
        return ResponseEntity.status(OK).body(userService.registerPurchase(request));
    }
}
