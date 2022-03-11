package com.sim.landlord.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/8/18 15:26
 * @Desc:
 */
public class LandLordsUtils {

    public static void main(String[] args) {
        System.out.println("---*** （〜^㉨^)〜斗地主");
        System.out.println("---*** 该版本为测试版，规则如下");
        System.out.println("---*** 1.字符输入后点击回车键提交,不输入也点击回车键进入下一步");
        System.out.println("---*** 2.暂不支持带牌，如 3334 或 33344456，只支持 333 或 3334444");
        System.out.println("---*** 3.支持识别大小写，如出 JQK 输入 jqk 也可识别");
        System.out.println("---*** 4.出牌不需要输入左边特殊符号，只需输入数字即可");
        System.out.println("---******************************************************---");
        System.out.println("");
        start();
    }

    public static void start() {
        //1、准备牌
        //准备一个map集合，存储牌的索引和组装好的牌
        HashMap<Integer, String> poker = new HashMap<>();
        //准备一个List结合，存储牌的索引
        ArrayList<Integer> pokerIndex = new ArrayList<>();
        //准备两个集合用来存放花色和牌的序号
        ArrayList<String> colors = new ArrayList<>();
        colors.add("♥");
        colors.add("♠");
        colors.add("♣");
        colors.add("♦");

        ArrayList<String> allPok = new ArrayList<>();
        ArrayList<String> nums = new ArrayList<>();
        nums.add("2");
        nums.add("3");
        nums.add("4");
        nums.add("5");
        nums.add("6");
        nums.add("7");
        nums.add("8");
        nums.add("9");
        nums.add("10");
        nums.add("J");
        nums.add("Q");
        nums.add("K");
        nums.add("A");
        //把大王和小王存储到集合中去
        //先定义一个牌的索引
        int index = 0;
        poker.put(index, "大王");
        pokerIndex.add(index);
        index++;
        poker.put(index, "小王");
        pokerIndex.add(index);
        index++;
        //循环嵌套遍历两个集合，组装52张牌。存放到集合中
        for (String num : nums) {
            for (String color : colors) {
                poker.put(index, color + num);
                allPok.add(color + num);
                pokerIndex.add(index);
                index++;
            }
        }
//2、洗牌
        Collections.shuffle(pokerIndex);

        //3、发牌
        //首先需要定义四个集合，用来存放三个玩家，一个底牌
        ArrayList<Integer> player01 = new ArrayList<>();
        ArrayList<Integer> player02 = new ArrayList<>();
        ArrayList<Integer> player03 = new ArrayList<>();
        ArrayList<Integer> dipai = new ArrayList<>();

        for (int i = 0; i < pokerIndex.size(); i++) {
            Integer in = pokerIndex.get(i);
            if (i > 50) {
                dipai.add(in);
            } else if (i % 3 == 0) {
                player01.add(in);
            } else if (i % 3 == 1) {
                player02.add(in);
            } else if (i % 3 == 2) {
                player03.add(in);
            }
        }

        //整牌
        Collections.sort(player01);
        Collections.sort(player02);
        Collections.sort(player03);
        Collections.sort(dipai);

        Map<String, Integer> setMap = new HashMap();
        Map<String, Integer> valueMap = new HashMap();
        for (int i = 3; i <= 10; i++) {
            setMap.put(String.valueOf(i), i);
        }
        setMap.put("J", 11);
        setMap.put("Q", 12);
        setMap.put("K", 13);
        setMap.put("A", 14);
        setMap.put("2", 15);
        setMap.put("小王", 16);
        setMap.put("大王", 17);

        colors.stream().forEach(color -> {
            setMap.keySet().stream().forEach(value -> {
                if (!value.equals("大王") && !value.equals("小王")) {
                    valueMap.put(color + value, setMap.get(value));
                } else {
                    valueMap.put(value, setMap.get(value));
                }
            });
        });
        valueMap.putAll(setMap);


        String local = "";
        System.out.println("请输入是否自动(Y|N 可不输,默认非自动)");
        int randomLand = 0;
        boolean self = true;
        boolean lan = false;
        //输入是手动还是自动
        String self1 = scanner.nextLine();
        if (StringUtils.isEmpty(self1)) {
            self1 = "N";
        }
        if (self1.equals("非自动") || self1.equals("N") || self1.equals("n")) {
            self = false;
            System.out.println("是否抢地主(Y|N),默认随机地主");
            String land = scanner.nextLine();
            if (StringUtils.isEmpty(land)) {
                land = "N";
            }
            if (land.equals("是") || land.equals("Y") || land.equals("y")) {
                lan = true;
            } else {
                randomLand = RandomUtils.nextInt(1, 4);
            }
        } else {
            //随机地主
            randomLand = RandomUtils.nextInt(1, 4);
        }
        System.out.println("请输入当前玩家的名称(不输则为默认玩家)");
        local = scanner.nextLine();
        if (StringUtils.isEmpty(local)) {
            local = "路人甲";
        }
        ArrayList list = null;
        Map<String, List> pokMap = new HashMap<>();
        Map<Integer, String> peoperMap = new HashMap<>();
        for (int i = 1; i < 4; i++) {
            switch (i) {
                case 1:
                    list = player01;
                    break;
                case 2:
                    list = player02;
                    break;
                case 3:
                    list = player03;
                    break;
            }
            String name = "";
            if (i == 1 && self) {
                name = local;
            } else {
                String[] s = {"路人甲", "路人乙", "路人丙"};
                String[] s1 = {"小苹果", "小辣椒", "小刘影"};
                String[] s2 = {local, "玩家二", "玩家三"};
                while (StringUtils.isEmpty(name)) {
                    if (Arrays.asList(s).contains(local)) {
                        for (String str : s) {
                            if (peoperMap.containsValue(str)) {
                                continue;
                            }
                            name = str;
                        }
                    } else if (Arrays.asList(s1).contains(local)) {
                        for (String str : s1) {
                            if (peoperMap.containsValue(str)) {
                                continue;
                            }
                            name = str;
                        }
                    } else {
                        for (String str : s2) {
                            if (peoperMap.containsValue(str)) {
                                continue;
                            }
                            name = str;
                        }
                    }
                }
            }
            peoperMap.put(i, name);
            if (StringUtils.isNotEmpty(local)) {
                if (name.equals(local)) {
                    list.addAll(dipai);
                }
            } else {
                if (i == randomLand) {
                    local = name;
                    list.addAll(dipai);
                }
            }
            List pokList = lookPoker(poker, list);
            listSort(pokList, valueMap);
            pokMap.put(name, pokList);
        }
        System.out.println("");
        System.out.println("");
        System.out.println("你选择了" + (self ? "自动" : "非自动") + "出牌，并且" + (lan ? "抢地主" : "不抢地主" + "，地主为：" + peoperMap.get(randomLand)));
        System.out.print("一共有" + pokMap.size() + "个玩家，玩家分别为：");
        pokMap.keySet().stream().forEach(key -> System.out.print(key + "、"));
        System.out.println("\b");
        System.out.println("当前玩家为：" + local);

        List<String> boards = new ArrayList();
        int m = 0;
        while (allPok.size() > 0) {
            if (boards != null && boards.size() > 0) {
                if (boards.get(0).equals("end")) {
                    break;
                }
            }
            for (Integer key : peoperMap.keySet()) {//keySet获取map集合key的集合  然后在遍历key即可
                if (key == m && m != 0) {
                    //如果后面的玩家都要不起，则由当前玩家重新出牌
                    boards.clear();
                }
                if (!(key == randomLand) && m == 0) {
                    continue;
                }
                printSingleColor("", key + 31, key + 31, "【" + peoperMap.get(key) + "】出牌=====================================");
                try {
                    Thread.sleep(Long.valueOf(RandomStringUtils.randomNumeric(3)));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (local.equals(peoperMap.get(key))) {
                    System.out.println("【" + peoperMap.get(key) + "】的牌为：" + pokMap.get(peoperMap.get(key)).toString());
                }
                //如果是手动出牌，则列出所有的牌
                if (!self && local.equals(peoperMap.get(key))) {
//                    boards.clear();
                    String board = "";
                    System.out.print("请输入你要出的牌：");
                    board = scanner.nextLine();
                    while (!checkBoard(board, pokMap.get(peoperMap.get(key)), boards, valueMap)) {
                        System.out.print("出牌有误，请输入你要出的牌：");
                        board = scanner.nextLine();
                    }
                    if (StringUtils.isNotEmpty(board)) {
                        char[] chars = board.toCharArray();
                        boards = new ArrayList() {{
                            for (char c1 : chars) {
                                String s = String.valueOf(c1);
                                if (s.contains("大") || s.contains("小")) {
                                    s = s.contains("大") ? "大王" : "小王";
                                } else if (s.contains("王")) {
                                    continue;
                                } else if (s.contains("0")) {
                                    s = "10";
                                } else if (s.contains("1")) {
                                    continue;
                                }
                                final String p = s;

                                //根据出牌的数字匹配对应的牌号
                                //要存储不同符号的牌号
                                String v = pokMap.get(peoperMap.get(key)).stream().filter(t ->
                                        getNum(t.toString()).equals(StringUtils.upperCase(p)) && !contains(t)).findFirst().get().toString();
                                add(v);
                            }
                        }};

                    } else {
                        boards.clear();
                    }
                }
                List retBoards = playHandPlayer1(peoperMap.get(key), pokMap, valueMap, allPok, boards, local, self);
                List b = new ArrayList();
                if (retBoards != null) {
                    b.addAll(retBoards);
                }
                //如果下家都要不起，则由该玩家重新出牌
                if (retBoards != null && retBoards.size() > 0) {
                    //记录最后出牌的玩家
                    m = key;
                    boards.clear();
                    boards.addAll(b);
                    if (b.get(0).equals("end")) {
                        break;
                    }
                }

            }

        }
    }

    public static boolean checkBoard(String board, List<String> pokList, List<String> retBoards, Map<String, Integer> valueMap) {
        if (StringUtils.isEmpty(board)) {
            if (retBoards == null || retBoards.size() == 0) {
                return false;
            }
            return true;
        }
        Map<String, String> map = new HashMap();
        char[] chars = board.toCharArray();
        List<String> charBoard = new ArrayList() {{
            for (char c1 : chars) {
                String s = String.valueOf(c1);
                final String p = s;
                Object v = pokList.stream().filter(t ->
                        getNum(t).equals(StringUtils.upperCase(p)) && !contains(t)).findFirst();
                if (v == null) {
                    add("false");
                    break;
                }
                if (s.contains("大") || s.contains("小")) {
                    add(s.contains("大") ? "大王" : "小王");
                } else if (s.contains("0")) {
                    add("10");
                } else if (s.contains("王")) {
                    continue;
                } else if (s.contains("1")) {
                    continue;
                } else {
                    add(s.toUpperCase());
                }
            }
        }};
        if (charBoard.contains("false")) {
            return false;
        }

        charBoard.stream().forEach(c -> {
            int n = pokList.stream().filter(p ->
                    getNum(p).equals(StringUtils.upperCase(String.valueOf(c)))
            ).toArray().length;
            if (n == 0) {
                map.put("a", "false");
            }
        });
        if (map.size() > 0) {
            if (map.get("a").equals("false")) {
                return false;
            }
        }

        //初始化牌
        Map<Integer, List<String>> initMap = initBoard(charBoard, valueMap);
        //判断只能有一个list大小大于0
        int m = initMap.keySet().stream().filter(n -> initMap.get(n).size() > 0).toArray().length;
        if (m >= 2) {
            return false;
        }
        //取出不为空list的数值
        Integer num = initMap.keySet().stream().filter(n -> initMap.get(n).size() > 0).findFirst().get();
        //获取出不为空的list
        List<String> list = initMap.get(num);
        if (retBoards.size() == 0) {
            //判断是否连续
            switch (num) {
                case 1:
                    if (list.size() == 1) {
                        return true;
                    }
                    if (list.size() >= 5) {
                        Map map1 = clocks(list, valueMap, 5);
                        if (map1.size() > 0) {
                            return true;
                        }
                    }
                    return false;
                case 2:
                    if (list.size() == 1) {
                        return true;
                    }
                    if (list.size() >= 3) {
                        Map map1 = clocks(list, valueMap, 3);
                        if (map1.size() > 0) {
                            return true;
                        }
                    }
                    return false;

                case 3:
                    if (list.size() == 1) {
                        return true;
                    }
                    if (list.size() >= 2) {
                        Map map1 = clocks(list, valueMap, 2);
                        if (map1.size() > 0) {
                            return true;
                        }
                    }
                    return false;
            }

        }
        Map<Integer, List<String>> rbtInitMap = initBoard(retBoards, valueMap);
        //获取出不为空的list
        Integer num1 = rbtInitMap.keySet().stream().filter(n -> rbtInitMap.get(n).size() > 0).findFirst().get();
        List<String> list1 = rbtInitMap.get(num1);
        switch (num) {
            case 1:
                if (num1 != 1) {
                    return false;
                }

                if (list.size() == 1) {
                    if (valueMap.get(list.get(0).length() == 1 ? "♥" + list.get(0) : list.get(0)) <= valueMap.get(list1.get(0))) {
                        return false;
                    }
                }
                if (list.size() >= 5) {
                    List retBoard = checkOtherPok(initMap.get(1), null, valueMap, rbtInitMap.get(1).get(0), 1);
                    if (retBoard == null) {
                        return false;
                    }
                }
                break;
            case 2:
                if (num1 != 2) {
                    return false;
                }

                if (list.size() == 1) {
                    if (valueMap.get("♥" + list.get(0)) <= valueMap.get(list1.get(0))) {
                        return false;
                    }
                }
                if (list.size() >= 3) {
                    List retBoard = checkOtherPok(initMap.get(2), null, valueMap, rbtInitMap.get(2).get(0), 2);
                    if (retBoard == null) {
                        return false;
                    }
                }

                break;
            case 3:
                if (num1 != 3) {
                    return false;
                }

                if (list.size() == 1) {
                    if (valueMap.get("♥" + list.get(0)) <= valueMap.get(list1.get(0))) {
                        return false;
                    }
                }
                if (list.size() >= 2) {
                    List retBoard = checkOtherPok(initMap.get(3), null, valueMap, rbtInitMap.get(3).get(0), 3);
                    if (retBoard == null) {
                        return false;
                    }
                }
                break;
            case 4:
                if (list.size() == 1) {
                    if (list1.size() > 1) {
                        return true;
                    }
                    if (valueMap.get("♥" + list.get(0)) <= valueMap.get(list1.get(0))) {
                        return false;
                    }
                }
                //王炸则直接返回成功
                if (list.size() == 2) {
                    return true;
                } else {
                    List retBoard = checkOtherPok(initMap.get(4), null, valueMap, rbtInitMap.get(4).get(0), 4);
                    if (retBoard == null) {
                        return false;
                    }
                }
                break;
        }
        return true;


    }


    public static List playHandPlayer1(String pok, Map<String, List> pokMap, Map<String, Integer> valueMap, List<String> allpok, List boards, String local, boolean self) {
        List pokList = pokMap.get(pok);
        //初始化牌
        Map<Integer, List<String>> initMap = initBoard(pokList, valueMap);

        if ((!local.equals(pok) && !self) || self) {
            boards = playHand(initMap, valueMap, boards);
        }
        if (boards != null && boards.size() > 0) {

            System.out.println("【" + pok + "】出的牌为：" + boards.toString());
            System.out.println("");

            allpok.removeAll(boards);
            pokList.removeAll(boards);
            pokMap.put(pok, pokList);

            if (pokList.size() <= 0) {
                boards.clear();
                boards.add("end");
                System.out.println("【" + pok + "】:哈哈，我赢啦！！！！！！！！！！！");
            } else {
//                if (local.equals(pok)) {
//                System.out.println("【" + pok + "】剩下的牌为：" + pokList);
//                System.out.println("");
//                }
            }
        } else {
            System.out.println("【" + pok + "】表示他要不起(╥╯^╰╥)");
//            System.out.println("【" + pok + "】剩下的牌为：" + pokList);
            System.out.println("");
        }
        return boards;
    }

    /**
     * @param pattern 前面的图案 such as "=============="
     * @param code    颜色代号：背景颜色代号(41-46)；前景色代号(31-36)
     * @param n       数字+m：1加粗；3斜体；4下划线
     * @param content 要打印的内容
     */
    public static void printSingleColor(String pattern, int code, int n, String content) {
        System.out.format("%s\33[%d;%dm%s%n", pattern, code, n, content);
    }

    /**
     * 去除不需要的内容
     *
     * @param
     */
    public static void clearVal(int n) {
        for (int i = 0; i < n; i++) {
            System.out.print("\b");
        }
    }


    /**
     * 根据上家出的牌来出牌
     *
     * @param initMap  初始化的牌
     * @param valueMap
     * @param boards   上家的牌
     */
    public static List playHand(Map<Integer, List<String>> initMap, Map<String, Integer> valueMap, List<String> boards) {
        List<String> oneList = initMap.get(1);
        List<String> twoList = initMap.get(2);
        List<String> threeList = initMap.get(3);
        List<String> fourList = initMap.get(4);

        oneList.addAll(twoList);
        oneList.addAll(threeList);

        twoList.addAll(threeList);

        listSort(oneList, valueMap);
        listSort(twoList, valueMap);
        listSort(threeList, valueMap);

//        System.out.println("oneList" + oneList);
//        System.out.println("twoList" + twoList);
//        System.out.println("threeList" + threeList);

        //判断是否有顺子
        Map<String, Integer> ontMap = clocks(oneList, valueMap, 5);
        //判断是否有连对
        Map<String, Integer> twoMap = clocks(twoList, valueMap, 3);
        //判断是否有飞机
        Map<String, Integer> threeMap = clocks(threeList, valueMap, 2);
//        System.out.println("map" + ontMap);
//        System.out.println("map1" + twoMap);
//        System.out.println("map2" + threeMap);

        List retBoard = new ArrayList();
        //如果没有上家出牌，则当前玩家为第一个出牌或者继续出牌
        if (boards == null || boards.size() == 0) {
            if (ontMap.size() > 0) {
                retBoard = getBoard(ontMap, valueMap, 1, oneList);
            } else if (twoMap.size() > 0) {
                retBoard = getBoard(twoMap, valueMap, 2, twoList);
            } else if (threeMap.size() > 0) {
                retBoard = getBoard(threeMap, valueMap, 3, threeList);
            } else {
//                retBoard.add(oneList.get(0));
                if (threeList != null && threeList.size() > 0) {
                    retBoard = checkOtherPok(threeList, threeMap, valueMap, "", 3);
                } else if (twoList != null && twoList.size() > 0) {
                    retBoard = checkOtherPok(twoList, twoMap, valueMap, "", 2);
                } else if (oneList != null && oneList.size() > 0) {
                    retBoard = checkOtherPok(oneList, ontMap, valueMap, "", 1);
                } else if (fourList != null && fourList.size() > 0) {
                    retBoard = checkOtherPok(fourList, null, valueMap, "", 4);
                }
            }
        } else {
            //判断对方出的牌的规则
            Map<Integer, List<String>> initMap1 = initBoard(boards, valueMap);
            List<String> oneList1 = initMap1.get(1);
            List<String> twoList1 = initMap1.get(2);
            List<String> threeList1 = initMap1.get(3);
            List<String> fourList1 = initMap1.get(4);

            listSort(oneList1, valueMap);
            listSort(twoList1, valueMap);
            listSort(threeList1, valueMap);

            if (oneList1.size() > 0 && threeList1.size() == 0 && fourList1.size() == 0) {
                if (oneList1.size() > 4) {
                    //判断是否有顺子
                    Map<String, Integer> ontMap1 = clocks(oneList1, valueMap, 5);
                    if (ontMap1.size() > 0) {
                        retBoard = checkPock(ontMap1, ontMap, valueMap, 1, oneList);
                    }
                } else {
                    //单张
                    if (oneList1.size() == 1) {
                        retBoard = checkOtherPok(oneList, ontMap, valueMap, oneList1.get(0), 1);
                    }
                }
            } else if (twoList1.size() > 0 && threeList1.size() == 0 && fourList1.size() == 0) {
                if (twoList1.size() > 2) {
                    //判断是否有连对
                    Map<String, Integer> twoMap1 = clocks(twoList1, valueMap, 3);
                    if (twoMap1.size() > 0) {
                        //连对
                        retBoard = checkPock(twoMap1, twoMap, valueMap, 2, twoList);
                    }
                } else {
                    //对子
                    retBoard = checkOtherPok(twoList, twoMap, valueMap, twoList1.get(0), 2);
                }
            } else if (threeList1.size() > 0) {
                if (threeList1.size() > 3) {
                    //判断是否有飞机
                    Map<String, Integer> threeMap1 = clocks(threeList1, valueMap, 2);
                    if (threeMap1.size() > 0) {
                        //飞机
                        retBoard = checkPock(threeMap1, threeMap, valueMap, 3, threeList);
                    }
                } else {
                    //三带一
                    retBoard = checkOtherPok(threeList, threeMap, valueMap, threeList1.get(0), 3);
                }
            } else if (fourList1.size() > 0) {
                if (fourList1.size() == 2) {
                    //王炸
//                    retBoard = null;
                } else {
                    //炸弹
                    retBoard = checkOtherPok(fourList, null, valueMap, fourList1.get(0), 4);
                }
            }

        }
        if (retBoard == null || retBoard.size() == 0) {
            return retBoard;
        }
        retBoard = listSort(retBoard, valueMap);

        //出完牌后在map中去掉牌
        ontMap.remove(retBoard);
        twoMap.remove(retBoard);
        threeMap.remove(retBoard);
        return retBoard;

    }

    /**
     * 获取单张或者对子或者三带一
     *
     * @param list     匹配对应的list
     * @param ownMap   匹配对应的map
     * @param valueMap
     * @param val      对方出的牌
     * @param bNum
     * @return
     */
    public static List checkOtherPok(List<String> list, Map<String, Integer> ownMap, Map<String, Integer> valueMap, String val, int bNum) {
        List<String> boards = new ArrayList<>();
        list.stream().forEach(li -> {
            if ((StringUtils.isNotEmpty(val) && valueMap.get(li) > valueMap.get(val)) || StringUtils.isEmpty(val)) {
                if (ownMap != null) {
                    int len = ownMap.keySet().stream().filter(n -> getNum(n).equals(getNum(li))).toArray().length;
                    if (len == 0) {
                        //防止需要两张却获取三张的情况
                        //如果是连对则放入两张牌 飞机则为三张
                        int len1 = boards.stream().filter(n -> getNum(n).equals(getNum(li))).toArray().length;
                        if (len1 < bNum && boards.size() < bNum) {
                            boards.add(li);
                        }
                    }
                } else {
                    //防止需要两张却获取三张的情况
                    //如果是连对则放入两张牌 飞机则为三张
                    int len = boards.stream().filter(n -> getNum(n).equals(getNum(li))).toArray().length;
                    if (len < bNum && boards.size() < bNum) {
                        boards.add(li);
                    }
                }
            }
        });
        return boards;
    }

    /**
     * 根据对方的牌判断自己的牌是否要的起
     *
     * @param advMap   对方的牌
     * @param ownMap   自己的牌
     * @param valueMap
     * @param
     */
    public static List checkPock(Map<String, Integer> advMap, Map<String, Integer> ownMap, Map<String, Integer> valueMap, int bNum, List<String> list) {
        if (ownMap.size() == 0) {
            return null;
        }
        List<String> boards = new ArrayList<>();
        Integer val = advMap.values().stream().findFirst().get();
        String key = advMap.keySet().stream().findFirst().get();
        Map<String, Integer> sMap = new HashMap();
        //顺子
        if (ownMap.size() > 0) {
            ownMap.keySet().stream().forEach(keyset -> {
                        //判断自己的顺子是否比对方的顺子第一张大
                        if (valueMap.get(keyset) > valueMap.get(key)) {
                            //判断张数是否匹配
                            if (ownMap.get(keyset) >= val) {
                                //记录第一张牌与最后一张牌
                                sMap.put("start", valueMap.get(keyset));
                                sMap.put("end", valueMap.get(keyset) + val);
                                return;
                            }
                        }
                    }
            );
        }
        if (sMap.size() > 0) {
            for (int i = sMap.get("start"); i < sMap.get("end"); i++) {
                final int num = i;
                valueMap.keySet().stream().forEach(key1 -> {
                    if (valueMap.get(key1) == num && list.contains(key1)) {
                        //防止需要两张却获取三张的情况
                        //如果是连对则放入两张牌 飞机则为三张
                        int len = boards.stream().filter(n -> getNum(n).equals(getNum(key1))).toArray().length;
                        if (len < bNum) {
                            boards.add(key1);
                        }
                    }
                });
            }
        }
        return boards;
    }

    /**
     * 如果是地主出牌，则判断第一次该出的牌
     * //如果是地主则先出牌
     * //判断最多的张数的牌
     * //判断最小的牌，从最小牌出起
     *
     * @param map
     * @param valueMap
     * @param bNum     是顺子还是连对还是飞机
     */
    public static List getBoard(Map<String, Integer> map, Map<String, Integer> valueMap, int bNum, List<String> list) {
        //找到张数最多的牌
        int maxVal = map.values().stream().max((a, b) ->
                a > b ? 1 : -1
        ).get();

        List<String> boards = new ArrayList();
        map.keySet().stream().forEach(key -> {
            //获取最多牌数的第一张牌
            if (map.get(key) == maxVal) {
                boards.add(key);
                return;
            }
        });
        int start = valueMap.get(boards.get(0));
        int end = start + maxVal;
        for (int i = start; i < end; i++) {
            final int num = i;
            valueMap.keySet().stream().forEach(key -> {
                if (valueMap.get(key) == num && list.contains(key)) {
                    int len = boards.stream().filter(n -> getNum(n).equals(getNum(key))).toArray().length;
                    if (len < bNum) {
                        boards.add(key);
                    }
                }
            });
        }
        return boards;

    }

    /*
     * 参数：
     * HashMap<Integer,String> poker:存储牌的map结合
     * ArrayList<Integer> list ：存储玩家和底牌的list集合
     */
    public static List lookPoker(Map<Integer, String> poker, ArrayList<Integer> list) {
        ArrayList pokList = new ArrayList();
        for (Integer key : list) {
            String value = poker.get(key);
            pokList.add(value);
        }

        return pokList;
    }


    //初始化牌
    public static Map<Integer, List<String>> initBoard(List<String> poker, Map<String, Integer> valueMap) {
        //判断数组中重复的数字与次数
        Map<Integer, Long> repetMap = poker.stream()
                .filter(p -> valueMap.get(p) != null).collect(
                        Collectors.groupingBy(p -> valueMap.get(p), Collectors.counting()));

        Map<String, Long> repetNum = new HashMap<>();
        repetMap.keySet().forEach(m -> {
            poker.stream().forEach(p -> {
                if (valueMap.get(p) == m) {
                    repetNum.put(p, repetMap.get(m));
                }
            });
        });


        List<String> oneList = new ArrayList<>();
        List<String> twoList = new ArrayList<>();
        List<String> threeList = new ArrayList<>();
        List<String> fourList = new ArrayList<>();

        repetNum.forEach((k, v) -> {
            switch (v.intValue()) {
                case 1:
                    oneList.add(k);
                    break;
                case 2:
                    twoList.add(k);
                    break;
                case 3:
                    threeList.add(k);
                    break;
                case 4:
                    fourList.add(k);
                    break;
            }
        });
        if (oneList.contains("大王") && oneList.contains("小王")) {
            fourList.add("大王");
            fourList.add("小王");
            oneList.remove("大王");
            oneList.remove("小王");

        }
        //两张牌加三张牌组为连对
//        twoList.addAll(threeList);


        Map<Integer, List<String>> listMap = new HashMap<>();
        listMap.put(1, oneList);
        listMap.put(2, twoList);
        listMap.put(3, threeList);
        listMap.put(4, fourList);

        return listMap;
    }

    /**
     * 判断list中的牌是否有顺序的牌
     *
     * @param list
     * @param index
     * @param valueMap
     * @return
     */
    public static int checkClockAfter(List<String> list, int index, Map<String, Integer> valueMap) {
        if (index + 1 == list.size()) {
            return index;
        }
        String val = list.get(index);
        if (valueMap.get(list.get(index + 1)) - valueMap.get(val) == 1) {
            return checkClockAfter(list, index + 1, valueMap);
        } else {
            return index;
        }
    }


    /**
     * 获取顺序的牌的第一张与张数
     *
     * @param valueMap
     * @param num      顺子至少需要连续的五张牌 连对则需三对 飞机为2
     * @return
     */
    public static Map<String, Integer> clocks(List<String> list, Map<String, Integer> valueMap, int num) {
        //先去重
        ArrayList<String> li = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(o ->
                        getNum(o)))), ArrayList::new));

        Map<String, Integer> clocksMap = new HashMap();
        Stream.iterate(0, i -> i + 1).limit(li.size()).forEach(index -> {
            int returnIndex = checkClockAfter(li, index, valueMap);
            //数据相减后与传过来的数据减一做对比，如顺子最大的index为5，最小的index为1，相减后为4
            if (returnIndex - index >= num - 1) {
                //key : 连对第一张牌 value : 连对对数
                clocksMap.put(li.get(index), returnIndex - index + 1);
            }
        });
        return clocksMap;
    }

    //list排序
    public static List<String> listSort(List<String> list, Map<String, Integer> valueMap) {
        if (list.size() == 0) {
            return list;
        }
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return valueMap.get(o1) - valueMap.get(o2);
            }
        });
        return list;
    }

    public static String getNum(String s) {
        return s.replace("♦", "").replace("♥", "").replace("♠", "").replace("♣", "");
    }
    final static Scanner scanner=new Scanner(System.in);
//    public static String scanner.nextLine() {
//        BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
//        try {
//            return reader.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
}
