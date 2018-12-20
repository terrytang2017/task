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

package com.diwayou.task.integrate;

import com.diwayou.task.integrate.fixture.simple.FooSimpleElasticJob;

import java.util.function.Predicate;

import static org.junit.Assert.assertTrue;

public final class WaitingUtils {

    public static void waitingUntilTrueOrTimeout(Predicate<Void> condition, long timeout) {
        long start = System.currentTimeMillis();
        long end = start;
        while (!condition.test(null) && (end - start) < timeout) {
            waitingShortTime();
            end = System.currentTimeMillis();
        }
        assertTrue("等待任务执行超时", (end - start) < timeout);
    }

    public static void waitingShortTime() {
        sleep(300L);
    }

    private static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
