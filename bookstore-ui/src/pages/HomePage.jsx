import React from "react";
import "./HomePage.css";

const HomePage = () => {
  return (
    <div className="homepage-container">
      <header className="homepage-header">
        <h1 className="homepage-title">Bookstore</h1>
        <nav className="homepage-nav">
          <a href="/">Home</a>
          <a href="/books">Books</a>
          <a href="/cart">Cart</a>
        </nav>
      </header>

      <main className="homepage-main">
        <h2 className="homepage-welcome">Welcome to Our Bookstore!</h2>
        <p className="homepage-description">
          Explore a wide variety of books and find your next favorite read.
        </p>
      </main>
    </div>
  );
};

export default HomePage;
