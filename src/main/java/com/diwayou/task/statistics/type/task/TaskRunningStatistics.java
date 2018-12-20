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

import java.util.Date;

/**
 * 运行中的任务统计数据.
 *
 * @author liguangyun
 */
public final class TaskRunningStatistics {

    private final int runningCount;
    private final Date statisticsTime;
    private long id;
    private Date creationTime = new Date();

    public TaskRunningStatistics(int runningCount, Date statisticsTime) {
        this.runningCount = runningCount;
        this.statisticsTime = statisticsTime;
    }

    public TaskRunningStatistics(long id, int runningCount, Date statisticsTime, Date creationTime) {
        this.id = id;
        this.runningCount = runningCount;
        this.statisticsTime = statisticsTime;
        this.creationTime = creationTime;
    }

    public long getId() {
        return id;
    }

    public int getRunningCount() {
        return runningCount;
    }

    public Date getStatisticsTime() {
        return statisticsTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }
}
