package com.ugrong.framework.redis.samples.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class Student implements Serializable {

    private Long id;

    private String name;
}