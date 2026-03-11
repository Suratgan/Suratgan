let isNicknameChecked = false;
let checkedNickname = "";
let originalNickname = "";

let editingAddressId = null;
let currentAddress = null;

window.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("accessToken");

    if (!token) {
        alert("로그인이 필요합니다.");
        window.location.href = "./login.html";
        return;
    }

    const nicknameInput = document.getElementById("nickname");
    if (nicknameInput) {
        nicknameInput.addEventListener("input", handleNicknameChange);
    }

    loadMyInfo();
    loadAddress();
});

function printResult(data) {
    document.getElementById("result").textContent =
        typeof data === "string" ? data : JSON.stringify(data, null, 2);
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

function handleNicknameChange() {
    const currentNicknameValue = document.getElementById("nickname").value.trim();

    if (!currentNicknameValue) {
        isNicknameChecked = false;
        checkedNickname = "";
        setNicknameStatus("", "info");
        return;
    }

    if (currentNicknameValue === originalNickname) {
        isNicknameChecked = true;
        checkedNickname = currentNicknameValue;
        setNicknameStatus("현재 사용 중인 닉네임입니다.", "info");
        return;
    }

    if (isNicknameChecked && currentNicknameValue !== checkedNickname) {
        isNicknameChecked = false;
        checkedNickname = "";
        setNicknameStatus("닉네임이 변경되었습니다. 다시 중복체크해주세요.", "error");
    }
}

async function loadMyInfo() {
    try {
        const result = await apiRequest(
            CONFIG.ENDPOINTS.me,
            "GET",
            null,
            true
        );

        printResult(result);

        document.getElementById("nickname").value = result.nickname || "";
        document.getElementById("email").value = result.email || "";
        document.getElementById("role").value = result.role || "";

        originalNickname = result.nickname || "";
        checkedNickname = result.nickname || "";
        isNicknameChecked = true;

        setNicknameStatus("현재 사용 중인 닉네임입니다.", "info");
    } catch (e) {
        printResult(e.message);
        alert(e.message);
    }
}

async function checkNickname() {
    try {
        const nickname = document.getElementById("nickname").value.trim();

        if (!nickname) {
            setNicknameStatus("닉네임을 먼저 입력해주세요.", "error");
            return;
        }

        if (nickname === originalNickname) {
            isNicknameChecked = true;
            checkedNickname = nickname;
            setNicknameStatus("현재 사용 중인 닉네임입니다.", "info");
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
            setNicknameStatus(result.message || "사용 가능한 닉네임입니다.", "success");
        } else {
            isNicknameChecked = false;
            checkedNickname = "";
            setNicknameStatus(result.message || "이미 사용 중인 닉네임입니다.", "error");
        }
    } catch (e) {
        isNicknameChecked = false;
        checkedNickname = "";
        printResult(e.message);
        setNicknameStatus(e.message, "error");
        alert(e.message);
    }
}

async function updateNickname() {
    try {
        const nickname = document.getElementById("nickname").value.trim();

        if (!nickname) {
            setNicknameStatus("닉네임을 입력해주세요.", "error");
            alert("닉네임을 입력해주세요.");
            return;
        }

        if (!isNicknameChecked || nickname !== checkedNickname) {
            setNicknameStatus("닉네임 중복체크를 완료해주세요.", "error");
            alert("닉네임 중복체크를 먼저 완료해주세요.");
            return;
        }

        const result = await apiRequest(
            CONFIG.ENDPOINTS.updateMe,
            "PATCH",
            { nickname },
            true
        );

        printResult(result);
        alert("닉네임이 수정되었습니다.");

        originalNickname = nickname;
        checkedNickname = nickname;
        isNicknameChecked = true;
        setNicknameStatus("현재 사용 중인 닉네임입니다.", "info");

        await loadMyInfo();
    } catch (e) {
        printResult(e.message);
        alert(e.message);
    }
}

async function updatePassword() {
    try {
        const currentPassword = document.getElementById("currentPassword").value.trim();
        const newPassword = document.getElementById("newPassword").value.trim();
        const newPasswordConfirm = document.getElementById("newPasswordConfirm").value.trim();

        if (!currentPassword) {
            alert("현재 비밀번호를 입력해주세요.");
            return;
        }

        if (!newPassword) {
            alert("새 비밀번호를 입력해주세요.");
            return;
        }

        if (!newPasswordConfirm) {
            alert("새 비밀번호 확인을 입력해주세요.");
            return;
        }

        if (newPassword !== newPasswordConfirm) {
            alert("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
            return;
        }

        if (currentPassword === newPassword) {
            alert("새 비밀번호는 현재 비밀번호와 다르게 입력해주세요.");
            return;
        }

        const result = await apiRequest(
            CONFIG.ENDPOINTS.updatePassword,
            "PATCH",
            { currentPassword, newPassword },
            true
        );

        printResult(result);
        alert("비밀번호가 변경되었습니다. 다시 로그인해주세요.");

        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        window.location.href = "./login.html";
    } catch (e) {
        printResult(e.message);
        alert(e.message);
    }
}

async function deleteMyAccount() {
    const confirmed = confirm("정말 회원 탈퇴하시겠습니까? 탈퇴 후에는 복구할 수 없습니다.");
    if (!confirmed) return;

    try {
        const result = await apiRequest(
            CONFIG.ENDPOINTS.deleteMe,
            "DELETE",
            null,
            true
        );

        printResult(result);
        alert("회원 탈퇴가 완료되었습니다.");

        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        window.location.href = "./login.html";
    } catch (e) {
        printResult(e.message);
        alert(e.message);
    }
}

/* =========================
   주소 관련 (단건)
========================= */

function resetAddressForm() {
    editingAddressId = null;

    document.getElementById("address").value = "";
    document.getElementById("detailAddress").value = "";

    document.getElementById("addressFormTitle").textContent = "배송지 등록";
    document.getElementById("addressSubmitButton").textContent = "주소 등록";
    document.getElementById("addressCancelButton").style.display = "none";
}

function setAddressEditMode(address) {
    editingAddressId = address.id;

    document.getElementById("address").value = address.address || "";
    document.getElementById("detailAddress").value = address.detailAddress || "";

    document.getElementById("addressFormTitle").textContent = "배송지 수정";
    document.getElementById("addressSubmitButton").textContent = "주소 수정";
    document.getElementById("addressCancelButton").style.display = "inline-flex";
}

function cancelAddressEdit() {
    resetAddressForm();
}

async function submitAddress() {
    if (editingAddressId) {
        await updateAddress();
    } else {
        await saveAddress();
    }
}

async function saveAddress() {
    try {
        const address = document.getElementById("address").value.trim();
        const detailAddress = document.getElementById("detailAddress").value.trim();

        if (!address) {
            alert("기본 주소를 입력해주세요.");
            return;
        }

        if (!detailAddress) {
            alert("상세 주소를 입력해주세요.");
            return;
        }

        const result = await apiRequest(
            CONFIG.ENDPOINTS.addressCreate,
            "POST",
            { address, detailAddress },
            true
        );

        printResult(result);
        alert("주소가 등록되었습니다.");

        resetAddressForm();
        await loadAddress();
    } catch (e) {
        printResult(e.message);
        alert(e.message);
    }
}

async function updateAddress() {
    try {
        const address = document.getElementById("address").value.trim();
        const detailAddress = document.getElementById("detailAddress").value.trim();

        if (!editingAddressId) {
            alert("수정할 주소가 선택되지 않았습니다.");
            return;
        }

        if (!address) {
            alert("기본 주소를 입력해주세요.");
            return;
        }

        if (!detailAddress) {
            alert("상세 주소를 입력해주세요.");
            return;
        }

        const result = await apiRequest(
            `${CONFIG.ENDPOINTS.addressUpdate}`,
            "PATCH",
            { address, detailAddress },
            true
        );

        printResult(result);
        alert("주소가 수정되었습니다.");

        resetAddressForm();
        await loadAddress();
    } catch (e) {
        printResult(e.message);
        alert(e.message);
    }
}

async function deleteAddress(addressId) {
    const confirmed = confirm("등록된 배송지를 삭제하시겠습니까?");
    if (!confirmed) return;

    try {
        const result = await apiRequest(
            `${CONFIG.ENDPOINTS.addressDelete}/${addressId}`,
            "DELETE",
            null,
            true
        );

        printResult(result);
        alert("주소가 삭제되었습니다.");

        if (editingAddressId && Number(editingAddressId) === Number(addressId)) {
            resetAddressForm();
        }

        currentAddress = null;
        renderAddress(null);
    } catch (e) {
        printResult(e.message);
        alert(e.message);
    }
}

async function loadAddress() {
    try {
        const result = await apiRequest(
            CONFIG.ENDPOINTS.addressList,
            "GET",
            null,
            true
        );

        currentAddress = result || null;
        printResult(result);
        renderAddress(currentAddress);
    } catch (e) {
        currentAddress = null;
        printResult(e.message);
        renderAddress(null);
    }
}

function renderAddress(address) {
    const container = document.getElementById("currentAddressBox");
    if (!container) return;

    if (!address || !address.id) {
        container.innerHTML = `<p class="form-note">등록된 배송지가 없습니다.</p>`;
        return;
    }

    container.innerHTML = `
    <div class="address-card">
      <div class="address-card-header">
        <span class="badge">ADDRESS</span>
      </div>

      <h4 class="address-main">${escapeHtml(address.address || "-")}</h4>
      <p class="address-detail">${escapeHtml(address.detailAddress || "-")}</p>

      <div class="address-meta">
        <span>위도: ${address.latitude ?? "-"}</span>
        <span>경도: ${address.longitude ?? "-"}</span>
      </div>

      <div class="actions" style="margin-top: 12px;">
        <button
          type="button"
          class="small-btn secondary"
          onclick="setAddressEditMode(currentAddress)"
        >
          수정
        </button>

        <button
          type="button"
          class="small-btn ghost danger-outline"
          onclick="deleteAddress(${address.id})"
        >
          삭제
        </button>
      </div>
    </div>
  `;
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}