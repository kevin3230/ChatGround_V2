# CHATGROUND_V2

###概述
    訪客可註冊會員,登入後可在聊天室與不特定多人聊天。

CHATGROUND_V2是將另一個作品CHATGROUND 的架構改寫為Spring Boot, `會員授權與認證`採用spring security實現RBAC模型
, 密碼使用BCrypt加密, security 預設啟用CSRF防護。 `聊天室`用WebSocket雙工傳輸訊息, 訊息在後端先存在Redis中, 
Redis啟用RDB機制, 週期性對redis進行快照完成持久化, 並且寫排程設定上午2點執行將Redis的聊天訊息用Batch insert存入MySQL。

###使用技術
- Spring Boot
    - JPA
    - Security
    - WebSocket
    - Redis
    
- Thymeleaf
