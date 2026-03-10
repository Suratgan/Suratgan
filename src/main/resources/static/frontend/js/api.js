function getToken() {
  return localStorage.getItem("accessToken") || "";
}

function setToken(token) {
  localStorage.setItem("accessToken", token);
}

function removeToken() {
  localStorage.removeItem("accessToken");
}

function setUserId(userId) {
  localStorage.setItem("userId", userId);
}

function getUserId() {
  return localStorage.getItem("userId");
}

async function apiRequest(path, method = "GET", body = null, auth = false) {
  const headers = {
    "Content-Type": "application/json"
  };

  if (auth && getToken()) {
    headers["Authorization"] = `Bearer ${getToken()}`;
  }

  const response = await fetch(CONFIG.BASE_URL + path, {
    method,
    headers,
    body: body ? JSON.stringify(body) : null
  });

  const text = await response.text();
  let data = null;

  try {
    data = text ? JSON.parse(text) : null;
  } catch (e) {
    data = text;
  }

  if (!response.ok) {
    const message =
        (data && data.message) ||
        (typeof data === "string" ? data : "요청 실패");
    throw new Error(message);
  }

  return data;
}