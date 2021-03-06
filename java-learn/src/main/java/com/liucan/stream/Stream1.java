package com.liucan.stream;

import com.liucan.pojo.Student;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class Stream1 {
    public void example() {
        /*
        概念：
         流用来处理集合的数据，因为集合操作麻烦，而流的话让我们可以像sql语句一样
         对集合进行像流一样的操作
        特点：
         只能遍历一次，像流一样不能返回
         采用内部迭代的方式（只需告诉流我们需要什么结果，处理过程由流自行完成）
         而外部迭代是对集合进行处理，则需我们手写处理代码
        操作类型；
         中间操作与结束操作，
         中间操作只是对操作进行了记录，惰性求值是用于描述stream流的，返回值是stream，
         只有结束操作才会触发实际的计算,及早求值，
         这也是Stream在迭代大集合时高效的原因之一
         */

        //对集合
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Student stu = new Student();
            stu.setId(i);
            stu.setAge(i);
            stu.setName(String.valueOf(i));
            stu.setSex(i%2 == 0 ? "male": "female");
            list.add(stu);
        }
        Stream<Student> studentStream = list.stream();

        //对结果按照sex分组保持
        Map<String, List<Student>> maps = list.stream().collect(Collectors.groupingBy(Student::getSex));
        //对结果按照sex分组,并计算其count保存
        Map<String, Long> map = list.stream().collect(Collectors.groupingBy(Student::getSex, Collectors.counting()));
        //对结果按照sex分组,并计算其平均值保存
        Map<String, Double> map2 = list.stream().collect(Collectors.groupingBy(Student::getSex, Collectors.averagingInt(Student::getAge)));

        //对结果按照age是否>2进行分区（特殊的分组）
        Map<Boolean, List<Student>> map3 = list.stream().collect(Collectors.partitioningBy(e -> e.getAge() > 2));
        //对结果按照ange是否>2进行分区，然后在分区里面对其sex进行分组，获得其个数
        Map<Boolean, Map<String, Long>> map4 = list.stream().collect(Collectors.partitioningBy(e -> e.getAge() > 2,
                Collectors.groupingBy(Student::getSex, Collectors.counting())));


        //对数组
        String[] names = {"chaimm","peter","john"};
        Stream<String> stringStream = Arrays.stream(names);

        //对值
        Stream<String> stringStream1 = Stream.of("chaimm1","peter1","john1");

        //对文件,Java7简化了IO操作，把打开IO操作放在try后的括号中即可省略关闭IO的代码
//        try(Stream lines = Files.lines(Paths.get("c/xxx"), Charset.defaultCharset())) {
//            //可对lines做一些操作
//            lines.count();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        List<Student> result = list.stream()
                .sorted(Comparator.comparing(Student::getName))
                .filter(e -> e.getSex().equals("male"))
                .collect(Collectors.toList());

        result = list.stream().distinct().collect(Collectors.toList());
        result = list.stream().limit(3).collect(Collectors.toList());
        result = list.stream().skip(3).collect(Collectors.toList());
        List<String> sexs  = list.stream()
                .sorted(Comparator.comparing(Student::getSex))
                .filter(e -> e.getSex().equals("male"))
                .map(Student::getSex)
                .collect(Collectors.toList());

        //male 且年龄最大的
        OptionalInt maxAge = list.stream()
                .sorted(Comparator.comparing(Student::getName))
                .filter(e -> e.getSex().equals("male"))
                .mapToInt(Student::getAge)
                .max();

        //Stream可以容纳不同的数据类型
        //但stream操作（filter，sum，distinct ...）和collectors不支持它，需要使用flatMap转换
        //将一条一条的小流，汇聚成一条大流
        List <Integer> together = Stream.of(Arrays.asList(1,2), Arrays.asList(3,4))
                .flatMap(numbers -> numbers.stream())
                .collect(Collectors.toList());

        //collector后续看一下:
        //https://www.cnblogs.com/woshimrf/p/java8-learn-collector.html
    }
}
