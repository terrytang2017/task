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

package com.diwayou.task.internal.election;

import com.diwayou.task.internal.listener.AbstractJobListener;
import com.diwayou.task.internal.listener.AbstractListenerManager;
import com.diwayou.task.internal.schedule.JobRegistry;
import com.diwayou.task.internal.server.ServerNode;
import com.diwayou.task.internal.server.ServerService;
import com.diwayou.task.internal.server.ServerStatus;
import com.diwayou.task.reg.base.CoordinatorRegistryCenter;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent.Type;

/**
 * 主节点选举监听管理器.
 *
 * @author zhangliang
 */
public final class ElectionListenerManager extends AbstractListenerManager {

    private final String jobName;

    private final LeaderNode leaderNode;

    private final ServerNode serverNode;

    private final LeaderService leaderService;

    private final ServerService serverService;

    public ElectionListenerManager(final CoordinatorRegistryCenter regCenter, final String jobName) {
        super(regCenter, jobName);
        this.jobName = jobName;
        leaderNode = new LeaderNode(jobName);
        serverNode = new ServerNode(jobName);
        leaderService = new LeaderService(regCenter, jobName);
        serverService = new ServerService(regCenter, jobName);
    }

    @Override
    public void start() {
        addDataListener(new LeaderElectionJobListener());
        addDataListener(new LeaderAbdicationJobListener());
    }

    class LeaderElectionJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final Type eventType, final String data) {
            if (!JobRegistry.getInstance().isShutdown(jobName) && (isActiveElection(path, data) || isPassiveElection(path, eventType))) {
                leaderService.electLeader();
            }
        }

        private boolean isActiveElection(final String path, final String data) {
            return !leaderService.hasLeader() && isLocalServerEnabled(path, data);
        }

        private boolean isPassiveElection(final String path, final Type eventType) {
            return isLeaderCrashed(path, eventType) && serverService.isAvailableServer(JobRegistry.getInstance().getJobInstance(jobName).getIp());
        }

        private boolean isLeaderCrashed(final String path, final Type eventType) {
            return leaderNode.isLeaderInstancePath(path) && Type.NODE_REMOVED == eventType;
        }

        private boolean isLocalServerEnabled(final String path, final String data) {
            return serverNode.isLocalServerPath(path) && !ServerStatus.DISABLED.name().equals(data);
        }
    }

    class LeaderAbdicationJobListener extends AbstractJobListener {

        @Override
        protected void dataChanged(final String path, final Type eventType, final String data) {
            if (leaderService.isLeader() && isLocalServerDisabled(path, data)) {
                leaderService.removeLeader();
            }
        }

        private boolean isLocalServerDisabled(final String path, final String data) {
            return serverNode.isLocalServerPath(path) && ServerStatus.DISABLED.name().equals(data);
        }
    }
}
