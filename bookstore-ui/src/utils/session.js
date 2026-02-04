export function getSessionId() {
  let sessionId = localStorage.getItem("sessionId");
  if (!sessionId) {
    sessionId = crypto.randomUUID(); // generates a random UUID
    localStorage.setItem("sessionId", sessionId);
  }
  return sessionId;
}
