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

package com.diwayou.task.reg.zookeeper;

import com.diwayou.task.exception.JobSystemException;
import com.diwayou.task.reg.base.ElectionCandidate;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * 使用{@link LeaderSelector}实现选举服务.
 *
 * @author gaohongtao
 * @author caohao
 */
public final class ZookeeperElectionService {

    private static final Logger log = LoggerFactory.getLogger(ZookeeperElectionService.class);

    private final CountDownLatch leaderLatch = new CountDownLatch(1);

    private final LeaderSelector leaderSelector;

    public ZookeeperElectionService(final String identity, final CuratorFramework client, final String electionPath, final ElectionCandidate electionCandidate) {
        leaderSelector = new LeaderSelector(client, electionPath, new LeaderSelectorListenerAdapter() {

            @Override
            public void takeLeadership(final CuratorFramework client) throws Exception {
                log.info("Elastic job: {} has leadership", identity);
                try {
                    electionCandidate.startLeadership();
                    leaderLatch.await();
                    log.warn("Elastic job: {} lost leadership.", identity);
                    electionCandidate.stopLeadership();
                } catch (final JobSystemException exception) {
                    log.error("Elastic job: Starting error", exception);
                    System.exit(1);
                }
            }
        });
        leaderSelector.autoRequeue();
        leaderSelector.setId(identity);
    }

    /**
     * 开始选举.
     */
    public void start() {
        log.debug("Elastic job: {} start to elect leadership", leaderSelector.getId());
        leaderSelector.start();
    }

    /**
     * 停止选举.
     */
    public void stop() {
        log.info("Elastic job: stop leadership election");
        leaderLatch.countDown();
        try {
            leaderSelector.close();
            // CHECKSTYLE:OFF
        } catch (final Exception ignored) {
        }
        // CHECKSTYLE:ON
    }
}
