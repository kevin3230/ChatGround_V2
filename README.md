# CHATGROUND_V2

### 概述
	訪客可註冊會員,登入後可在聊天室與不特定多人聊天。

CHATGROUND_V2是將另一個作品CHATGROUND 的架構改寫為Spring Boot, **會員授權與認證**採用spring security實現RBAC模型,
會員密碼使用BCrypt加密, security 預設啟用CSRF防護。 **聊天室**用WebSocket雙工傳輸訊息, 會員帳號與暱稱放在JWT中確保不會被修改, 聊天訊息在後端存在Redis中,
並且執行排程將Redis的聊天訊息用Batch insert存入資料庫。資料庫連線資訊用Jasypt加解密。

### 使用技術
- Spring Boot
    - JPA
    - Security
    - WebSocket
    - Redis
    - JWT
    
- Thymeleaf

### 使用說明
使用Java 17開發。初次使用需初始化網站，設定管理員帳號。
