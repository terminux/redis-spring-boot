package com.ugrong.framework.redis.samples.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Student implements Serializable {

	private Long id;

	private String name;
}