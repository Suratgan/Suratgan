window.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("accessToken");

    if (!token) {
        alert("로그인이 필요합니다.");
        window.location.href = "./login.html";
    }
});

function printResult(data) {
    document.getElementById("result").textContent =
        typeof data === "string" ? data : JSON.stringify(data, null, 2);
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

        const payload = {
            address,
            detailAddress
        };

        const result = await apiRequest(
            CONFIG.ENDPOINTS.addressCreate,
            "POST",
            payload,
            true
        );

        printResult(result);
        alert("주소가 등록되었습니다.");

        document.getElementById("address").value = "";
        document.getElementById("detailAddress").value = "";
    } catch (e) {
        printResult(e.message);
        alert(e.message);
    }
}