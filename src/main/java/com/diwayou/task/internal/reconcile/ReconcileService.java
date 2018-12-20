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

package com.diwayou.task.internal.reconcile;

import com.diwayou.task.config.LiteJobConfiguration;
import com.diwayou.task.internal.config.ConfigurationService;
import com.diwayou.task.internal.election.LeaderService;
import com.diwayou.task.internal.sharding.ShardingService;
import com.diwayou.task.reg.base.CoordinatorRegistryCenter;
import com.google.common.util.concurrent.AbstractScheduledService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 调解分布式作业不一致状态服务.
 *
 * @author caohao
 */
public final class ReconcileService extends AbstractScheduledService {

    private static final Logger log = LoggerFactory.getLogger(ReconcileService.class);
    private final ConfigurationService configService;
    private final ShardingService shardingService;
    private final LeaderService leaderService;
    private long lastReconcileTime;

    public ReconcileService(final CoordinatorRegistryCenter regCenter, final String jobName) {
        lastReconcileTime = System.currentTimeMillis();
        configService = new ConfigurationService(regCenter, jobName);
        shardingService = new ShardingService(regCenter, jobName);
        leaderService = new LeaderService(regCenter, jobName);
    }

    @Override
    protected void runOneIteration() throws Exception {
        LiteJobConfiguration config = configService.load(true);
        int reconcileIntervalMinutes = null == config ? -1 : config.getReconcileIntervalMinutes();
        if (reconcileIntervalMinutes > 0 && (System.currentTimeMillis() - lastReconcileTime >= reconcileIntervalMinutes * 60 * 1000)) {
            lastReconcileTime = System.currentTimeMillis();
            if (leaderService.isLeaderUntilBlock() && !shardingService.isNeedSharding() && shardingService.hasShardingInfoInOfflineServers()) {
                log.warn("Elastic Job: job status node has inconsistent value,start reconciling...");
                shardingService.setReshardingFlag();
            }
        }
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0, 1, TimeUnit.MINUTES);
    }
}
