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

package com.diwayou.task.statistics.type.task;

import com.diwayou.task.statistics.StatisticInterval;

import java.util.Date;

/**
 * 任务运行结果统计数据.
 *
 * @author liguangyun
 */
public final class TaskResultStatistics {

    private final int successCount;
    private final int failedCount;
    private final StatisticInterval statisticInterval;
    private final Date statisticsTime;
    private long id;
    private Date creationTime = new Date();

    public TaskResultStatistics(int successCount, int failedCount, StatisticInterval statisticInterval, Date statisticsTime) {
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.statisticInterval = statisticInterval;
        this.statisticsTime = statisticsTime;
    }

    public TaskResultStatistics(long id, int successCount, int failedCount, StatisticInterval statisticInterval, Date statisticsTime, Date creationTime) {
        this.id = id;
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.statisticInterval = statisticInterval;
        this.statisticsTime = statisticsTime;
        this.creationTime = creationTime;
    }

    public long getId() {
        return id;
    }

    public int getSuccessCount() {
        return successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public StatisticInterval getStatisticInterval() {
        return statisticInterval;
    }

    public Date getStatisticsTime() {
        return statisticsTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }
}
