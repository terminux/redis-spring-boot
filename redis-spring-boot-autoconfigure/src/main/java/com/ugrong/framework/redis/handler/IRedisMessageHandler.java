package com.ugrong.framework.redis.handler;

import java.io.Serializable;

public interface IRedisMessageHandler<T extends Serializable> {

	void handle(T payload, String topic);

}
