# CHATGROUND

   這個小專案用來練習架設聊天網站。基於密碼的安全性，會員密碼加上Math.random()隨機產生含特殊字元
的20個字元當作加鹽，再用SHA-1演算法取得雜湊碼儲存在資料庫。使用到Spring框架的ORM模組接管Hibernate ORM。

  It's a web app for practice which is about the chatroom.
Member's password is using SHA-1 to hash with 20 characters salty which is generated by Math.random().
Using Spring framework ORM module associated with Hibernate ORM.