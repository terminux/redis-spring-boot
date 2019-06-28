package com.ugrong.framework.redis.lock;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

import static java.util.stream.Collectors.joining;

@Getter
public class RedisLockScript implements Serializable {

    @Value("classpath:/lua/expandLock.lua")
    public Resource lockScriptResource;

    @Value("classpath:/lua/unlock.lua")
    public Resource unlockScriptResource;

    private String expandLockLuaScript;

    private String unlockLuaScript;

    @PostConstruct
    public void initScript() throws IOException {
        this.expandLockLuaScript = this.reader(lockScriptResource);
        this.unlockLuaScript = this.reader(unlockScriptResource);
    }

    private String reader(Resource scriptResource) throws IOException {
        return new BufferedReader(new InputStreamReader(scriptResource.getInputStream()))
                .lines().collect(joining(" "));
    }
}
