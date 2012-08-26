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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

class OkCancelDialogDemo extends OkCancelDialog {

    private static final long serialVersionUID = 1L;

    private JRadioButton allowRadio;
    private JRadioButton forbidRadio;
    
    public OkCancelDialogDemo(Frame parent) {
        super(parent, "OkCancelDialog Demo", false);
        setContent(makeWidgets());
        makeActions();
    }

    private JPanel makeWidgets() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        panel.setLayout(new GridBagLayout());
        
        JLabel label = new JLabel("Allow me to press the \"Ok\" button?");
        
        allowRadio = new JRadioButton("Yes, let me press it.");
        forbidRadio = new JRadioButton("No, disable the button.");
        
        ButtonGroup g = new ButtonGroup();
        g.add(allowRadio);
        g.add(forbidRadio);
        
        allowRadio.setSelected(true);
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        panel.add(label, c);
        
        c.insets = new Insets(12, 12, 0, 0);
        panel.add(allowRadio, c);
        
        c.insets = new Insets(0, 12, 0, 0);
        panel.add(forbidRadio, c);
        
        return panel;
    }
    
    private void makeActions() {
        ActionListener l = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateButton();
            }
        };
        
        allowRadio.addActionListener(l);
        forbidRadio.addActionListener(l);
    }
    
    protected boolean ok() {
        return allowRadio.isSelected();
    }
    
}
