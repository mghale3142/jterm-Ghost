/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.engedu.ghost;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SimpleDictionaryTest {

    SimpleDictionary sd;

    @Before
    public void setup() {
        ArrayList<String> al = new ArrayList<String>();
        al.add("dance");
        al.add("done");
        al.add("math");
        al.add("mount");
        al.add("north");
        sd = new SimpleDictionary(al, 0);
    }

    @Test
    public void testIsWord() {
        // TODO(you): Add some tests!
        assertEquals(true, sd.isWord("done"));
        assertEquals(false, sd.isWord("mat"));
    }

    @Test
    public void testGetAnyWordStartingWith() {
        // TODO(you): Add some tests!
        assertEquals("done", sd.getAnyWordStartingWith("don"));
        assertEquals(sd.getAnyWordStartingWith("mee"), null);
        assertEquals(sd.getAnyWordStartingWith("done"), "done");


    }
}
