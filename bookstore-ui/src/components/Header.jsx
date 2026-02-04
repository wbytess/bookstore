import React from "react";

import "../styles/header.css";

export default function Header({ onAddBookClick }) {
  return (
    <header className="header-container">
      <h1 className="header-title">Bookstore</h1>
      <nav className="header-nav">
        <button className="header-button" onClick={onAddBookClick}>
          Add Book
        </button>
      </nav>
    </header>
  );
}
