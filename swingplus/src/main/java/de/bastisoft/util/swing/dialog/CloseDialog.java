/*
 * Copyright 2012 Sebastian Koppehel
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

package de.bastisoft.util.swing.dialog;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import de.bastisoft.util.swing.LabelText;
import de.bastisoft.util.swing.SwingUtils;

public abstract class CloseDialog extends StandardDialog {

    private static final long serialVersionUID = 1L;
    
    public CloseDialog(Frame owner, String title, boolean modal) {
        this(owner, title, modal, new LabelText("Close", 'C'));
    }
    
    public CloseDialog(Frame owner, String title, boolean modal, LabelText closeText) {
        super(owner, title, modal);
        
        JButton closeButton = SwingUtils.makeButton(closeText);
        setButtons(new JButton[] { closeButton });
        makeActions(closeButton);
    }
    
    private void makeActions(JButton closeButton) {
        Action closeAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
            }
        };
        
        closeButton.addActionListener(closeAction);
        
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "close");
        
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put("close", closeAction);
    }

}
