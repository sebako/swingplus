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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import de.bastisoft.util.swing.IconCache;
import de.bastisoft.util.swing.IconCacheException;
import de.bastisoft.util.swing.header.StatusHeader;
import de.bastisoft.util.swing.header.StatusMessage;
import de.bastisoft.util.swing.header.StatusMessage.Severity;

public class StatusHeaderDemo extends CloseDialog {

    private StatusHeader header;
    private JTextField msgField;
    private JButton pushButton, popButton;
    private JRadioButton warningRadio, errorRadio;
    private JList<StatusMessage> stackList;
    
    private IconCache icons;
    private StackModel stackModel;
    
    public StatusHeaderDemo(Frame parent) {
        super(parent, "StatusHeader Demo", false);
        
        Map<String, String> names = new HashMap<>();
        names.put("icon", "swingplus.png");
        icons = new IconCache(packagePath(StatusHeaderDemo.class), names);
        
        setContent(makeWidgets());
        makeActions();
    }

    private static String packagePath(Class<?> clazz) {
        String slashed = clazz.getPackage().getName().replace('.', '/');
        return "/" + slashed + "/";
    }
    
    private JPanel makeWidgets() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        try {
            header = new StatusHeader("StatusHeader Demo", "Demonstrating the status header", icons.getIcon("icon"), 400);
        }
        catch (IconCacheException e) {
            header = new StatusHeader("StatusHeader Demo", "Demonstrating the status header", 400);
            e.printStackTrace();
        }
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.gridx = 0;
        panel.add(header, c);
        panel.add(new JSeparator(), c);
        
        c.weighty = 1;
        c.insets = new Insets(20, 20, 20, 20);
        panel.add(makeMainPanel(), c);
        
        c.weighty = 0;
        c.insets = new Insets(0, 0, 0, 0);
        panel.add(new JSeparator(), c);
        
        return panel;
    }
    
    private JPanel makeMainPanel() {
        msgField = new JTextField();
        JLabel msgLabel = new JLabel("Message:");
        msgLabel.setDisplayedMnemonic('M');
        msgLabel.setLabelFor(msgField);
        
        pushButton = new JButton("Push");
        pushButton.setMnemonic('U');
        
        warningRadio = new JRadioButton("Warning", true);
        warningRadio.setMnemonic('W');
        errorRadio = new JRadioButton("Error");
        errorRadio.setMnemonic('E');
        ButtonGroup levelGroup = new ButtonGroup();
        levelGroup.add(warningRadio);
        levelGroup.add(errorRadio);
        
        stackList = new JList<>();
        stackList.setModel(stackModel = new StackModel(header));
        stackList.setCellRenderer(new MessageRenderer());
        
        JScrollPane scroller = new JScrollPane(stackList);
        scroller.setPreferredSize(new Dimension(240, 150));
        JLabel stackLabel = new JLabel("Stack:");
        stackLabel.setDisplayedMnemonic('S');
        stackLabel.setLabelFor(stackList);
        
        popButton = new JButton("Pop");
        popButton.setMnemonic('O');
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.BASELINE_LEADING;
        panel.add(msgLabel, c);
        
        c.weightx = 1;
        c.gridwidth = 2;
        c.insets = new Insets(0, 10, 0, 0);
        panel.add(msgField, c);
        
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(pushButton, c);
        
        c.gridy++;
        c.insets = new Insets(7, 0, 0, 0);
        panel.add(new JLabel("Level:"), c);
        
        c.insets = new Insets(7, 10, 0, 0);
        panel.add(warningRadio, c);
        
        c.insets = new Insets(7, 5, 0, 0);
        panel.add(errorRadio, c);
        
        c.gridy++;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(20, 0, 0, 0);
        panel.add(stackLabel, c);
        
        c.weightx = 1;
        c.gridwidth = 2;
        c.insets = new Insets(20, 10, 0, 0);
        panel.add(scroller, c);
        
        c.weightx = 0;
        c.gridwidth = 1;
        panel.add(popButton, c);
        
        return panel;
    }
    
    private void makeActions() {
        pushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                push();
            }
        });
        
        popButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pop();
            }
        });
    }
    
    private void push() {
        String text = msgField.getText();
        if (text.length() == 0)
            return;
        
        Severity sev = warningRadio.isSelected() ? Severity.WARNING : Severity.ERROR;
        stackModel.push(text, sev);
    }
    
    private void pop() {
        int selected = stackList.getSelectedIndex();
        if (selected >= 0)
            stackModel.pop(selected);
    }

    private class StackModel implements ListModel<StatusMessage> {

        List<StatusMessage> messages = new ArrayList<>();
        List<ListDataListener> listeners = new CopyOnWriteArrayList<>();
        int nextID = 0;
        
        StatusHeader header;
        
        StackModel(StatusHeader header) {
            this.header = header;
        }
        
        @Override
        public int getSize() {
            return messages.size();
        }

        @Override
        public StatusMessage getElementAt(int index) {
            return messages.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
            listeners.add(l);
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
            listeners.remove(l);
        }
        
        void push(String text, Severity severity) {
            StatusMessage msg = new StatusMessage(nextID++, severity, text);
            header.addMessage(msg);
            messages.add(msg);
            
            for (ListDataListener l : listeners)
                l.intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, messages.size() - 1, messages.size() - 1));
        }
        
        void pop(int index) {
            StatusMessage msg = getElementAt(index);
            header.removeMessage(msg.id);
            messages.remove(index);
            for (ListDataListener l : listeners)
                l.intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index, index));
        }
        
    }
    
    private static class MessageRenderer extends DefaultListCellRenderer {
        
        @Override
        public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            StatusMessage msg = (StatusMessage) value;
            setText(String.format("%02d %s: %s", msg.id, msg.severity == Severity.WARNING ? "W" : "E", msg.text));
            return this;
        }
        
    }
    
}
