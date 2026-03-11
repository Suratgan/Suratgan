const CONFIG = {
  BASE_URL: "http://localhost:8080",

  ENDPOINTS: {

    emailRequest: "/api/v1/email/request",
    emailVerify: "/api/v1/email/verify",
    nicknameCheck: "/api/v1/users/nickname-check",
    signup: "/api/v1/users/signup",
    login: "/api/v1/auth/login",
    logout: "/api/v1/auth/logout",

    me: "/api/v1/users/me",

    categories: "/api/v1/categories",

    // 가게 조회
    stores: "/api/v1/stores",

    // 메뉴 조회
    menusByStore: (storeId) => `/api/v1/stores/${storeId}/menus`,

    // 주문
    createOrder: "/api/v1/orders",
    myOrders: (userId) => `/api/v1/users/${userId}/orders`,
    myOrderDetail: (userId, orderId) =>
        `/api/v1/users/${userId}/orders/${orderId}`,

    updateOrderStatus: (orderId, action) =>
        `/api/v1/orders/${orderId}/${action}`,

    // 리뷰
    createReview: "/api/v1/reviews"
  }
};