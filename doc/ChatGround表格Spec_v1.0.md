## ChatGround Table Spec

### MEMBER 會員
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|id      |會員編號  |number     |PRIMARY KEY,NOT NULL| |
|account      |帳號  |VARCHAR(20)    |NOT NULL| |
|birth      |生日  |date    |NULL| |
|email      |電子信箱  |VARCHAR(30)    |NOT NULL| |
|gender      |性別  |VARCHAR(255)    |NULL| |
|nick_name      |暱稱  |VARCHAR(10)    |NOT NULL| |
|password      |密碼  |VARCHAR(255)    |NOT NULL| |
|picture      |大頭照  |BLOB    |NULL| |
|reg_date      |註冊日期  |datetime    |NULL| |
|status      |帳號狀態  |VARCHAR(255)    |NOT NULL| |



### CHAT_ROOM 聊天室，預備表格，尚未用到
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|id      |聊天室編號  |number     |PRIMARY KEY,NOT NULL| |
|nick_name      |聊天室名稱  |VARCHAR(20)    |NOT NULL| |



### GROUND_MESSAGE 群體聊天室訊息
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|id      |群體聊天室編號  |number     |PRIMARY KEY,NOT NULL| |
|content      |訊息內容  |VARCHAR(500)    |NOT NULL| |
|sender      |發信者  |number    |NOT NULL| |
|time      |發信時間  |datetime    |NULL| |



### MEM_CHAT_ROOM_SET 會員_聊天室關聯表格，預備表格，尚未用到
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|id      |編號  |number     |PRIMARY KEY,NOT NULL| |
|cr_id      |聊天室編號  |number    |FOREIGN KEY, NOT NULL| |
|mem_id      |會員編號  |number    |FOREIGN KEY, NOT NULL| |



### MEMBER_CHATROOM 會員_聊天室關聯表格，預備表格，尚未用到
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|cr_id      |聊天室編號  |number    |PRIMARY KEY, FOREIGN KEY, NOT NULL| |
|mem_id      |會員編號  |number    |PRIMARY KEY, FOREIGN KEY, NOT NULL| |



### MEMBER_SYSROLE 會員_系統角色關聯表格
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|role_id      |角色編號  |number    |FOREIGN KEY, NOT NULL| |
|mem_id      |會員編號  |number    |FOREIGN KEY, NOT NULL| |



### MESSAGE 聊天訊息，預備表格，尚未用到
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|id      |訊息編號  |number     |PRIMARY KEY,NOT NULL| |
|content      |訊息內容  |VARCHAR(500)    |NOT NULL| |
|sender      |發信者  |number    |NOT NULL| |
|status      |訊息狀態  |VARCHAR(255)    |NOT NULL|失敗,已讀,未讀,收回,刪除 |
|time      |發信時間  |datetime    |NULL| |
|chatroom_id      |聊天室編號  |number    |FOREIGN KEY, NULL| |



### SYS_PERMISSION 系統權限
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|id      |權限編號  |number     |PRIMARY KEY,NOT NULL| |
|available      |狀態  |boolean    | NULL| |啟用或停用
|name      |名稱  |VARCHAR(255)    |NULL| |
|parent_id      |父編號  |number    |NOT NULL| |
|parent_ids      |父編號列表  |VARCHAR(255)    |NULL| |
|permission      |性別  |VARCHAR(255)    |NULL| |
|resource_type      |資源類型  |VARCHAR(255)    |NULL| |
|url      |資源路徑  |VARCHAR(255)    |NULL| |



### SYS_ROLE 系統角色
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|id      |角色編號  |number     |PRIMARY KEY,NOT NULL| |
|available      |狀態  |boolean    | NULL| |角色是否可用，啟用或停用
|cn_name      |通用名稱  |VARCHAR(255)    |NULL| |common name
|description      |描述  |VARCHAR(255)    |NULL| |
|role      |角色名稱  |VARCHAR(255)    |NOT NULL| |



### SYS_ROLE_PERMISSION 系統角色_系統權限關聯表格
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|role_id      |角色編號  |number    |FOREIGN KEY, NOT NULL| |
|permission_id      |權限編號  |number    |FOREIGN KEY, NOT NULL| |



### SYSTEM_NOTIFICATION 系統推播，預備表格，尚未用到
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|id      |訊息編號  |number     |PRIMARY KEY,NOT NULL| |
|content      |推播內容  |VARCHAR(500)    |NOT NULL| |
|time      |發信時間  |datetime    |NULL| |



### CUSTOM_PROPERTIES 客製化網站參數
|Column Name |中文名稱 | Column Type |限制 |備註 |
|---         |---      |---         |---  |---   |
|domain      |主要分類  |VARCHAR(10)     |PRIMARY KEY, NOT NULL| |
|type      |次要分類  |VARCHAR(255)    |PRIMARY KEY, NOT NULL| |
|common_name      |通用名稱  |VARCHAR(255)    |NOT NULL| |
|description      |描述  |VARCHAR(500)    |NULL| |
|hard_code      |Hard Code  |VARCHAR(5)    |NOT NULL| |是否為Hard Code。Y:是，N:否
|created_by      |新增者  |number    |FOREIGN KEY, NOT NULL|值域:member_id |
|modified_by      |編輯者  |number    |FOREIGN KEY, NOT NULL|值域:member_id |
|created_time      |新增時間  |datetime    |NOT NULL| |
|modified_time      |修改時間  |datetime    |NOT NULL| |
