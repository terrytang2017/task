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

package com.diwayou.task;

import com.diwayou.task.api.AllApiTests;
import com.diwayou.task.config.AllConfigTests;
import com.diwayou.task.context.AllContextTests;
import com.diwayou.task.event.AllEventTests;
import com.diwayou.task.exception.AllExceptionTests;
import com.diwayou.task.executor.AllExecutorTests;
import com.diwayou.task.integrate.AllIntegrateTests;
import com.diwayou.task.internal.AllInternalTests;
import com.diwayou.task.reg.AllRegTests;
import com.diwayou.task.statistics.AllStatisticsTests;
import com.diwayou.task.util.AllUtilTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        AllRegTests.class,
        AllContextTests.class,
        AllApiTests.class,
        AllConfigTests.class,
        AllExecutorTests.class,
        AllEventTests.class,
        AllExceptionTests.class,
        AllStatisticsTests.class,
        AllUtilTests.class,
        AllApiTests.class,
        AllConfigTests.class,
        AllInternalTests.class,
        AllIntegrateTests.class
})
public final class AllLiteCoreTests {
}
