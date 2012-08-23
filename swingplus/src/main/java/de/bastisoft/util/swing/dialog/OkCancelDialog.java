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

public abstract class OkCancelDialog extends StandardDialog {

    private static final long serialVersionUID = 1L;

    private JButton okButton;
    private JButton cancelButton;
    
    private boolean ok;
    
    public OkCancelDialog(Frame owner, String title, boolean modal) {
        this(owner, title, modal, new LabelText("Ok", 'O'), new LabelText("Cancel", 'C'));
    }
    
    public OkCancelDialog(Frame owner, String title, boolean modal, LabelText okText, LabelText cancelText) {
        super(owner, title, modal);
        
        setButtons(new JButton[] {
                okButton = SwingUtils.makeButton(okText),
                cancelButton = SwingUtils.makeButton(cancelText) });
        
        makeActions();
    }
    
    private void makeActions() {
        Action okAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                if (ok()) {
                    ok = true;
                    setVisible(false);
                }
            }
        };
        
        Action cancelAction = new AbstractAction() {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        };
        
        okButton.addActionListener(okAction);
        cancelButton.addActionListener(cancelAction);
        
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "ok");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");
        
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put("ok", okAction);
        actionMap.put("cancel", cancelAction);
    }
    
    public boolean run() {
        ok = false;
        updateButton();
        setVisible(true);
        return ok;
    }
    
    protected void updateButton() {
        okButton.setEnabled(ok());
    }

    protected abstract boolean ok();

}
