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

package de.bastisoft.util.swing.header;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.bastisoft.util.swing.IconCache;
import de.bastisoft.util.swing.IconCacheException;
import de.bastisoft.util.swing.header.StatusMessage.Severity;

public class StatusHeader extends JPanel {

    private static final String ICON_EMPTY = "empty";
    private static final String ICON_WARNING = "warning";
    private static final String ICON_ERROR = "error";
    
    private static final Color COLOR_WARNING = Color.BLACK;
    private static final Color COLOR_ERROR = Color.BLACK;
    
    private String subtitle;
    private JLabel statusLabel;
    private List<StatusMessage> messages;
    private StatusMessage current;
    private IconCache cache;
    private Image backimg;
    
    public StatusHeader(String headline, String subtitle, int minimumWidth) {
        this(headline, subtitle, null, minimumWidth);
    }
    
    public StatusHeader(String headline, String subtitle, Icon icon, int minimumWidth) {
        this.subtitle = subtitle;
        
        messages = new ArrayList<StatusMessage>();
        
        HashMap<String, String> icons = new HashMap<String, String>();
        icons.put(ICON_EMPTY, "empty.png");
        icons.put(ICON_WARNING, "warning.png");
        icons.put(ICON_ERROR, "error.png");
        
        String path = getClass().getPackage().getName().replace('.', '/');
        cache = new IconCache("/" + path + "/", icons);
        
        backimg = loadImage("background.png");
        
        makeLayout(headline, icon, minimumWidth);
    }
    
    private static Image loadImage(String path) {
        try (InputStream in = StatusHeader.class.getResourceAsStream(path)) {
            if (in != null)
                return ImageIO.read(in);
        }
        catch (IOException e) {
            // ...
        }
        return null;
    }
    
    private void makeLayout(String headline, Icon userIcon, int minimumWidth) {
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        
        JLabel header = new JLabel(headline);
        Font f = header.getFont();
        header.setFont(f.deriveFont(Font.BOLD, f.getSize() + 3.0F));
        
        Icon empty;
        try {
            empty = cache.getIcon(ICON_EMPTY);
        }
        catch (IconCacheException e) {
            empty = null;
        }
        
        statusLabel = new JLabel(subtitle);
        statusLabel.setIcon(empty);
        statusLabel.setIconTextGap(7);
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(10, 10, 0, 0);
        add(header, c);
        
        c.weighty = 1;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.insets = new Insets(10, 10, 10, 0);
        add(statusLabel, c);
        
        if (userIcon != null) {
            c.gridx = 1;
            c.weightx = c.weighty = 0;
            c.gridheight = 2;
            c.anchor = GridBagConstraints.EAST;
            c.insets = new Insets(0, 0, 0, 20);
            add(new JLabel(userIcon), c);
        }
        
        int minimumHeight = getPreferredSize().height;
        if (backimg != null && backimg.getHeight(null) > minimumHeight)
            minimumHeight = backimg.getHeight(null);
        setPreferredSize(new Dimension(minimumWidth, minimumHeight));
    }
    
    @Override
    public void paintComponent(Graphics graphics) {
        int w = getWidth();
        int h = getHeight();
        
        Graphics2D g = (Graphics2D) graphics;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        
        if (backimg != null) {
            int imgWidth = backimg.getWidth(null);
            int imgHeight = backimg.getHeight(null);
            g.drawImage(backimg, w - imgWidth, h - imgHeight, null);
        }
    }
    
    public void addMessage(int id, Severity severity, String text) {
        addMessage(new StatusMessage(id, severity, text));
    }
    
    public void addMessage(StatusMessage message) {
        rm(message.id);
        messages.add(message);
        updateStatus();
    }
    
    public void removeMessage(int id) {
        if (rm(id))
            updateStatus();
    }
    
    private boolean rm(int id) {
        int len = messages.size();
        
        if (len == 0)
            return false;
        
        for (Iterator<StatusMessage> it = messages.iterator(); it.hasNext();)
            if (it.next().id == id)
                it.remove();
        
        return len != messages.size();
    }
    
    private void updateStatus() {
        if (messages.size() == 0) {
            if (current != null) {
                current = null;
                statusLabel.setText(subtitle);
                
                Icon empty;
                try {
                    empty = cache.getIcon(ICON_EMPTY);
                }
                catch (IconCacheException e) {
                    empty = null;
                }
                statusLabel.setIcon(empty);
            }
        }
        
        else {
            /* There is a message to display. Find the highest error; if there are no
             * errors, the hightest warning message. */
            
            StatusMessage highest = messages.get(0);
            for (int i = 1; i < messages.size(); i++) {
                StatusMessage m = messages.get(i);
                if (highest.severity == Severity.WARNING &&
                        m.severity == Severity.WARNING ||
                        m.severity == Severity.ERROR)
                    highest = m;
            }
            
            /* Only change the status label if no message is currently displayed or if the message
             * is indeed different. */
            
            if (current == null || !current.equals(highest)) {
                current = highest;
                statusLabel.setText(current.text);
                
                Color color = current.severity == Severity.WARNING ? COLOR_WARNING : COLOR_ERROR;
                statusLabel.setForeground(color);
                
                Icon icon;
                try {
                    icon = cache.getIcon(current.severity == Severity.WARNING ? ICON_WARNING : ICON_ERROR);
                }
                catch (IconCacheException e) {
                    icon = null;
                }
                statusLabel.setIcon(icon);
            }
        }
    }

}
