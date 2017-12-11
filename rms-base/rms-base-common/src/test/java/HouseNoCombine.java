import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

public class HouseNoCombine {

    private static final String RULES_AND = "&";
    private static final String OUT_AND = "&&";
    private static final String COMMON_AND = ",";

    public Map<String, String> rulesToMap(String combineRules) {
        return Arrays.stream(combineRules.split(COMMON_AND))
                .collect(Collectors.toMap(s -> s.split(RULES_AND)[0], v -> v));
    }


    public Map<String, String> getCombineNo(final Map<String, String> rules, String houseNos, String houseNo) {
        Map<String, String> combineNoMap = new HashMap<>();
        combineNoMap.put(houseNo, houseNo);
        String rule = rules.get(houseNo);
        if (rule != null && !"".equals(rule)) {
            String endRule = rule.split(RULES_AND)[1];
            /*判断是否已经存在,对循环关联房间的处理*/
            String exist = combineNoMap.get(endRule);
            if (houseNos.contains(endRule) && exist == null) {
                combineNoMap.putAll(getCombineNo(rules, houseNos, endRule));
            }
        }
        return combineNoMap;
    }


    public static SortedSet<String> combineAlgorithm(String[] str) {
        SortedSet<String> set = new TreeSet<>();

        int nCnt = str.length;
        //int nBit = (0xFFFFFFFF >>> (32 - nCnt));
        int nBit = 1 << nCnt;
        for (int i = 1; i <= nBit; i++) {
            for (int j = 0; j < nCnt; j++) {
                if ((i << (31 - j)) >> 31 == -1) {
                    String out = new String(str[j] + OUT_AND);
                    set.add(out);
                    System.out.print(out);
                }
            }
            System.out.println("");
        }
        return set;
    }

    @Test
    public void outRules() {
        String combineRules = "101&102,102&103";
        String houseNos = "101,102,103                                                                                               ss44                                                ";

        final Map<String, String> rulesMap = rulesToMap(combineRules);
        final SortedSet<String> set = new TreeSet<>();

        Arrays.stream(houseNos.split(COMMON_AND)).forEach(d -> {
            Map<String, String> combineHouseNo = getCombineNo(rulesMap, houseNos, d);
            Set<String> strSet = combineHouseNo.keySet();
            //combineAlgorithm(strSet.toArray(new String[]{}));
            set.addAll(combineAlgorithm(strSet.toArray(new String[]{})));
        });
        String out = set.stream().map(x -> x.substring(0, x.length() - 2)).collect(Collectors.joining(COMMON_AND));
        System.out.println("最终结果：\n" + out);
    }

}
