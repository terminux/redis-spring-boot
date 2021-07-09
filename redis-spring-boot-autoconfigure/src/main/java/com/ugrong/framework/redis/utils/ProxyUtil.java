package com.ugrong.framework.redis.utils;

import java.util.Objects;

import lombok.NoArgsConstructor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.target.SingletonTargetSource;

import static lombok.AccessLevel.PRIVATE;

/**
 * The type Proxy util.
 */
@NoArgsConstructor(access = PRIVATE)
public class ProxyUtil {

	/**
	 * 通过代理对象获取真实的类
	 *
	 * @param proxyObject the proxy object
	 * @return origin class
	 */
	public static Class<?> getOriginClass(Object proxyObject) {
		if (Objects.nonNull(proxyObject)) {
			if (proxyObject instanceof Advised) {
				Advised advised = (Advised) proxyObject;
				Object target = ((SingletonTargetSource) (advised).getTargetSource()).getTarget();
				if (Objects.nonNull(target)) {
					return target.getClass();
				}
			}
			else {
				return proxyObject.getClass();
			}
		}
		return null;
	}
}
