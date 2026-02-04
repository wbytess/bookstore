import React, { useState } from "react";
import "../styles/addbookform.css";

export default function AddBookForm() {
  const [name, setName] = useState("");
  const [author, setAuthor] = useState("");
  const [price, setPrice] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    const bookData = { name, author, price: parseFloat(price) };

    try {
      const res = await fetch("http://localhost:8080/api/books", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(bookData),
      });

      if (!res.ok) throw new Error("Failed to add book");

      alert("Book added successfully!");
      setName("");
      setAuthor("");
      setPrice("");
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <form className="addbook-form" onSubmit={handleSubmit}>
      <h2>Add New Book</h2>
      <label>
        Name:
        <input
          type="text"
          value={name}
          onChange={(e) => setName(e.target.value)}
          required
        />
      </label>
      <label>
        Author:
        <input
          type="text"
          value={author}
          onChange={(e) => setAuthor(e.target.value)}
          required
        />
      </label>
      <label>
        Price:
        <input
          type="number"
          step="0.01"
          value={price}
          onChange={(e) => setPrice(e.target.value)}
          required
        />
      </label>
      <button type="submit">Add Book</button>
    </form>
  );
}
