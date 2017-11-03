package com.thinkgem.jeesite.modules.fee.common.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.enums.AreaTypeEnum;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cache.MyCache;
import com.thinkgem.jeesite.modules.cache.MyCacheBuilder;
import com.thinkgem.jeesite.modules.cache.enums.MyCacheConstant;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.entity.Area;
import com.thinkgem.jeesite.modules.fee.common.dao.FeeCommonDao;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.config.service.FeeConfigService;
import com.thinkgem.jeesite.modules.fee.enums.ChargeMethodEnum;
import com.thinkgem.jeesite.modules.fee.enums.FeeTypeEnum;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wangganggang
 * @date 2017年09月18日 下午9:57
 */
@Service
public class FeeCommonService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FeeCommonDao feeCommonDao;

    @Autowired
    private HouseService houseService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private AreaService areaService;

    @Autowired
    private FeeConfigService feeConfigService;

    private MyCache feeCache = MyCacheBuilder.getInstance().getSoftCache(MyCacheConstant.FEE_CACHE);

    public static final String FEE_CONFIG_CACHE_KEY = "fee_config_cache_key";

    public House getHouseById(String houseId) {
        return houseService.get(houseId);
    }

    public Room getRoomById(String roomId) {
        return roomService.get(roomId);
    }

    public List<Room> getRoomByHouseId(String houseId) {
        House house = houseService.get(houseId);
        if (StringUtils.equals(house.getIntentMode(), "0")) {
            return Lists.newArrayList();
        }
        return roomService.findRoomListByHouseId(houseId);
    }

    public List<Map> getHouseByQueryWhereAndType(String queryWhere, String type) {
        return houseService.getHouseByQueryWhereAndType(queryWhere, type);
    }

    public List<SelectItem> getAreaWithAuth() {
        Area queryArea = new Area();
        queryArea.setType(AreaTypeEnum.AREA.getValue());
        List<Area> areas = areaService.getAreaWithAuth(queryArea);
        return areas.stream().map(area -> new SelectItem(area.getId(), area.getName())).collect(Collectors.toList());
    }

    public List<SelectItem> getAreaWithAuthByType(String type) {
        Area queryArea = new Area();
        queryArea.setType(type);
        List<Area> areas = areaService.getAreaWithAuth(queryArea);
        return areas.stream().map(area -> new SelectItem(area.getId(), area.getName())).collect(Collectors.toList());
    }

    public FeeConfig getFeeConfig(FeeTypeEnum feeTypeEnum, String houseId, String roomId, ChargeMethodEnum chargeMethodEnum) {
        if (feeCache.getObject(getCacheKey(feeTypeEnum, houseId, roomId, chargeMethodEnum)) != null) {
            return (FeeConfig) feeCache.getObject(getCacheKey(feeTypeEnum, houseId, roomId, chargeMethodEnum));
        }

        Map<String, FeeConfig> feeConfigMap = getFeeConfig();
        String rangeId;
        if (Optional.ofNullable(roomId).isPresent() && !StringUtils.equals(roomId, "0")) {
            rangeId = getRangeIdByRoomId(roomId);
        } else {
            rangeId = getRangeIdByHouseId(houseId);
        }

        FeeConfig retFeeConfig = null;
        for (String id : rangeId.split(",")) {
            String key = id + "_" + feeTypeEnum.getValue();
            FeeConfig feeConfig = feeConfigMap.get(key);
            if (Optional.ofNullable(feeConfig).isPresent() && feeConfig.getChargeMethod() == chargeMethodEnum.getValue()) {
                retFeeConfig = feeConfig;
                break;
            }
        }

        /*没有找到特殊的配置,就取默认配置*/
        if (!Optional.ofNullable(retFeeConfig).isPresent()) {
            String key = 0 + "_" + feeTypeEnum.getValue();
            FeeConfig feeConfig = feeConfigMap.get(key);
            if (Optional.ofNullable(feeConfig).isPresent() && feeConfig.getChargeMethod() == chargeMethodEnum.getValue()) {
                retFeeConfig = feeConfig;
            }
        }

        if (!Optional.of(retFeeConfig).isPresent()) {
            logger.error("{}没有找到费用配置项", feeTypeEnum.getName());
            throw new IllegalArgumentException(feeTypeEnum.getName() + "没有找到费用配置");
        }

        /*加入到缓存*/
        feeCache.putObject(getCacheKey(feeTypeEnum, houseId, roomId, chargeMethodEnum), retFeeConfig);

        return retFeeConfig;
    }

    private String getCacheKey(FeeTypeEnum feeTypeEnum, String houseId, String roomId, ChargeMethodEnum chargeMethodEnum) {
        if (!Optional.ofNullable(houseId).isPresent()) {
            logger.error("查询费用配置房屋ID不能为空");
            throw new IllegalArgumentException("查询费用配置房屋ID不能为空");
        }

        if (Optional.ofNullable(roomId).isPresent() && !StringUtils.equals(roomId, "0")) {
            return Joiner.on("_").join(roomId, houseId, feeTypeEnum.getValue(), chargeMethodEnum.getValue());
        }
        return Joiner.on("_").join(houseId, feeTypeEnum.getValue(), chargeMethodEnum.getValue());
    }

    public FeeConfig getFeeConfig(Map<String, FeeConfig> feeConfigMap1, String rangeId, FeeTypeEnum feeTypeEnum) {
        Map<String, FeeConfig> feeConfigMap = getFeeConfig();

        FeeConfig feeConfig = null;
        for (String id : rangeId.split(",")) {
            String key = id + "_" + feeTypeEnum.getValue();
            feeConfig = feeConfigMap.get(key);
            if (Optional.ofNullable(feeConfig).isPresent()) {
                break;
            }
        }
        if (!Optional.ofNullable(feeConfig).isPresent()) {
            String key = 0 + "_" + feeTypeEnum.getValue();
            feeConfig = feeConfigMap.get(key);
        }

        if (!Optional.of(feeConfig).isPresent()) {
            logger.error("{}没有找到费用配置项", feeTypeEnum.getName());
            throw new IllegalArgumentException(feeTypeEnum.getName() + "没有找到费用配置");
        }
        return feeConfig;
    }

    public String getRangeIdByRoomId(String roomId) {
        return feeCommonDao.getRangeIdByRoomId(roomId);
    }

    public String getRangeIdByHouseId(String houseId) {
        return feeCommonDao.getRangeIdByHouseId(houseId);
    }

    public Map<String, FeeConfig> getFeeConfig() {
        if (feeCache.getObject(FEE_CONFIG_CACHE_KEY) != null) {
            return (Map<String, FeeConfig>) feeCache.getObject(FEE_CONFIG_CACHE_KEY);
        }
        FeeConfig queryConfig = new FeeConfig();
        queryConfig.setConfigStatus(0);
        List<FeeConfig> feeConfigs = feeConfigService.findList(queryConfig);
        Map<String, FeeConfig> feeConfigMap = Maps.uniqueIndex(feeConfigs,
                feeConfig -> feeConfig.getBusinessId() + "_" + feeConfig.getFeeType());

        feeCache.putObject(FEE_CONFIG_CACHE_KEY, feeConfigMap);
        return feeConfigMap;
    }

    public void clearFeeConfigCache() {
        feeCache.clear();
    }
}
