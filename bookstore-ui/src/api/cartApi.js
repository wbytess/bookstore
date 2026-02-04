const CART_API_URL = "http://localhost:8080/api/cart";
const SESSION_KEY = "sample-session-id";

function getSessionId() {
  let sessionId = localStorage.getItem(SESSION_KEY);
  if (!sessionId) {
    sessionId = crypto.randomUUID();
    localStorage.setItem(SESSION_KEY, sessionId);
  }
  return sessionId;
}

export async function fetchCartItems() {
  const response = await fetch(CART_API_URL, {
    headers: {
      "X-Session-Id": getSessionId(),
    },
  });

  if (!response.ok) {
    throw new Error("Failed to fetch cart items");
  }

  return response.json();
}
