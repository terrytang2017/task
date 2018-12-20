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

package com.diwayou.task.util;

import com.diwayou.task.util.concurrent.ExecutorServiceObjectTest;
import com.diwayou.task.util.config.ShardingItemParametersTest;
import com.diwayou.task.util.config.ShardingItemsTest;
import com.diwayou.task.util.digest.EncryptionTest;
import com.diwayou.task.util.env.HostExceptionTest;
import com.diwayou.task.util.env.IpUtilsTest;
import com.diwayou.task.util.env.TimeServiceTest;
import com.diwayou.task.util.json.GsonFactoryTest;
import com.diwayou.task.util.json.JobConfigurationGsonTypeAdapterTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        ExecutorServiceObjectTest.class,
        EncryptionTest.class,
        TimeServiceTest.class,
        IpUtilsTest.class,
        HostExceptionTest.class,
        GsonFactoryTest.class,
        JobConfigurationGsonTypeAdapterTest.class,
        ShardingItemsTest.class,
        ShardingItemParametersTest.class
})
public final class AllUtilTests {
}
