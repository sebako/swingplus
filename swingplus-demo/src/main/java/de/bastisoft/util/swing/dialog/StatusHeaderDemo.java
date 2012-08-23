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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import de.bastisoft.util.swing.dialog.OkCancelDialog;
import de.bastisoft.util.swing.header.StatusHeader;
import de.bastisoft.util.swing.header.StatusMessage;
import de.bastisoft.util.swing.header.StatusMessage.Severity;

public class StatusHeaderDemo extends OkCancelDialog {

    public StatusHeaderDemo() {
        super(null, "StatusHeader Demo", false);
        setContent(makeWidgets());
    }

    private JPanel makeWidgets() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        StatusHeader header = new StatusHeader("StatusHeader Demo", "Demonstrating the status header", 400);
        header.addMessage(new StatusMessage(0, Severity.WARNING, "Warning"));
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        panel.add(header, c);
        panel.add(new JSeparator(), c);
        
        JLabel label = new JLabel("Hello Kitty");
        
        c.weighty = 1;
        c.insets = new Insets(20, 20, 20, 20);
        panel.add(label, c);
        
        c.weighty = 0;
        c.insets = new Insets(0, 0, 0, 0);
        panel.add(new JSeparator(), c);
        
        return panel;
    }

    @Override
    protected boolean ok() {
        return true;
    }
    
    public static void main(String[] args) {
        new StatusHeaderDemo().run();
    }

}
