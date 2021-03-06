/*
 * Copyright 2019
 * Ubiquitous Knowledge Processing (UKP) Lab and FG Language Technology
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tudarmstadt.ukp.clarin.webanno.telemetry;

import java.io.Serializable;

public class TelemetryDetail
    implements Serializable
{
    private static final long serialVersionUID = -3891866931664933567L;

    private final String key;
    private final String value;
    private final String description;

    public TelemetryDetail(String aKey, String aValue)
    {
        this(aKey, aValue, null);
    }

    public TelemetryDetail(String aKey, String aValue, String aDescription)
    {
        super();
        key = aKey;
        value = aValue;
        description = aDescription;
    }

    public String getKey()
    {
        return key;
    }

    public String getValue()
    {
        return value;
    }

    public String getDescription()
    {
        return description;
    }
}
