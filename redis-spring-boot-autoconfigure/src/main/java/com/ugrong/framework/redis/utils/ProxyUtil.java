/**
 * MIT License
 *
 * Copyright (c) 2019-2021 ugrong@163.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
