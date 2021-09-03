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

package com.ugrong.framework.redis.samples.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.ugrong.framework.redis.annotation.RedisLock;
import com.ugrong.framework.redis.samples.service.IStudentService;

@Service
public class IStudentServiceImpl implements IStudentService {

	@Override
	@RedisLock(lockType = "STUDENT_LOCK", lockField = "123")
	public void testAnonLock() {
		System.out.println("获取到锁，当前线程名称：" + Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(3);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
