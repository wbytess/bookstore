import React, { useEffect, useState } from "react";
import { fetchCartItems } from "../api/cartApi";
import "../styles/cart.css";

export default function Cart() {
  const [items, setItems] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    loadCart();
  }, []);

  const loadCart = async () => {
    try {
      const data = await fetchCartItems();
      setItems(data);
    } catch (err) {
      setError(err.message);
    }
  };

  const total = items
    .reduce((sum, item) => sum + (item.subtotal || 0), 0)
    .toFixed(2);

  return (
    <div className="cart-container">
      <h2 className="cart-title">Your Cart</h2>

      {error && <p className="cart-error">{error}</p>}

      {items.length === 0 ? (
        <p className="cart-empty">Your cart is empty</p>
      ) : (
        <>
          <table className="cart-table">
            <thead>
              <tr>
                <th>Book</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Subtotal</th>
              </tr>
            </thead>
            <tbody>
              {items.map((item) => (
                <tr key={item.id}>
                  <td>{item.book.name}</td>
                  <td>${item.book.price.toFixed(2)}</td>
                  <td>{item.quantity}</td>
                  <td>${item.subtotal.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>

          <div className="cart-total">
            <strong>Total: ${total}</strong>
          </div>
        </>
      )}
    </div>
  );
}
