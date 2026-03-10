function setText(id, data) {
  document.getElementById(id).textContent =
      typeof data === "string" ? data : JSON.stringify(data, null, 2);
}

async function loadMe() {
  try {
    const me = await apiRequest(CONFIG.ENDPOINTS.me, "GET", null, true);
    if (me.id) setUserId(me.id);
    setText("meResult", me);
  } catch (e) {
    setText("meResult", e.message);
  }
}

async function logout() {
  try {
    const result = await apiRequest(CONFIG.ENDPOINTS.logout, "POST", null, true);
    removeToken();
    localStorage.removeItem("userId");
    setText("meResult", result);
    alert("로그아웃 완료");
  } catch (e) {
    setText("meResult", e.message);
  }
}

async function loadCategories() {
  try {
    const result = await apiRequest(CONFIG.ENDPOINTS.categories, "GET", null, true);
    setText("categoryResult", result);
  } catch (e) {
    setText("categoryResult", e.message);
  }
}

async function loadStores() {
  try {
    const page = 0;

    const storeName = document.getElementById("storeName")?.value?.trim() || "";
    const latitude = document.getElementById("latitude")?.value?.trim() || "";
    const longitude = document.getElementById("longitude")?.value?.trim() || "";
    const categoryNamesRaw = document.getElementById("categoryNames")?.value?.trim() || "";

    const params = new URLSearchParams();
    params.append("page", page);

    if (storeName) params.append("storeName", storeName);
    if (latitude) params.append("latitude", latitude);
    if (longitude) params.append("longitude", longitude);

    if (categoryNamesRaw) {
      categoryNamesRaw
          .split(",")
          .map(v => v.trim())
          .filter(Boolean)
          .forEach(category => params.append("categoryNames", category));
    }

    const result = await apiRequest(
        `${CONFIG.ENDPOINTS.stores}?${params.toString()}`,
        "GET",
        null,
        true
    );

    setText("storeMenuResult", result);
  } catch (e) {
    setText("storeMenuResult", e.message);
  }
}

async function loadMenus() {
  try {
    const storeId = document.getElementById("storeIdForMenu").value.trim();
    const result = await apiRequest(CONFIG.ENDPOINTS.menusByStore(storeId), "GET", null, true);
    setText("storeMenuResult", result);
  } catch (e) {
    setText("storeMenuResult", `메뉴 조회 실패: ${e.message}`);
  }
}

async function createOrder() {
  try {

    const token = getToken();

    const body = {
      ordererName: document.getElementById("ordererName").value,
      ordererMobile: document.getElementById("ordererMobile").value,
      ordererEmail: document.getElementById("ordererEmail").value,
      storeId: document.getElementById("storeId").value,
      storeName: document.getElementById("storeNameOrder").value,
      storeAddress: document.getElementById("storeAddress").value,
      items: [
        {
          menuId: parseInt(document.getElementById("menuId").value),
          quantity: parseInt(document.getElementById("quantity").value)
        }
      ]
    };

    const response = await fetch(CONFIG.BASE_URL + CONFIG.ENDPOINTS.createOrder, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },
      body: JSON.stringify(body)
    });

    const data = await response.json();

    setText("orderCreateResult", data);

    if (data) {
      if (data.orderId) {
        document.getElementById("paymentOrderId").value = data.orderId;
        document.getElementById("customerKey").value = "SURAGAN_USER_" + String(data.orderId).substring(0, 8);
      }

      if (data.amount) {
        document.getElementById("paymentAmount").value = data.amount;
      }

      if (data.storeName) {
        document.getElementById("paymentOrderName").value = `${data.storeName} 주문`;
      }

      document.getElementById("paymentCurrency").value = "KRW";
      document.getElementById("paymentSuccessUrl").value = `${window.location.origin}/frontend/success.html`;
      document.getElementById("paymentFailUrl").value = `${window.location.origin}/frontend/fail.html`;
    }

  } catch (e) {
    setText("orderCreateResult", `주문 생성 실패: ${e.message}`);
  }
}

async function loadMyOrders() {
  try {
    let userId = getUserId();
    if (!userId) {
      const me = await apiRequest(CONFIG.ENDPOINTS.me, "GET", null, true);
      if (!me.id) throw new Error("users/me 응답에서 id를 찾지 못했습니다.");
      userId = me.id;
      setUserId(userId);
    }

    const result = await apiRequest(CONFIG.ENDPOINTS.myOrders(userId), "GET", null, true);
    setText("myOrdersResult", result);
  } catch (e) {
    setText("myOrdersResult", e.message);
  }
}

async function loadMyOrderDetail() {
  try {
    let userId = getUserId();
    if (!userId) {
      const me = await apiRequest(CONFIG.ENDPOINTS.me, "GET", null, true);
      if (!me.id) throw new Error("users/me 응답에서 id를 찾지 못했습니다.");
      userId = me.id;
      setUserId(userId);
    }

    const orderId = document.getElementById("orderIdDetail").value.trim();
    const result = await apiRequest(CONFIG.ENDPOINTS.myOrderDetail(userId, orderId), "GET", null, true);
    setText("myOrderDetailResult", result);
  } catch (e) {
    setText("myOrderDetailResult", e.message);
  }
}

async function updateOrderStatus() {
  try {
    const orderId = document.getElementById("statusOrderId").value.trim();
    const action = document.getElementById("orderAction").value;

    const result = await apiRequest(
        CONFIG.ENDPOINTS.updateOrderStatus(orderId, action),
        "PATCH",
        null,
        true
    );

    setText("orderStatusResult", result || "상태 변경 완료");
  } catch (e) {
    setText("orderStatusResult", e.message);
  }
}

async function createReview() {
  try {
    const orderId = document.getElementById("reviewOrderId").value.trim();
    const subject = document.getElementById("reviewSubject").value.trim();
    const content = document.getElementById("reviewContent").value.trim();
    const score = Number(document.getElementById("reviewScore").value);

    const result = await apiRequest(
        CONFIG.ENDPOINTS.createReview,
        "POST",
        { orderId, subject, content, score },
        true
    );

    setText("reviewResult", result);
    alert("리뷰 작성 완료");
  } catch (e) {
    setText("reviewResult", e.message);
  }
}

async function requestTossPayment() {
  try {
    if (typeof TossPayments === "undefined") {
      throw new Error("TossPayments 스크립트가 로드되지 않았습니다.");
    }

    const clientKey = "test_ck_eqRGgYO1r5yAWNA95JybrQnN2Eya";
    const tossPayments = TossPayments(clientKey);

    const customerKey = document.getElementById("customerKey").value.trim();
    const method = document.getElementById("paymentMethod").value.trim();
    const amountValue = Number(document.getElementById("paymentAmount").value);
    const currency = document.getElementById("paymentCurrency").value.trim() || "KRW";
    const orderId = document.getElementById("paymentOrderId").value.trim();
    const orderName = document.getElementById("paymentOrderName").value.trim();

    const successUrl =
        document.getElementById("paymentSuccessUrl").value.trim() ||
        `${window.location.origin}/frontend/success.html`;

    const failUrl =
        document.getElementById("paymentFailUrl").value.trim() ||
        `${window.location.origin}/frontend/fail.html`;

    if (!customerKey) throw new Error("customerKey를 입력해주세요.");
    if (!method) throw new Error("결제 수단을 선택해주세요.");
    if (!amountValue || amountValue <= 0) throw new Error("올바른 결제 금액을 입력해주세요.");
    if (!orderId) throw new Error("orderId를 입력해주세요.");
    if (!orderName) throw new Error("orderName을 입력해주세요.");

    setText("paymentResult", {
      customerKey,
      method,
      amount: {
        currency,
        value: amountValue
      },
      orderId,
      orderName,
      successUrl,
      failUrl
    });

    const payment = tossPayments.payment({
      customerKey: customerKey
    });

    await payment.requestPayment({
      method: method,
      amount: {
        currency: currency,
        value: amountValue
      },
      orderId: orderId,
      orderName: orderName,
      successUrl: successUrl,
      failUrl: failUrl
    });
  } catch (e) {
    setText("paymentResult", `결제 요청 실패: ${e.message}`);
  }
}