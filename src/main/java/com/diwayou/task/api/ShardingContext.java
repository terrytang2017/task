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

package com.diwayou.task.api;

import com.diwayou.task.executor.ShardingContexts;

/**
 * 分片上下文.
 *
 * @author zhangliang
 */
public final class ShardingContext {

    /**
     * 作业名称.
     */
    private final String jobName;

    /**
     * 作业任务ID.
     */
    private final String taskId;

    /**
     * 分片总数.
     */
    private final int shardingTotalCount;

    /**
     * 作业自定义参数.
     * 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
     */
    private final String jobParameter;

    /**
     * 分配于本作业实例的分片项.
     */
    private final int shardingItem;

    /**
     * 分配于本作业实例的分片参数.
     */
    private final String shardingParameter;

    public ShardingContext(final ShardingContexts shardingContexts, final int shardingItem) {
        jobName = shardingContexts.getJobName();
        taskId = shardingContexts.getTaskId();
        shardingTotalCount = shardingContexts.getShardingTotalCount();
        jobParameter = shardingContexts.getJobParameter();
        this.shardingItem = shardingItem;
        shardingParameter = shardingContexts.getShardingItemParameters().get(shardingItem);
    }

    public String getJobName() {
        return jobName;
    }

    public String getTaskId() {
        return taskId;
    }

    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public String getJobParameter() {
        return jobParameter;
    }

    public int getShardingItem() {
        return shardingItem;
    }

    public String getShardingParameter() {
        return shardingParameter;
    }

    @Override
    public String toString() {
        return "ShardingContext{" +
                "jobName='" + jobName + '\'' +
                ", taskId='" + taskId + '\'' +
                ", shardingTotalCount=" + shardingTotalCount +
                ", jobParameter='" + jobParameter + '\'' +
                ", shardingItem=" + shardingItem +
                ", shardingParameter='" + shardingParameter + '\'' +
                '}';
    }
}
