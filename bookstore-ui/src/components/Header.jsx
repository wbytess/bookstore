import React, { useState } from "react";

import "../styles/header.css";

export default function Header({
  onCartClick,
  onAddBookClick,
  onShowAllBooks,
}) {
  return (
    <header className="header-container">
      <h1 className="header-title">Bookstore</h1>
      <nav className="header-nav">
        <button className="header-button" onClick={onCartClick}>
          Cart
        </button>
        <button className="header-button" onClick={onAddBookClick}>
          Add Book
        </button>
        <button className="header-button" onClick={onShowAllBooks}>
          Show All Books
        </button>
      </nav>
    </header>
  );
}
