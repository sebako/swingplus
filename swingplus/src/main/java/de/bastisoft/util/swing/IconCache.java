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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconCache {

    private String pathPrefix;
    private Map<String, Icon> cache;
    private Map<String, String> icons;

    public IconCache(String pathPrefix, Map<String, String> icons) {
        this.pathPrefix = pathPrefix;
        this.icons = new HashMap<String, String>();
        this.icons.putAll(icons);

        cache = new HashMap<String, Icon>();
    }

    public Icon getIcon(String symbolicName) throws IconCacheException {
        String path = icons.get(symbolicName);
        if (path == null) {
            throw new IconCacheException("Unknown symbolic name: " + symbolicName);
        }

        Icon icon = cache.get(path);
        if (icon == null) {
            icon = loadIcon(path);
            cache.put(path, icon);
        }

        return icon;
    }

    private Icon loadIcon(String path) throws IconCacheException {
        InputStream in = getClass().getResourceAsStream(pathPrefix + path);

        try {
            return new ImageIcon(ImageIO.read(in));
        } catch (Exception e) {
            throw new IconCacheException("Error loading image", e);
        }
    }
    
}
