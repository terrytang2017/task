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

package com.diwayou.task.statistics.type.job;

import java.util.Date;

/**
 * 作业注册到Cloud平台统计数据.
 *
 * @author liguangyun
 */
public final class JobRegisterStatistics {

    private final int registeredCount;
    private final Date statisticsTime;
    private long id;
    private Date creationTime = new Date();

    public JobRegisterStatistics(int registeredCount, Date statisticsTime) {
        this.registeredCount = registeredCount;
        this.statisticsTime = statisticsTime;
    }

    public JobRegisterStatistics(long id, int registeredCount, Date statisticsTime, Date creationTime) {
        this.id = id;
        this.registeredCount = registeredCount;
        this.statisticsTime = statisticsTime;
        this.creationTime = creationTime;
    }

    public long getId() {
        return id;
    }

    public int getRegisteredCount() {
        return registeredCount;
    }

    public Date getStatisticsTime() {
        return statisticsTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }
}
