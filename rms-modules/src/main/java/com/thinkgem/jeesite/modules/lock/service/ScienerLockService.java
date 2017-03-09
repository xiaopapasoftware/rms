package com.thinkgem.jeesite.modules.lock.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.PropertiesLoader;
import com.thinkgem.jeesite.modules.lock.utils.HttpRequestUtil;

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


    @SuppressWarnings({ "rawtypes", "unchecked" })
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
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


    @SuppressWarnings({ "unchecked", "rawtypes" })
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Map> getLockList() {
        Map authRes = this.managerAuthorize();
        if(authRes== null || authRes.get("access_token")== null){
            log.error("sciener authorize failed");
            return null;
        }
        String accessToken = (String) authRes.get("access_token");
        int openId = (Integer) authRes.get("openid");
        Map<String, Object> map = Maps.newHashMap();
        map.put("client_id", clientId);
        map.put("access_token", accessToken);
        map.put("openid",openId);
        map.put("date", System.currentTimeMillis());
        try {
            String s  = HttpRequestUtil.readContentFromPost(lockScienerV1, "room/list", map);
            Map res = (Map) JsonMapper.fromJsonString(s, Map.class);
            log.debug("==================getLockList:" + res);
            if(res!= null && res.get("list")!= null){
                return (List<Map>) res.get("list");
            }else{
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public List<Map> getAllKeys() {
        List<Map> locks= this.getLockList();
        if(locks== null){
            return null;
        }
        List<Map> keys = new ArrayList<Map>();
        Map authRes = this.managerAuthorize();
        if(authRes== null || authRes.get("access_token")== null){
            log.error("sciener authorize failed");
            return null;
        }

//        for(Map lock: locks){
//            Map<String, Object> map = Maps.newHashMap();
//            map.put("clientId", clientId);
//            map.put("accessToken", accessToken);
//            map.put("lockId",lock.get("room_id"));
//            map.put("date", System.currentTimeMillis());
//            try {
//                String s2 = HttpRequestUtil.readContentFromPost(lockScienerV2, "lock/listAllKey", map);
//                Map res2 = (Map) JsonMapper.fromJsonString(s2, Map.class);
//                if(res2.get("list")!= null) {
//                    ArrayList<Map> tmpkeys = (ArrayList<Map>) res2.get("list");
//                    for(Map key: tmpkeys) {
//                        Long keyDate = (Long) key.get("date");
//                        key.putAll(lock);
//                        key.put("date",keyDate );
//                    }
//                    keys.addAll(tmpkeys);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return keys;
    }

    //分配钥匙
    //返回参数 errcode int 错误码 0为成功 ; errmsg String 错误信息
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map<String, Object> sendKey(int lockId, String user, long startDate, long endDate, String remarks) {
        Map authRes = this.managerAuthorize();
        if(authRes== null || authRes.get("access_token")== null){
            log.error("sciener authorize failed");
            return null;
        }
        String accessToken = (String) authRes.get("access_token");
        Map<String, Object> map = Maps.newHashMap();
        map.put("clientId", clientId);
        map.put("accessToken", accessToken);
        map.put("lockId",lockId);
        map.put("receiverUsername",user);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("remarks",remarks);
        map.put("date", System.currentTimeMillis());

        try {
            String s  = HttpRequestUtil.readContentFromPost(lockScienerV2, "key/send", map);
            Map res = (Map) JsonMapper.fromJsonString(s, Map.class);
            log.debug("==================sendKey:" + res);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //删除钥匙
    //返回参数 errcode int 错误码 0为成功 ; errmsg String 错误信息
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map<String, Object> deleteKey(int lockId, int openid, int keyId) {
        Map authRes = this.managerAuthorize();
        if(authRes== null || authRes.get("access_token")== null){
            log.error("sciener authorize failed");
            return null;
        }
        String accessToken = (String) authRes.get("access_token");
        Map<String, Object> map = Maps.newHashMap();
        map.put("clientId", clientId);
        map.put("accessToken", accessToken);
        map.put("lockId",lockId);
        map.put("openid",openid);
        map.put("keyId",keyId);
        map.put("date", System.currentTimeMillis());

        try {
            String s  = HttpRequestUtil.readContentFromPost(lockScienerV2, "key/delete", map);
            Map res = (Map) JsonMapper.fromJsonString(s, Map.class);
            log.debug("==================deleteKey:" + res);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Map> getAllKeysByLockId(String lockId) {
        List<Map> tmpkeys = null;
        Map authRes = this.managerAuthorize();
        if(authRes== null || authRes.get("access_token")== null){
            log.error("sciener authorize failed");
            return null;
        }
        String accessToken = (String) authRes.get("access_token");


            Map<String, Object> map = Maps.newHashMap();
            map.put("clientId", clientId);
            map.put("accessToken", accessToken);
            map.put("lockId",lockId);
            map.put("date", System.currentTimeMillis());
            try {
                String s2 = HttpRequestUtil.readContentFromPost(lockScienerV2, "lock/listAllKey", map);
                Map res2 = (Map) JsonMapper.fromJsonString(s2, Map.class);

                if(res2.get("list")!= null) {
                    tmpkeys = (ArrayList<Map>) res2.get("list");
                    for(Map key: tmpkeys) {
                        Long keyDate = (Long) key.get("date");
                        //key.putAll(lock);
                        key.put("date",keyDate );
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return tmpkeys;
    }
}
