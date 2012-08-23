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

public class StatusMessage {

    public static enum Severity { WARNING, ERROR }
    
    public final int id;
    public final Severity severity;
    public final String text;
    
    public StatusMessage(int id, Severity severity, String text) {
        this.id = id;
        this.severity = severity;
        this.text = text;
    }
    
    public boolean equals(Object other) {
        if (!(other instanceof StatusMessage))
            return false;
        
        StatusMessage o = (StatusMessage) other;
        return o.id == id && o.severity == severity && o.text.equals(text);
    }
    
}
