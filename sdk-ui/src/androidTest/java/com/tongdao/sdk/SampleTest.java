package com.tongdao.sdk;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

/**
 * Created by agonch on 9/29/16.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SampleTest {

    @Test
    public void simpleTest(){
        assertThat("Test not working",1, IsEqual.equalTo(1));
    }
}
