import React, { useState } from "react";
import Header from "../components/Header";
import AddBookForm from "../components/AddBookForm";

export default function HomePage() {
  const [showAddBook, setShowAddBook] = useState(false);

  return (
    <>
      <Header onAddBookClick={() => setShowAddBook(!showAddBook)} />
      <main>
        <h2>Welcome to the Bookstore</h2>
        {showAddBook && <AddBookForm />}
      </main>
    </>
  );
}
