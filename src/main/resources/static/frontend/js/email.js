function printResult(data) {
  document.getElementById("result").textContent =
      typeof data === "string" ? data : JSON.stringify(data, null, 2);
}

async function requestEmailCode() {
  try {
    const email = document.getElementById("requestEmail").value.trim();

    const result = await apiRequest(
        CONFIG.ENDPOINTS.emailRequest,
        "POST",
        { email },
        false
    );

    localStorage.setItem("verifiedEmailCandidate", email);
    printResult(result);
    alert("인증코드 요청 완료");
  } catch (e) {
    printResult(e.message);
    alert(e.message);
  }
}

async function verifyEmailCode() {
  try {
    const email = document.getElementById("verifyEmail").value.trim();
    const code = document.getElementById("verifyCode").value.trim();

    const result = await apiRequest(
        CONFIG.ENDPOINTS.emailVerify,
        "POST",
        { email, code },
        false
    );

    localStorage.setItem("verifiedEmail", email);
    printResult(result);
    alert("이메일 인증 완료");
  } catch (e) {
    printResult(e.message);
    alert(e.message);
  }
}