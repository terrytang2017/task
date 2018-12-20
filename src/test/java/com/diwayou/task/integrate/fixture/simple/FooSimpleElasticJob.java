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

package com.diwayou.task.integrate.fixture.simple;

import com.diwayou.task.api.ShardingContext;
import com.diwayou.task.api.simple.SimpleJob;

public final class FooSimpleElasticJob implements SimpleJob {

    private static volatile boolean completed;

    public static void reset() {
        completed = false;
    }

    public static boolean isCompleted() {
        return completed;
    }

    @Override
    public void execute(final ShardingContext shardingContext) {
        completed = true;
    }
}
