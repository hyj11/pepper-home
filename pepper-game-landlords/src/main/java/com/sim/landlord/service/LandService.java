package com.sim.landlord.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/10/14 14:06
 * @Desc:
 */
@Service
public class LandService {

    /**
     * 发牌
     */
    public static Map<String, List<String>> deal(List<String> players) {
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

        Map<String, List<String>> player = players(pokerIndex, players, poker, valueMap);
        System.out.println(player);


        return player;
    }

    /*
     * 参数：
     * HashMap<Integer,String> poker:存储牌的map结合
     * ArrayList<Integer> list ：存储玩家和底牌的list集合
     */
    public static List<String> lookPoker(Map<Integer, String> poker, List<Integer> list) {
        ArrayList pokList = new ArrayList();
        for (Integer key : list) {
            String value = poker.get(key);
            pokList.add(value);
        }

        return pokList;
    }

    /**
     * 给每个人随机发牌
     *
     * @param pokerIndex
     * @param player     所有玩家
     * @param poker      所有的牌及其序号（同一张牌不同花色序号不一致）
     * @param valueMap   所有的牌及其序号（同一张牌不同花色序号一致）
     * @return
     */
    public static Map<String, List<String>> players(ArrayList<Integer> pokerIndex, List<String> player, Map<Integer, String> poker, Map<String, Integer> valueMap) {
        List<Integer> dipai = new ArrayList<>();
        Map<String, List<Integer>> players = new HashMap<>();
        for (int i = 0; i < pokerIndex.size(); i++) {
            Integer in = pokerIndex.get(i);
            final int m = i;
            Stream.iterate(0, j -> j + 1).limit(player.size()).forEach(index -> {
                //留出需要的底牌
                if (m > (52 - player.size())) {
                    dipai.add(in);
                } else if (m % player.size() == index) {
                    List<Integer> li = players.get(player.get(index));
                    if (li == null) {
                        li = new ArrayList();
                    }
                    li.add(in);
                    players.put(player.get(index), li);
                }
            });
            players.put("dipai", deWeigntInteger(dipai));
        }
        Map<String, List<String>> pokers = new HashMap<>();

        players.forEach((k, v) -> {
//            List<Integer> strs = listStringToInt(v);
            //根据牌的序号获取出对应牌及其花色
            List<String> pokList = lookPoker(poker, v);
            listSort(pokList, valueMap);
            pokers.put(k, pokList);
        });

        return pokers;
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


    /**
     * list去重
     *
     * @param list
     * @return
     */
    public static List deWeigntInteger(List<Integer> list) {
        return list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(o ->
                        o))), ArrayList::new));
    }

    /**
     * list去重
     *
     * @param list
     * @return
     */
    public static List deWeigntString(List<String> list) {
        return list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(o ->
                        getNum(o)))), ArrayList::new));
    }

    public static List<Integer> listStringToInt(List<String> stringList) {
        return stringList.stream().map(x -> Integer.valueOf(x)).collect(Collectors.toList());
    }

    /**
     * 去掉花色
     *
     * @param s
     * @return
     */
    public static String getNum(String s) {
        return s.replace("♦", "").replace("♥", "").replace("♠", "").replace("♣", "");
    }

    public static void main(String[] args) {
        List<String> strings = new ArrayList() {{
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
        }};

        deal(strings);
    }
}
