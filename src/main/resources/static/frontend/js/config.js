const CONFIG = {
  BASE_URL: "http://localhost:8080",

  ENDPOINTS: {
    emailRequest: "/api/v1/email/request",
    emailVerify: "/api/v1/email/verify",
    signup: "/api/v1/users/signup",
    login: "/api/v1/auth/login",
    logout: "/api/v1/auth/logout",
    me: "/api/v1/users/me",
    categories: "/api/v1/categories",

    createOrder: "/api/v1/orders",
    myOrders: (userId) => `/api/v1/users/${userId}/orders`,
    myOrderDetail: (userId, orderId) => `/api/v1/users/${userId}/orders/${orderId}`,
    updateOrderStatus: (orderId, action) => `/api/v1/orders/${orderId}/${action}`,

    createReview: "/api/v1/reviews",

    // 아래 두 개는 네가 제공한 컨트롤러에서 GET 조회가 안 보여서 임시값
    // 실제 백엔드 GET 조회 API에 맞게 수정 필요
    storesByCategory: (categoryId) => `/api/v1/categories/${categoryId}/stores`,
    menusByStore: (storeId) => `/api/v1/stores/${storeId}/menus`
  }
};