/*
 * Copyright 2007 Mark Scott
 *
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
 */
package org.codebrewer.idea.jetty;

import com.intellij.CommonBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.PropertyKey;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.ResourceBundle;

/**
 * @author Mark Scott
 * @version $Id: JettyBundle.java 6 2007-02-10 23:33:18Z mark $
 */
public class JettyBundle
{
  private static Reference<ResourceBundle> ourBundle;

  @NonNls
  private static final String BUNDLE = "org.codebrewer.idea.jetty.JettyBundle";

  private static ResourceBundle getBundle()
  {
    ResourceBundle bundle = null;
    if (ourBundle != null) {
      bundle = ourBundle.get();
    }
    if (bundle == null) {
      bundle = ResourceBundle.getBundle(BUNDLE);
      ourBundle = new SoftReference<ResourceBundle>(bundle);
    }
    return bundle;
  }

  public static String message(@PropertyKey(resourceBundle = BUNDLE) final String key, final Object... params)
  {
    return CommonBundle.message(getBundle(), key, params);
  }

  private JettyBundle()
  {
  }
}