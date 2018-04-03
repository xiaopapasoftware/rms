import org.junit.Test;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class HouseNoCom {

    private static final String RULES_HOUSE = "\\|";
    private static final String RULES_AND = "&";
    private static final String COMMON_AND = ",";
    private static final String[] HOUSE_NO_END = {"A", "B", "C", "D", "E", "F", "E"};

    /**
     * 规则转换成map,过滤掉不需要参与的规则
     *
     * @param combineRules 組合規則
     * @param houseNoMap   查詢的房間
     * @return
     */
    private Map<String, List<String>> rulesToMap(String combineRules, Map<String, List<String>> houseNoMap) {
        return Arrays.stream(combineRules.split(COMMON_AND))
                .filter(r -> existHouseNo(houseNoMap, r.split(RULES_AND)[0]) && existHouseNo(houseNoMap, r.split(RULES_AND)[1]))
                .collect(Collectors.groupingBy(d -> d.split(RULES_AND)[0], Collectors.toCollection(ArrayList::new)));
    }

    private Map<String, List<String>> rulesToMap1(String combineRules, Map<String, List<String>> houseNoMap) {
        Set<String> ruleSet = new HashSet<>();
        List<String> ruleList = Arrays.stream(combineRules.split(COMMON_AND))
                .filter(r -> existHouseNo(houseNoMap, r.split(RULES_AND)[0]) && existHouseNo(houseNoMap, r.split(RULES_AND)[1]))
                .collect(Collectors.toList());

        ruleSet.addAll(ruleList);

        ruleList.forEach(r -> {
            String[] rules = r.split(RULES_AND);
            ruleSet.add(String.join(RULES_AND, rules[1], rules[0]));
        });
        return ruleSet.stream().collect(Collectors.groupingBy(d -> d.split(RULES_AND)[0], Collectors.toCollection(ArrayList::new)));
    }


    /**
     * houseNo 转 map 便于重复情况的处理
     *
     * @param houseNos
     * @return
     */
    private Map<String, List<String>> houseToMap(String houseNos) {
        return Arrays.stream(houseNos.split(COMMON_AND))
                .collect(Collectors.groupingBy(d -> d, Collectors.toCollection(ArrayList::new)));
    }

    /**
     * 判断房号是否存在
     *
     * @param houseNoMap
     * @param houseNo
     * @return
     */
    private boolean existHouseNo(Map<String, List<String>> houseNoMap, String houseNo) {
        if (Optional.ofNullable(houseNoMap.get(houseNo)).isPresent()) {
            return true;
        }
        return false;
    }

    /**
     * 房号组合规则
     *
     * @param rulesMap
     * @param houseNo
     * @param outSet
     * @param map
     * @return
     */
    private Map<String, String> combineHouse(Map<String, List<String>> rulesMap, String houseNo, Set<String> outSet, Map<String, String> map) {
        List<String> ruleList = rulesMap.get(houseNo);
        if (Optional.ofNullable(ruleList).isPresent()) {
            ruleList.forEach(r -> {
                String endFix = r.split(RULES_AND)[1];
                if (!Optional.ofNullable(map.get(endFix)).isPresent()) {
                    map.put(endFix, endFix);
                    outSet.add(map.keySet().stream().collect(Collectors.joining(RULES_AND)));
                    map.putAll(combineHouse(rulesMap, r.split(RULES_AND)[1], outSet, map));
                }
            });
        }
        return map;
    }

    /**
     * 房屋组合
     *
     * @param rules    规则
     * @param houseNos 房号
     * @return
     */
    public String combineHouse(String rules, String houseNos) {

        Map<String, List<String>> houseMap = houseToMap(houseNos);

        Map<String, List<String>> rulesMap = rulesToMap1(rules, houseMap);

        /*存放组合houseId*/
        final Set<String> outSet = new HashSet<>();

        houseMap.keySet().stream().forEach(h -> {
            outSet.add(h);

            /*两间及以上的组合*/
            Map<String, String> map = new HashMap<>();
            map.put(h, h);
            combineHouse(rulesMap, h, outSet, map);
        });

        /*重复情况的处理*/
        houseMap.forEach((k, v) -> {
            if (v.size() > 1) {
                String houseNo = v.get(0);
                List<String> house = outSet.stream().filter(h -> h.contains(houseNo)).collect(Collectors.toList());
                outSet.removeAll(house);
                house.forEach(h -> {
                    for (int i = 0; i < v.size(); i++) {
                        outSet.add(h.replace(houseNo, houseNo + "-" + HOUSE_NO_END[i]));
                    }
                });
            }
        });

        String out = outSet.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.joining(COMMON_AND));
        return out;
    }

    public void readFile(String filePath) {
        List<String> input = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        FileReader reader = null;
        BufferedReader br = null;
        try {
            reader = new FileReader(filePath);
            br = new BufferedReader(reader);
            String str = null;
            while ((str = br.readLine()) != null) {
                input.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        input.forEach(s -> {
            String[] rulesHouse = s.split(RULES_HOUSE);
            String out = combineHouse(rulesHouse[0], rulesHouse[1]);
            sb.append(out).append("\r\n");
        });
        writeFile(sb.toString());
    }

    private void writeFile(String content) {
        BufferedWriter out = null;
        try {
            File file = new File(".\\output.txt");
            file.createNewFile();
            out = new BufferedWriter(new FileWriter(file));
            out.write(content);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        String path = "E:\\test.txt";
        readFile(path);
        System.out.println("总共耗时:" + (System.currentTimeMillis() - start));
    }

    @Test
    public void test1() {
        String s = "101&102,102&103,103&104,101&104,104&105|101,102,103,104";
        String[] rulesHouse = s.split(RULES_HOUSE);
        String out = combineHouse(rulesHouse[0], rulesHouse[1]);
        System.out.println(out);
    }

}
