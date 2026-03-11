function printResult(data) {
  document.getElementById("result").textContent =
      typeof data === "string" ? data : JSON.stringify(data, null, 2);
}

async function login() {
  try {
    const nickname = document.getElementById("nickname").value.trim();
    const password = document.getElementById("password").value.trim();

    const result = await apiRequest(
        CONFIG.ENDPOINTS.login,
        "POST",
        { nickname, password },
        false
    );

    // 백엔드 LoginResponseDto 구조가 여기서는 안 보이므로
    // 가장 흔한 케이스 기준으로 토큰 키를 유연하게 처리
    const token =
        result.accessToken || result.token || result.jwtToken || result.authorization;

    if (!token) {
      throw new Error("응답에서 토큰 키를 찾지 못했습니다. LoginResponseDto 필드명 확인 필요");
    }

    setToken(token);

    const me = await apiRequest(CONFIG.ENDPOINTS.me, "GET", null, true);

    if (me.id) {
      setUserId(me.id);
    }

    printResult({ login: result, me });
    alert("로그인 성공");
    window.location.href = "./app.html";
  } catch (e) {
    printResult(e.message);
    alert(e.message);
  }
}