import React, { useEffect, useState } from "react";
import "../styles/AllBooks.css";

export default function AllBooks() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);

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

  if (loading) return <p className="AllBooks-loading">Loading books...</p>;
  if (books.length === 0)
    return <p className="AllBooks-empty">No books found.</p>;

  return (
    <div className="AllBooks">
      <h2 className="AllBooks-title">All Books</h2>
      <ul className="AllBooks-list">
        {books.map((book) => (
          <li key={book.id} className="AllBooks-item">
            <strong>{book.name}</strong> by {book.author} - ${book.price}
          </li>
        ))}
      </ul>
    </div>
  );
}
