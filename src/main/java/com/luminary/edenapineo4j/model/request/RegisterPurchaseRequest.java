package com.luminary.edenapineo4j.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterPurchaseRequest {
    private long purchaserId;
    private long sellerId;
    private long productId;
}
