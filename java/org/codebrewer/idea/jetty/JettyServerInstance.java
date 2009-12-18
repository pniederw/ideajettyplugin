/*
 * Copyright 2007, 2008 Mark Scott
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

import com.intellij.debugger.DebuggerManager;
import com.intellij.debugger.PositionManager;
import com.intellij.debugger.engine.DebugProcess;
import com.intellij.debugger.engine.DebugProcessAdapter;
import com.intellij.debugger.engine.DefaultJSPPositionManager;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.javaee.run.configuration.CommonModel;
import com.intellij.javaee.serverInstances.DefaultJ2EEServerEvent;
import com.intellij.javaee.serverInstances.DefaultServerInstance;
import com.intellij.openapi.util.Key;
import org.jetbrains.annotations.NonNls;

/**
 * @author Mark Scott
 * @version $Id: JettyServerInstance.java 77 2008-11-20 21:01:09Z mark $
 */
public class JettyServerInstance extends DefaultServerInstance
{
  @NonNls
  protected static final String STARTING_MESSAGE = "-DSTOP.KEY=";

  private static boolean isStartingMessage(final String text)
  {
    return text.startsWith(STARTING_MESSAGE);
  }

  private boolean isStartedUp = false;

  public JettyServerInstance(final CommonModel runConfiguration)
  {
    super(runConfiguration);
  }

  @Override
  public void start(final ProcessHandler processHandler)
  {// (JettyDeploymentProvider) getServerModel().getDeploymentProvider()
    super.start(processHandler);
    fireServerListeners(new DefaultJ2EEServerEvent(true, false));

    final JettyModel jettyModel = (JettyModel) getServerModel();
    DebuggerManager.getInstance(jettyModel.getProject()).addDebugProcessListener(processHandler, new DebugProcessAdapter()
    {
      private PositionManager positionManager;

      @Override
      public void processAttached(final DebugProcess process)
      {
        positionManager = new DefaultJSPPositionManager(process, getScopeFacets(getCommonModel()))
        {
          @Override
          protected String getGeneratedClassesPackage()
          {
            return "org.apache.jsp";
          }
        };
        process.appendPositionManager(positionManager);
      }
    });

    if (getCommonModel().isLocal()) {
      processHandler.addProcessListener(new ProcessAdapter()
      {
        private int stdoutLinesRead = 0;

        @Override
        public void onTextAvailable(final ProcessEvent event, final Key outputType)
        {
          final String text = event.getText();

          if (stdoutLinesRead < 2 && ProcessOutputTypes.STDOUT.equals(outputType)) {
            stdoutLinesRead++;
            switch (stdoutLinesRead) {
              case 1:
                jettyModel.setStopPort(Integer.parseInt(text.substring(0, text.indexOf(System.getProperty("line.separator")))));
                break;
              case 2:
                jettyModel.setStopKey(text.substring(text.indexOf('=') + 1, text.indexOf(System.getProperty("line.separator"))));
                break;
              default:
            }
          }

          if (!isStartedUp && ProcessOutputTypes.STDOUT.equals(outputType) && isStartingMessage(text)) {
            isStartedUp = true;
          }
        }
      });
    }
    else {
      isStartedUp = true;
    }
  }

  @Override
  public boolean isConnected()
  {
    return isStartedUp && super.isConnected();
  }
}
