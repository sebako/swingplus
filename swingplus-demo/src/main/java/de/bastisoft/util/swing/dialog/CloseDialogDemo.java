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

import java.awt.Color;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

class CloseDialogDemo extends CloseDialog {

    public CloseDialogDemo(Frame owner) {
        super(owner, "CloseDialog Demo", false);
        setContent(makeWidgets());
    }

    private JComponent makeWidgets() {
        JPanel p = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics;
                Color start = Color.WHITE;
                Color end = new Color(0xD3, 0xDC, 0xE5);
                GradientPaint paint = new GradientPaint(0, 0, start, 40, getHeight(), end);
                g.setPaint(paint);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        JLabel label = new JLabel("<html><font color='#1573A9'>Hello Swing+");
        label.setFont(label.getFont().deriveFont(30f));
        
        p.setBorder(BorderFactory.createEtchedBorder());
        p.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(50, 25, 50, 25);
        p.add(label, c);
        
        JPanel outerPanel = new JPanel();
        outerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        outerPanel.add(p);
        
        return outerPanel;
    }
    
}
