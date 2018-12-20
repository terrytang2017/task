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

package com.diwayou.task.api.strategy.impl;

import com.diwayou.task.api.strategy.JobInstance;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class RotateServerByNameJobShardingStrategyTest {

    private RotateServerByNameJobShardingStrategy rotateServerByNameJobShardingStrategy = new RotateServerByNameJobShardingStrategy();

    @Test
    public void assertSharding1() {
        Map<JobInstance, List<Integer>> expected = new HashMap<>();
        expected.put(new JobInstance("host1@-@0"), Collections.singletonList(0));
        expected.put(new JobInstance("host2@-@0"), Collections.singletonList(1));
        expected.put(new JobInstance("host0@-@0"), Collections.<Integer>emptyList());
        assertThat(rotateServerByNameJobShardingStrategy.sharding(Arrays.asList(new JobInstance("host0@-@0"), new JobInstance("host1@-@0"), new JobInstance("host2@-@0")), "1", 2), is(expected));
    }

    @Test
    public void assertSharding2() {
        Map<JobInstance, List<Integer>> expected = new HashMap<>();
        expected.put(new JobInstance("host2@-@0"), Collections.singletonList(0));
        expected.put(new JobInstance("host0@-@0"), Collections.singletonList(1));
        expected.put(new JobInstance("host1@-@0"), Collections.<Integer>emptyList());
        assertThat(rotateServerByNameJobShardingStrategy.sharding(Arrays.asList(new JobInstance("host0@-@0"), new JobInstance("host1@-@0"), new JobInstance("host2@-@0")), "2", 2), is(expected));
    }

    @Test
    public void assertSharding3() {
        Map<JobInstance, List<Integer>> expected = new HashMap<>();
        expected.put(new JobInstance("host0@-@0"), Collections.singletonList(0));
        expected.put(new JobInstance("host1@-@0"), Collections.singletonList(1));
        expected.put(new JobInstance("host2@-@0"), Collections.<Integer>emptyList());
        assertThat(rotateServerByNameJobShardingStrategy.sharding(Arrays.asList(new JobInstance("host0@-@0"), new JobInstance("host1@-@0"), new JobInstance("host2@-@0")), "3", 2), is(expected));
    }
}
