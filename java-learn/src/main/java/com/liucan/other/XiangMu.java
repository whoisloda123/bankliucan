package com.liucan.other;

/**
 * @author liucan
 * @version 19-7-29
 */
public class XiangMu {
    /*
     *
     *
     * javawiress
     * 1.thrift
     * 3.common
     * 7.扭蛋活动
     * 8.kafka
     * 9.下单抽免单
     * 10.加购抽奖-大促活动
     *      介绍：用户将商品加入购物车，即可抽奖，然后优先发发加入购物车商品对应的有库存的专场券，其次发b类运营给的有库存的券
     *  a.kafka消费用户的加入购物车消息，保存到mysql
     *  b.redis保存有券库存的信息
     *  c.通过quartz任务调度系统执行定时任务
     *  d.通过thrift调用券服务的查询券信息和发券
     *  e.通过mysql-mybatis，jooq保存用户发券记录
     *  f.活动的一些信息，如开始结束时间，券配置保存在zk里面
     *  g.后台管理系统配置券信息freemarker
     *  h.设计到的难点和需要考虑到的地方：
     *          1.缓存和数据库一致性：保存用户一天玩的次数（redis和mysql）
     *          2.分布式事务：扣积分+发券
     *          3.高并发高可用
     *              a.让运维的多加几台机器
     *              b.限流
     *              c.降级
     *              d.mq流量削峰
     *              e.分布式集群
     *              f.分库分表
     *              q.负债均衡
     *
     *  oversea-competitor
     *      介绍：抓取亚马逊网站上面的数据，和自营店铺的数据，入es库，然后同步hive
     *  1.分为core，crawler,web3个模块
     *  2.core:公共模块，包括mybatis-generator相关的文件,公用的类
     *  3.web,spring-boot，提供api接口和后台管理页面
     *  4.crawler抓取模块：包括
     *      定时任务模块
     *      kafka消费模块
     *      评论抓取模块
     *  5.设计到的难点和需要考虑到的地方
     *
     */
}
