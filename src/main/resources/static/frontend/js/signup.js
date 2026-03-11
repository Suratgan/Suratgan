let isEmailVerified = false;
let lastVerifiedEmail = "";
let isNicknameChecked = false;
let checkedNickname = "";

window.addEventListener("DOMContentLoaded", () => {
  const emailInput = document.getElementById("email");
  const nicknameInput = document.getElementById("nickname");

  if (emailInput) {
    emailInput.addEventListener("input", handleEmailChange);
  }

  if (nicknameInput) {
    nicknameInput.addEventListener("input", handleNicknameChange);
  }
});

function handleNicknameChange() {
  const currentNickname = document.getElementById("nickname").value.trim();

  if (!currentNickname) {
    isNicknameChecked = false;
    checkedNickname = "";
    setNicknameStatus("", "info");
    return;
  }

  if (isNicknameChecked && currentNickname !== checkedNickname) {
    isNicknameChecked = false;
    checkedNickname = "";
    setNicknameStatus("아이디가 변경되었습니다. 다시 중복체크해주세요.", "error");
  }
}

function printResult(data) {
  document.getElementById("result").textContent =
      typeof data === "string" ? data : JSON.stringify(data, null, 2);
}

function setEmailStatus(message, type = "info") {
  const statusEl = document.getElementById("emailStatusMessage");
  if (!statusEl) return;

  if (!message || !message.trim()) {
    statusEl.textContent = "";
    statusEl.className = "status-message";
    return;
  }

  statusEl.textContent = message;
  statusEl.className = `status-message show ${type}`;
}

function showVerifySection() {
  const verifySection = document.getElementById("emailVerifySection");
  if (verifySection) {
    verifySection.style.display = "block";
  }
}

function hideVerifySection() {
  const verifySection = document.getElementById("emailVerifySection");
  if (verifySection) {
    verifySection.style.display = "none";
  }
}

function clearEmailVerificationState() {
  isEmailVerified = false;
  lastVerifiedEmail = "";

  const codeInput = document.getElementById("emailCode");
  if (codeInput) {
    codeInput.value = "";
  }
}

function handleEmailChange() {
  const currentEmail = document.getElementById("email").value.trim();

  if (!currentEmail) {
    clearEmailVerificationState();
    hideVerifySection();
    setEmailStatus("", "info");
    return;
  }

  if (isEmailVerified && currentEmail !== lastVerifiedEmail) {
    clearEmailVerificationState();
    showVerifySection();
    setEmailStatus("이메일이 변경되었습니다. 다시 인증해주세요.", "error");
  }
}

async function sendEmailCode() {
  try {
    const email = document.getElementById("email").value.trim();

    if (!email) {
      setEmailStatus("이메일을 먼저 입력해주세요.", "error");
      return;
    }

    isEmailVerified = false;
    // localStorage.removeItem("verifiedEmail");

    const result = await apiRequest(
        CONFIG.ENDPOINTS.emailRequest,
        "POST",
        { email },
        false
    );

    printResult(result);
    showVerifySection();
    setEmailStatus("인증번호가 발송되었습니다. 이메일을 확인해주세요.", "info");
    alert("인증번호가 발송되었습니다.");
  } catch (e) {
    printResult(e.message);
    setEmailStatus(e.message, "error");
    alert(e.message);
  }
}

async function verifyEmailCode() {
  try {
    const email = document.getElementById("email").value.trim();
    const code = document.getElementById("emailCode").value.trim();

    if (!email) {
      setEmailStatus("이메일을 먼저 입력해주세요.", "error");
      return;
    }

    if (!code) {
      setEmailStatus("인증번호를 입력해주세요.", "error");
      return;
    }

    const result = await apiRequest(
        CONFIG.ENDPOINTS.emailVerify,
        "POST",
        { email, code },
        false
    );

    isEmailVerified = true;
    lastVerifiedEmail = email;
    printResult(result);
    hideVerifySection();
    setEmailStatus("이메일 인증이 완료되었습니다.", "success");
    alert("이메일 인증이 완료되었습니다.");
  } catch (e) {
    isEmailVerified = false;
    printResult(e.message);
    setEmailStatus(e.message, "error");
    alert(e.message);
  }
}

function setNicknameStatus(message, type = "info") {
  const statusEl = document.getElementById("nicknameStatusMessage");
  if (!statusEl) return;

  if (!message || !message.trim()) {
    statusEl.textContent = "";
    statusEl.className = "status-message";
    return;
  }

  statusEl.textContent = message;
  statusEl.className = `status-message show ${type}`;
}

async function checkNickname() {
  try {
    const nickname = document.getElementById("nickname").value.trim();

    if (!nickname) {
      setNicknameStatus("아이디를 먼저 입력해주세요.", "error");
      return;
    }

    const result = await apiRequest(
        CONFIG.ENDPOINTS.nicknameCheck,
        "POST",
        { nickname },
        false
    );

    printResult(result);

    if (result.available) {
      isNicknameChecked = true;
      checkedNickname = nickname;
      setNicknameStatus("사용 가능한 아이디입니다.", "success");
    } else {
      isNicknameChecked = false;
      checkedNickname = "";
      setNicknameStatus(result.message || "이미 사용 중인 아이디입니다.", "error");
    }
  } catch (e) {
    isNicknameChecked = false;
    checkedNickname = "";
    printResult(e.message);
    setNicknameStatus(e.message, "error");
  }
}

async function signup() {
  try {
    const nickname = document.getElementById("nickname").value.trim();
    const password = document.getElementById("password").value.trim();
    const email = document.getElementById("email").value.trim();
    const role = document.getElementById("role").value;
    const phone = document.getElementById("phone").value.trim();

    if (!isNicknameChecked || nickname !== checkedNickname) {
      setNicknameStatus("아이디 중복체크를 완료해주세요.", "error");
      alert("아이디 중복체크를 먼저 완료해주세요.");
      return;
    }

    if (!isEmailVerified || email !== lastVerifiedEmail) {
      setEmailStatus("이메일 인증 완료 후 회원가입이 가능합니다.", "error");
      alert("이메일 인증을 먼저 완료해주세요.");
      return;
    }

    const result = await apiRequest(
        CONFIG.ENDPOINTS.signup,
        "POST",
        { nickname, password, email, role, phone },
        false
    );

    printResult(result);
    alert("회원가입 완료");
  } catch (e) {
    printResult(e.message);
    alert(e.message);
  }
}