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

package com.diwayou.task.integrate.std.simple;

import com.diwayou.task.integrate.AbstractBaseStdJobAutoInitTest;
import com.diwayou.task.integrate.WaitingUtils;
import com.diwayou.task.integrate.fixture.simple.FooSimpleElasticJob;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public final class SimpleElasticJobTest extends AbstractBaseStdJobAutoInitTest {

    public SimpleElasticJobTest() {
        super(FooSimpleElasticJob.class);
    }

    @Before
    @After
    public void reset() {
        FooSimpleElasticJob.reset();
    }

    @Test
    public void assertJobInit() {
        WaitingUtils.waitingUntilTrueOrTimeout((Void) -> FooSimpleElasticJob.isCompleted(), 300 * 1000);
        assertTrue(getRegCenter().isExisted("/" + getJobName() + "/sharding"));
    }
}