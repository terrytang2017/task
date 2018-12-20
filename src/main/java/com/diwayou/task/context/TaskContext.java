/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package com.diwayou.task.context;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 任务运行时上下文.
 *
 * @author zhangliang
 * @author caohao
 */
public final class TaskContext {

    private static final String DELIMITER = "@-@";

    private static final String UNASSIGNED_SLAVE_ID = "unassigned-slave";
    private final MetaInfo metaInfo;
    private final ExecutionType type;
    private String id;
    private String slaveId;

    private boolean idle;

    public TaskContext(final String jobName, final List<Integer> shardingItem, final ExecutionType type) {
        this(jobName, shardingItem, type, UNASSIGNED_SLAVE_ID);
    }

    public TaskContext(final String jobName, final List<Integer> shardingItem, final ExecutionType type, final String slaveId) {
        metaInfo = new MetaInfo(jobName, shardingItem);
        this.type = type;
        this.slaveId = slaveId;
        id = Joiner.on(DELIMITER).join(metaInfo, type, slaveId, UUID.randomUUID().toString());
    }

    private TaskContext(final String id, final MetaInfo metaInfo, final ExecutionType type, final String slaveId) {
        this.id = id;
        this.metaInfo = metaInfo;
        this.type = type;
        this.slaveId = slaveId;
    }

    /**
     * 根据任务主键获取任务上下文.
     *
     * @param id 任务主键
     * @return 任务上下文
     */
    public static TaskContext from(final String id) {
        String[] result = id.split(DELIMITER);
        Preconditions.checkState(5 == result.length);
        return new TaskContext(id, MetaInfo.from(result[0] + DELIMITER + result[1]), ExecutionType.valueOf(result[2]), result[3]);
    }

    /**
     * 获取未分配执行服务器前的任务主键.
     *
     * @param id 任务主键
     * @return 未分配执行服务器前的任务主键
     */
    public static String getIdForUnassignedSlave(final String id) {
        return id.replaceAll(TaskContext.from(id).getSlaveId(), UNASSIGNED_SLAVE_ID);
    }

    /**
     * 获取任务名称.
     *
     * @return 任务名称
     */
    public String getTaskName() {
        return Joiner.on(DELIMITER).join(metaInfo, type, slaveId);
    }

    /**
     * 获取任务执行器主键.
     *
     * @param appName 应用名称
     * @return 任务执行器主键
     */
    public String getExecutorId(final String appName) {
        return Joiner.on(DELIMITER).join(appName, slaveId);
    }

    public String getId() {
        return id;
    }

    public MetaInfo getMetaInfo() {
        return metaInfo;
    }

    public ExecutionType getType() {
        return type;
    }

    public String getSlaveId() {
        return slaveId;
    }

    /**
     * 设置任务执行服务器主键.
     *
     * @param slaveId 任务执行服务器主键
     */
    public void setSlaveId(final String slaveId) {
        id = id.replaceAll(this.slaveId, slaveId);
        this.slaveId = slaveId;
    }

    public boolean isIdle() {
        return idle;
    }

    public TaskContext setIdle(boolean idle) {
        this.idle = idle;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskContext that = (TaskContext) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "TaskContext{" +
                "id='" + id + '\'' +
                ", metaInfo=" + metaInfo +
                ", type=" + type +
                ", slaveId='" + slaveId + '\'' +
                ", idle=" + idle +
                '}';
    }

    /**
     * 任务元信息.
     */
    public static class MetaInfo {

        private final String jobName;

        private final List<Integer> shardingItems;

        public MetaInfo(String jobName, List<Integer> shardingItems) {
            this.jobName = jobName;
            this.shardingItems = shardingItems;
        }

        /**
         * 根据任务元信息字符串获取元信息对象.
         *
         * @param value 任务元信息字符串
         * @return 元信息对象
         */
        public static MetaInfo from(final String value) {
            String[] result = value.split(DELIMITER);
            Preconditions.checkState(1 == result.length || 2 == result.length || 5 == result.length);
            return new MetaInfo(result[0], 1 == result.length || "".equals(result[1]) ? Collections.emptyList() :
                    Arrays.stream(result[1].split(",")).map(Integer::parseInt).collect(Collectors.toList()));
        }

        public String getJobName() {
            return jobName;
        }

        public List<Integer> getShardingItems() {
            return shardingItems;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MetaInfo metaInfo = (MetaInfo) o;

            if (!jobName.equals(metaInfo.jobName)) return false;
            return shardingItems.equals(metaInfo.shardingItems);
        }

        @Override
        public int hashCode() {
            int result = jobName.hashCode();
            result = 31 * result + shardingItems.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return Joiner.on(DELIMITER).join(jobName, Joiner.on(",").join(shardingItems));
        }
    }
}
