import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class HouseNoCom {

    private static final String RULES_AND = "&";
    private static final String COMMON_AND = ",";

    /**
     * 规则转换成map，不能组合的可能性（组合中不存在查询的房间）
     *
     * @param combineRules 組合規則
     * @param houseNos     查詢的房間
     * @return
     */
    public Map<String, List<String>> rulesToMap(String combineRules, String houseNos) {
        return Arrays.stream(combineRules.split(COMMON_AND))
                .filter(r -> existHouseNo(houseNos, r.split(RULES_AND)[0]) && existHouseNo(houseNos, r.split(RULES_AND)[1]))
                .collect(Collectors.groupingBy(d -> d.split(RULES_AND)[0], Collectors.toCollection(ArrayList::new)));
    }

    /**
     * 判断房号是否存在
     *
     * @param houseNos
     * @param houseNo
     * @return
     */
    public boolean existHouseNo(String houseNos, String houseNo) {
        if (houseNo == null || houseNos == null) {
            return false;
        }
        return houseNos.contains(houseNo);
    }

    public Map<String, String> getCombine(Map<String, List<String>> rulesMap, String houseNo, Set<String> outSet, Map<String, String> map) {
        List<String> ruleList = rulesMap.get(houseNo);
        if (Optional.ofNullable(ruleList).isPresent() && ruleList.size() > 0) {
            ruleList.forEach(r -> {
                String endFix = r.split(RULES_AND)[1];
                if (!Optional.ofNullable(map.get(endFix)).isPresent()) {
                    map.put(endFix, endFix);
                    outSet.add(map.keySet().stream().collect(Collectors.joining(RULES_AND)));
                    map.putAll(getCombine(rulesMap, r.split(RULES_AND)[1], outSet, map));
                }
            });
        }
        return map;
    }

    @Test
    public void outRules() {
        String combineRules1 = "101&102,102&103,103&104,104&101,104&105";
        String houseNos1 = "101,102,103,104";

        String combineRules = "101&102,102&103,101&201,201&202,202&203";
        String houseNos = "101,102,201,202,203";

        String combineRules2 = "101&102,102&103";
        String houseNos2 = "101,101,102";


        Map<String, List<String>> rulesMap = rulesToMap(combineRules, houseNos);

        /*存放组合houseId*/
        final Set<String> outSet = new HashSet<>();

        /*单间的添加*/
        Arrays.stream(houseNos.split(COMMON_AND)).forEach(d -> {
            outSet.add(d);
            /*两间及以上的组合*/
            Map<String, String> map = new HashMap<>();
            map.put(d, d);
            getCombine(rulesMap, d, outSet, map);
        });

        String out = outSet.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.joining(COMMON_AND));

        System.out.println("最终结果：\n" + out);
    }
}
