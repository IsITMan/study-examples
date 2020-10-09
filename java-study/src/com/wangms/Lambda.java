package com.wangms;



import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

/**
 * @author: wangms
 * @date: 2020-08-28 14:36
 */
public class Lambda {
    public static void main(String[] args) {
        Map<String, String> map = new HashMap();
        map.put("1", "张三");
        map.put("2", "李四");
        map.put("3", "王五");
        Collection<String> s1 = map.values();
        System.out.println(s1);
        for (String s : map.keySet()) {
            System.out.println(s);
        }

        map.forEach((a, b) -> {
            System.out.println(b);
        });

        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.forEach(v -> {
            System.out.println(v);
        });

        String str = list.stream().findFirst().toString();
        System.out.println(str);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("1");
            }
        };

        Runnable runnable1 = () -> System.out.println("1");

        Stream stream = Stream.of("a", "b", "c");
        System.out.println("limit:");
        stream.limit(2).forEach(System.out::println);

        List<Integer> list11 = new ArrayList<Integer>();
        list11.add(4);
        list11.add(3);
        list11.add(2);
        list11.add(3);
        list11.stream().sorted(Comparator.reverseOrder()).forEach(System.out::println);
        long count = list11.parallelStream().filter(a -> a == 3).count();
        System.out.println("count:"+count);

        list11.stream().distinct().forEach(a-> System.out.println("aa:"+a));
        List<Integer> numbers = Arrays.asList(1, 5, 7, 3, 9);
        IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();

        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());


        System.out.println(LocalDate.now());

        System.out.println(LocalDateTime.now());
        System.out.println(new Date());

        LocalDateTime ld = LocalDateTime.parse("2020-09-01T11:11:30.222Z");
        LocalDateTime ld2 = LocalDateTime.parse("2020-09-01T11:11:30.111Z");
        System.out.println("相差毫秒 : " + Duration.between(ld, ld2).toMillis());
        System.out.println("相毫秒 : " + Duration.between(ld, ld2).getSeconds());


        String[] strArray = new String[] { "a", "b", "c" };
        stream = Stream.of(strArray);
        stream = Arrays.stream(strArray);
        //List<String> list = Arrays.asList(strArray);
       // stream = list.stream();

        try {
            Stream<String> stream2 = Stream.of("a", "b", "c");
            // 转换成 Array
            String[] strArray1 = stream2.toArray(String[]::new);

            // 转换成 Collection
            List<String> list1 = stream2.collect(Collectors.toList());
            List<String> list2 = stream2.collect(toCollection(ArrayList::new));
            Set set1 = stream2.collect(Collectors.toSet());
            Stack stack1 = stream2.collect(toCollection(Stack::new));

            // 转换成 String
           // String str = stream.collect(Collectors.joining()).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
