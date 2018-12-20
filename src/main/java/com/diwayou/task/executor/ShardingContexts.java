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

package com.diwayou.task.executor;

import java.util.Map;

/**
 * 分片上下文集合.
 *
 * @author zhangliang
 */
public final class ShardingContexts {

    /**
     * 作业任务ID.
     */
    private final String taskId;

    /**
     * 作业名称.
     */
    private final String jobName;

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
     * 分配于本作业实例的分片项和参数的Map.
     */
    private final Map<Integer, String> shardingItemParameters;

    /**
     * 作业事件采样统计数.
     */
    private int jobEventSamplingCount;

    /**
     * 当前作业事件采样统计数.
     */
    private int currentJobEventSamplingCount;

    /**
     * 是否允许可以发送作业事件.
     */
    private boolean allowSendJobEvent = true;

    public ShardingContexts(String taskId, String jobName, int shardingTotalCount, String jobParameter, Map<Integer, String> shardingItemParameters) {
        this.taskId = taskId;
        this.jobName = jobName;
        this.shardingTotalCount = shardingTotalCount;
        this.jobParameter = jobParameter;
        this.shardingItemParameters = shardingItemParameters;
    }

    public ShardingContexts(final String taskId, final String jobName, final int shardingTotalCount, final String jobParameter,
                            final Map<Integer, String> shardingItemParameters, final int jobEventSamplingCount) {
        this.taskId = taskId;
        this.jobName = jobName;
        this.shardingTotalCount = shardingTotalCount;
        this.jobParameter = jobParameter;
        this.shardingItemParameters = shardingItemParameters;
        this.jobEventSamplingCount = jobEventSamplingCount;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getJobName() {
        return jobName;
    }

    public int getShardingTotalCount() {
        return shardingTotalCount;
    }

    public String getJobParameter() {
        return jobParameter;
    }

    public Map<Integer, String> getShardingItemParameters() {
        return shardingItemParameters;
    }

    public int getJobEventSamplingCount() {
        return jobEventSamplingCount;
    }

    public int getCurrentJobEventSamplingCount() {
        return currentJobEventSamplingCount;
    }

    public ShardingContexts setCurrentJobEventSamplingCount(int currentJobEventSamplingCount) {
        this.currentJobEventSamplingCount = currentJobEventSamplingCount;
        return this;
    }

    public boolean isAllowSendJobEvent() {
        return allowSendJobEvent;
    }

    public ShardingContexts setAllowSendJobEvent(boolean allowSendJobEvent) {
        this.allowSendJobEvent = allowSendJobEvent;
        return this;
    }

    @Override
    public String toString() {
        return "ShardingContexts{" +
                "taskId='" + taskId + '\'' +
                ", jobName='" + jobName + '\'' +
                ", shardingTotalCount=" + shardingTotalCount +
                ", jobParameter='" + jobParameter + '\'' +
                ", shardingItemParameters=" + shardingItemParameters +
                ", jobEventSamplingCount=" + jobEventSamplingCount +
                ", currentJobEventSamplingCount=" + currentJobEventSamplingCount +
                ", allowSendJobEvent=" + allowSendJobEvent +
                '}';
    }
}
