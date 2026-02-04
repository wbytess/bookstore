
const SESSION_KEY = "sample-session-id";

export function getSessionId() {
  let sessionId = localStorage.getItem(SESSION_KEY);

  if (!sessionId) {
    sessionId = crypto.randomUUID(); // browser-safe UUID
    localStorage.setItem(SESSION_KEY, sessionId);
  }

  return sessionId;
}

