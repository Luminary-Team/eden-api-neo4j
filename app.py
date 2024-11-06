from flask import Flask, request, jsonify
from neo4j import GraphDatabase
from dotenv import load_dotenv
import os

load_dotenv()

app = Flask(__name__)
driver = GraphDatabase.driver(
    os.getenv('NEO4J_URI'),
    auth=(
        os.getenv('NEO4J_USERNAME'),
        os.getenv('NEO4J_PASSWORD')
    )
)

# def create_node(node):
#     with driver.session() as session:
#         with session.begin_transaction() as tx:
#             try:


def create_relationship(purchaser_id, seller_id, product_id):
    with driver.session() as session:
        with session.begin_transaction() as tx:
            try:
                result = tx.run(
                    """
                    MATCH (purchaser:User {id: $purchaser_id})
                    MATCH (seller:User {id: $seller_id})
                    MERGE (purchaser)-[:PURCHASED {product_id: $product_id}]->(seller)
                    RETURN purchaser
                    """,
                    seller_id=seller_id, purchaser_id=purchaser_id, product_id=product_id
                )
                purchaser_node = result.single()
                tx.commit()
                if purchaser_node:
                    return {"status": "success", "result": purchaser_node.data()}, 201
                return {"status": "error", "message": "No data found"}, 400
            except Exception as e:
                tx.rollback()
                return {"status": "error", "message": str(e)}, 500

def ensure_user_exists(user_id, user_name):
    with driver.session() as session:
        with session.begin_transaction() as tx:
            try:
                result = tx.run(
                    """
                    MERGE (user:User {id: $user_id})
                    ON CREATE SET user.name = $user_name
                    RETURN user
                    """,
                    user_id=user_id, user_name=user_name
                )
                user_node = result.single().data()
                tx.commit()
                return {"status": "success", "user": user_node} if user_node else {"status": "error", "message": "User could not be created or found"}
            except Exception as e:
                tx.rollback()
                return {"status": "error", "message": str(e)}

@app.route('/purchase', methods=['POST'])
def purchase():
    lista = request.json
    results = []
    
    for data in lista:
        purchaser_id = data.get("purchaserId")
        seller_id = data.get("sellerId")
        product_id = data.get("productId")

        result = create_relationship(purchaser_id, seller_id, product_id)
        results.append(result[0])

    return jsonify(results), 201 if all(r["status"] == "success" for r in results) else 207

@app.route('/user', methods=['POST'])
def create_or_find_user():
    data = request.json
    user_id = data.get("userId")
    user_name = data.get("userName")

    result = ensure_user_exists(user_id, user_name)
    return jsonify(result), 200 if result["status"] == "success" else 500


if __name__ == '__main__':
    app.run(host="0.0.0.0", port=0000)