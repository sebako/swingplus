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

package de.bastisoft.util.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;

public class SwingUtils {
    
    public static JButton makeButton(LabelText labelText) {
        JButton button = new JButton();
        setText(button, labelText);
        return button;
    }
    
    public static JLabel makeLabel(LabelText label) {
        JLabel l = new JLabel(label.text);
        if (label.mnemonic != null) {
            l.setDisplayedMnemonic(label.mnemonic);
            if (label.mnemonicIndex != null)
                l.setDisplayedMnemonicIndex(label.mnemonicIndex);
        }
        return l;
    }
    
    public static JLabel makeLabel(LabelText label, Component labelFor) {
        JLabel l = makeLabel(label);
        l.setLabelFor(labelFor);
        return l;
    }
    
    public static void setText(AbstractButton button, LabelText labelText) {
        button.setText(labelText.text);
        if (labelText.mnemonic != null) {
            button.setMnemonic(labelText.mnemonic);
            if (labelText.mnemonicIndex != null)
                button.setDisplayedMnemonicIndex(labelText.mnemonicIndex);
        }
    }
    
    public static void equalizeButtons(JButton ... buttons) {
        int maxWidth = 0;
        int maxHeight = 0;
        
        for (JButton button : buttons) {
            Dimension d = button.getPreferredSize();
            maxWidth = Math.max(d.width, maxWidth);
            maxHeight = Math.max(d.height, maxHeight);
        }
        
        Dimension d = new Dimension(maxWidth, maxHeight);
        
        for (JButton button : buttons) {
            button.setMinimumSize(d);
            button.setPreferredSize(d);
            button.setMaximumSize(d);
        }
    }
    
    public static void adjustButtonMargins(Insets margin, JButton ... buttons) {
        for (JButton button : buttons)
            button.setMargin(margin);
    }
    
}
