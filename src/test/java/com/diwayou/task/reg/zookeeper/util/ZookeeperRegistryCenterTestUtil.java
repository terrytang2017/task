package com.diwayou.task.reg.zookeeper.util;

import com.diwayou.task.reg.zookeeper.ZookeeperRegistryCenter;

public class ZookeeperRegistryCenterTestUtil {

    public static void persist(final ZookeeperRegistryCenter zookeeperRegistryCenter) {
        zookeeperRegistryCenter.persist("/test", "test");
        zookeeperRegistryCenter.persist("/test/deep/nested", "deepNested");
        zookeeperRegistryCenter.persist("/test/child", "child");
    }
}
