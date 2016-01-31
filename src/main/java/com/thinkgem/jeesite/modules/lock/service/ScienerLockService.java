package com.thinkgem.jeesite.modules.lock.service;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.lock.utils.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import static org.junit.Assert.fail;

/**
 * Created by mabindong on 2016/1/10.
 */
@Service
public class ScienerLockService {

    Logger log = LoggerFactory.getLogger(ScienerLockService.class);

    private String lockScienerV1;
    private String lockScienerV2;
    private String lockScienerUser;
    private String redirectUri;
    private String clientId;
    private String clientSecret;
    private String managerName;
    private String managerPwd;

    @PostConstruct
    public void init(){
        PropertiesLoader proper = new PropertiesLoader("jeesite.properties");
        lockScienerV1 = proper.getProperty("lock.sciener.v1");
        lockScienerV2 = proper.getProperty("lock.sciener.v2");
        lockScienerUser = proper.getProperty("lock.sciener.user");
        redirectUri = proper.getProperty("lock.sciener.redirecturi");
        clientId = proper.getProperty("lock.sciener.clientId");
        clientSecret = proper.getProperty("lock.sciener.clientSecret");
        managerName = proper.getProperty("lock.sciener.managerName");
        managerPwd = proper.getProperty("lock.sciener.managerPwd");
    }


    public Map<String, Object> managerAuthorize() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("redirect_uri", redirectUri);
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        map.put("response_type", "token");
        map.put("username",managerName);
        map.put("password", managerPwd);
        map.put("grant_type", "password");
        try {
            String s = HttpRequestUtil.readContentFromPost("https://api.sciener.cn/oauth2", "token", map);
            Map res = (Map) JsonMapper.fromJsonString(s, Map.class);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  map;
    }

    public Map<String, Object> authorize(String userName, String password) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("redirect_uri", redirectUri);
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        map.put("response_type", "token");
        map.put("username",userName);
        map.put("password", password);
        map.put("grant_type", "password");
        try {
            String s = HttpRequestUtil.readContentFromPost("https://api.sciener.cn/oauth2", "token", map);
            Map res = (Map) JsonMapper.fromJsonString(s, Map.class);
            return res;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  map;
    }


    public Map<String, Object> regUser(String userName, String password) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        map.put("username",userName);
        map.put("password", password);
       try {
            String s  = HttpRequestUtil.readContentFromPost(lockScienerUser, "regUser", map);
            Map res = (Map) JsonMapper.fromJsonString(s, Map.class);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
