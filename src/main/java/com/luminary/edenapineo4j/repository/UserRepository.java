package com.luminary.edenapineo4j.repository;

import com.luminary.edenapineo4j.model.database.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface UserRepository extends Neo4jRepository<User, Long> {
    @Query("MATCH (seller:User{id: $sellerId}), (purchaser:User{id:$purchaserId}) " +
            "CREATE (purchaser)-[:BOUGHT_IT {productId:$productId}]->(seller) " +
            "RETURN purchaser, [(purchaser)-[:BOUGHT_IT]->(s) | s] AS boughtFrom")
    User createPurchaseRelationship(Long purchaserId, Long sellerId, Long productId);
}
