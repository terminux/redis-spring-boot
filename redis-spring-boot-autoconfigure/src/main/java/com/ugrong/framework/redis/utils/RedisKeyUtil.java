package com.ugrong.framework.redis.utils;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class RedisKeyUtil {

	public static String concatKey(String prefix, String delimiter, String suffix) {
		if (suffix.startsWith(delimiter)) {
			return prefix.concat(suffix);
		}
		return prefix.concat(delimiter).concat(suffix);
	}
}
