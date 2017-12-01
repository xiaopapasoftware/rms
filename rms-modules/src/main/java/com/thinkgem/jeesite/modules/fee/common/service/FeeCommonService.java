package com.thinkgem.jeesite.modules.fee.common.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.filter.search.Constants;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cache.MyCache;
import com.thinkgem.jeesite.modules.cache.MyCacheBuilder;
import com.thinkgem.jeesite.modules.cache.enums.MyCacheConstant;
import com.thinkgem.jeesite.modules.common.entity.SelectItem;
import com.thinkgem.jeesite.modules.contract.enums.RentModelTypeEnum;
import com.thinkgem.jeesite.modules.entity.Area;
import com.thinkgem.jeesite.modules.fee.common.dao.FeeCommonDao;
import com.thinkgem.jeesite.modules.fee.config.entity.FeeConfig;
import com.thinkgem.jeesite.modules.fee.config.service.FeeConfigService;
import com.thinkgem.jeesite.modules.fee.enums.FeeUnitEnum;
import com.thinkgem.jeesite.modules.inventory.entity.House;
import com.thinkgem.jeesite.modules.inventory.entity.Room;
import com.thinkgem.jeesite.modules.inventory.enums.HouseStatusEnum;
import com.thinkgem.jeesite.modules.inventory.service.HouseService;
import com.thinkgem.jeesite.modules.inventory.service.RoomService;
import com.thinkgem.jeesite.modules.service.AreaService;
import com.thinkgem.jeesite.modules.utils.DictUtils;
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

    public static final int FULL_MOUTH_DAYS = 30;

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

    public static final String FEE_CONFIG_CACHE_KEY = "fee_config_cache_key";

    private MyCache feeCache = MyCacheBuilder.getInstance().getSoftCache(MyCacheConstant.FEE_CACHE);

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

    /*获取所有合作的房间，只包括已出租的*/
    public List<Room> getJoinRentAllRoom(){
        return feeCommonDao.getJoinRentAllRoom();
    }

    /*获取所有整租的房间，只包括已出租的*/
    public List<House> getWholeRentAllHouse(){
        House query = new House();
        query.setIntentMode(RentModelTypeEnum.WHOLE_RENT.getValue());
        List<House> houses = houseService.findList(query);
        return houses.stream()
                .filter(h -> (StringUtils.equals(h.getHouseStatus(),HouseStatusEnum.WHOLE_RENT.getValue()) || StringUtils.equals(h.getHouseStatus(),HouseStatusEnum.PART_RENT.getValue())))
                .collect(Collectors.toList());
    }

    public List<Map> getHouseByAccountNumAndNumType(String accountNum, String numType) {
        return houseService.getHouseByAccountNumAndNumType(accountNum, numType);
    }

    public List<SelectItem> getAreaWithAuthByType(String type) {
        Area queryArea = new Area();
        queryArea.setType(type);
        List<Area> areas = areaService.getAreaWithAuth(queryArea);
        return areas.stream().map(area -> new SelectItem(area.getId(), area.getName())).collect(Collectors.toList());
    }

    public FeeConfig getFeeConfig(FeeUnitEnum feeUnitEnum, String houseId, String roomId) {
        if (feeCache.getObject(getCacheKey(feeUnitEnum, houseId, roomId)) != null) {
            return (FeeConfig) feeCache.getObject(getCacheKey(feeUnitEnum, houseId, roomId));
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
            if(StringUtils.isBlank(id) || StringUtils.equals(id,"0")){
                continue;
            }
            String key = id + "_" + feeUnitEnum.getValue();
            FeeConfig feeConfig = feeConfigMap.get(key);
            if (Optional.ofNullable(feeConfig).isPresent()) {
                retFeeConfig = feeConfig;
                break;
            }
        }

        /*没有找到特殊的配置,就取默认配置*/
        if (!Optional.ofNullable(retFeeConfig).isPresent()) {
            String key = 0 + "_" + feeUnitEnum.getValue();
            FeeConfig feeConfig = feeConfigMap.get(key);
            if (Optional.ofNullable(feeConfig).isPresent()) {
                retFeeConfig = feeConfig;
            }
        }

        if (!Optional.ofNullable(retFeeConfig).isPresent()) {
            logger.error("{}没有找到费用配置项", feeUnitEnum.getName());
            throw new IllegalArgumentException(feeUnitEnum.getName() + "没有找到费用配置");
        }

        /*加入到缓存*/
        feeCache.putObject(getCacheKey(feeUnitEnum, houseId, roomId), retFeeConfig);

        return retFeeConfig;
    }

    private String getCacheKey(FeeUnitEnum feeUnitEnum, String houseId, String roomId) {
        if (!Optional.ofNullable(houseId).isPresent()) {
            logger.error("查询费用配置房屋ID不能为空");
            throw new IllegalArgumentException("查询费用配置房屋ID不能为空");
        }

        if (Optional.ofNullable(roomId).isPresent() && !StringUtils.equals(roomId, "0")) {
            return Joiner.on("_").join(roomId, houseId, feeUnitEnum.getValue());
        }
        return Joiner.on("_").join(houseId, feeUnitEnum.getValue());
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
        queryConfig.setConfigStatus(Constants.YES);
        List<FeeConfig> feeConfigs = feeConfigService.findList(queryConfig);
        Map<String, FeeConfig> feeConfigMap = Maps.uniqueIndex(feeConfigs,
                feeConfig -> feeConfig.getBusinessId() + "_" + feeConfig.getFeeType());

        feeCache.putObject(FEE_CONFIG_CACHE_KEY, feeConfigMap);
        return feeConfigMap;
    }

    public void clearFeeConfigCache() {
        feeCache.clear();
    }

    public boolean isOpenInitFeeData(){
        String feeInitFun = DictUtils.getDictValue("fee_data_init_fun","fee_init","1");
        if(StringUtils.equals(feeInitFun,"0")){
            return true;
        }else{
            return false;
        }
    }
}
