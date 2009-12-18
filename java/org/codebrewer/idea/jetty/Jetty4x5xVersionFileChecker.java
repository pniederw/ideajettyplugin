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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * @author Mark Scott
 * @version $Id: Jetty4x5xVersionFileChecker.java 6 2007-02-10 23:33:18Z mark $
 */
public class Jetty4x5xVersionFileChecker extends AbstractJettyVersionFileChecker
{
  @NonNls private static final String VERSION_FILE_NAME = "VERSION.TXT";
  @NonNls private static final String VERSION_PATTERN = "^Jetty-(.*) - .*";

  @NotNull public String getVersionFileName()
  {
    return VERSION_FILE_NAME;
  }

  @NotNull public String getVersionPattern()
  {
    return VERSION_PATTERN;
  }
}