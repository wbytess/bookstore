import React, { useState } from "react";
import Header from "../components/Header";
import AddBookForm from "../components/AddBookForm";
import AllBooks from "../components/AllBooks";

export default function HomePage() {
  const [showAddBook, setShowAddBook] = useState(false);
  const [showAllBooks, setShowAllBooks] = useState(false);

  const handleAddBookClick = () => {
    setShowAddBook(!showAddBook);
    // hide AllBooks when opening AddBook
    setShowAllBooks(false);
  };

  const handleShowAllBooksClick = () => {
    setShowAllBooks(!showAllBooks);
    // hide AddBook when opening AllBooks
    setShowAddBook(false);
  };

  return (
    <>
      <Header
        onAddBookClick={handleAddBookClick}
        onShowAllBooks={handleShowAllBooksClick}
      />
      <main>
        <h2>Welcome to the Bookstore</h2>
        {showAddBook && <AddBookForm />}
        {showAllBooks && <AllBooks />}
      </main>
    </>
  );
}
