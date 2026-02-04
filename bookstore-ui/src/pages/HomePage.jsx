import React, { useState } from "react";
import Header from "../components/Header";
import AddBookForm from "../components/AddBookForm";
import AllBooks from "../components/AllBooks";
import Cart from "../components/Cart";

export default function HomePage() {
  const [showAddBook, setShowAddBook] = useState(false);
  const [showAllBooks, setShowAllBooks] = useState(false);
  const [showCart, setShowCart] = useState(false);

  const handleAddBookClick = () => {
    setShowAddBook(!showAddBook);
    // hide when opening AddBook
    setShowAllBooks(false);
    setShowCart;
  };

  const handleShowAllBooksClick = () => {
    setShowAllBooks(!showAllBooks);
    // hide when opening AllBooks
    setShowAddBook(false);
    setShowCart;
  };

  const handleShowCartClick = () => {
    setShowCart(!showCart);
    // hide when opening Cart
    setShowAddBook(false);
    setShowAllBooks(false);
  };

  return (
    <>
      <Header
        onAddBookClick={handleAddBookClick}
        onShowAllBooks={handleShowAllBooksClick}
        onCartClick={handleShowCartClick}
      />
      <main>
        <h2>Welcome to the Bookstore</h2>
        {showAddBook && <AddBookForm />}
        {showAllBooks && <AllBooks />}
        {showCart && <Cart />}
      </main>
    </>
  );
}
