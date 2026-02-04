import React, { useEffect, useState } from "react";
import "../styles/allbooks.css";

export default function AllBooks() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [quantities, setQuantities] = useState({});

  useEffect(() => {
    fetch("http://localhost:8080/api/books")
      .then((res) => res.json())
      .then((data) => {
        setBooks(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching books:", err);
        setLoading(false);
      });
  }, []);

  const handleQuantityChange = (bookId, value) => {
    setQuantities((prev) => ({ ...prev, [bookId]: value }));
  };

  const handleAddToCart = (bookId) => {
    const quantity = quantities[bookId] || 1;

    fetch("http://localhost:8080/api/cart", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ bookId, quantity }),
    })
      .then((res) => {
        if (!res.ok) throw new Error("Failed to add to cart");
        return res.json();
      })
      .then((data) => {
        alert(`Added ${quantity} of book ID ${bookId} to cart`);
      })
      .catch((err) => alert(err.message));
  };

  if (loading) return <p className="AllBooks-loading">Loading books...</p>;
  if (books.length === 0)
    return <p className="AllBooks-empty">No books found.</p>;

  return (
    <div className="allbooks">
      <h2>All Books</h2>
      <div className="allbooks-container">
        {books.map((book) => (
          <div key={book.id} className="allbooks-item">
            <h3 className="allbooks-name">{book.name}</h3>
            <p className="allbooks-author">{book.author}</p>
            <p className="allbooks-price">${book.price.toFixed(2)}</p>
            <input
              type="number"
              min="1"
              value={quantities[book.id] || 1}
              onChange={(e) =>
                handleQuantityChange(book.id, parseInt(e.target.value))
              }
              className="allbooks-quantity"
            />
            <button
              className="allbooks-add-button"
              onClick={() => handleAddToCart(book.id)}
            >
              Add
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
