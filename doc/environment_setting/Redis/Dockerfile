FROM redis:8.6.2
LABEL org.opencontainers.image.authors="Kevin.li" 

# 複製 redis.conf 檔案到容器中
COPY redis.conf /usr/local/etc/redis/redis.conf

# 給予寫入log的權限
RUN mkdir /usr/local/etc/redis/log
RUN chmod 777 /usr/local/etc/redis/log

# 執行 redis server 並且使用自訂的 redis.conf 檔案
CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]