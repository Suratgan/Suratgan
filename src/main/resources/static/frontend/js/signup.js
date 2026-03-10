window.addEventListener("DOMContentLoaded", () => {
  const verifiedEmail = localStorage.getItem("verifiedEmail");
  if (verifiedEmail) {
    document.getElementById("email").value = verifiedEmail;
  }
});

function printResult(data) {
  document.getElementById("result").textContent =
      typeof data === "string" ? data : JSON.stringify(data, null, 2);
}

async function signup() {
  try {
    const nickname = document.getElementById("nickname").value.trim();
    const password = document.getElementById("password").value.trim();
    const email = document.getElementById("email").value.trim();
    const role = document.getElementById("role").value;

    const result = await apiRequest(
        CONFIG.ENDPOINTS.signup,
        "POST",
        { nickname, password, email, role },
        false
    );

    printResult(result);
    alert("회원가입 완료");
  } catch (e) {
    printResult(e.message);
    alert(e.message);
  }
}