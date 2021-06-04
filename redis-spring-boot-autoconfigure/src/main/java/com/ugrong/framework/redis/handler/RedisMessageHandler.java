package com.ugrong.framework.redis.handler;

import java.io.Serializable;

public interface RedisMessageHandler<T extends Serializable> {

    void handle(T payload, String topic);

}
