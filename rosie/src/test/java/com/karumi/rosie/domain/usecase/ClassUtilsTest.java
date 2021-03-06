/*
 * The MIT License (MIT) Copyright (c) 2014 karumi Permission is hereby granted, free of charge,
 * to any person obtaining a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to
 * do so, subject to the following conditions: The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE
 * IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.karumi.rosie.domain.usecase;

import com.karumi.rosie.UnitTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClassUtilsTest extends UnitTest {

  @Test public void testCanAssignTwoAnyObjects() throws Exception {
    assertTrue(ClassUtils.canAssign(String.class, String.class));
  }

  @Test public void testCanAssignTwoAnyObjectsWithHierarchy() throws Exception {
    assertTrue(ClassUtils.canAssign(AnyClass.class, SonOfAnyClass.class));
  }

  @Test public void testCanAssignTwoObjectsWithAPrimitiveObject() throws Exception {
    assertTrue(ClassUtils.canAssign(int.class, Integer.class));
  }

  @Test public void testCanAssignTwoObjectsWithAPrimitiveObjectBase() throws Exception {
    assertTrue(ClassUtils.canAssign(Integer.class, int.class));
  }

  private class AnyClass {
  }

  private class SonOfAnyClass extends AnyClass {
  }
}