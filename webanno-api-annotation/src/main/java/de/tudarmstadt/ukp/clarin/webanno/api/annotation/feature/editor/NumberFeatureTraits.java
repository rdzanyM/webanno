/*
 * Copyright 2019
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.tudarmstadt.ukp.clarin.webanno.api.annotation.feature.editor;

import java.io.Serializable;

/**
 * Traits for number features.
 */
public class NumberFeatureTraits
    implements Serializable
{
    private static final long serialVersionUID = -2395185084802071593L;

    public enum EDITOR_TYPE
    {
        SPINNER("Spinner"), RADIO_BUTTONS("Radio Buttons");

        private final String name;

        EDITOR_TYPE(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }

    private boolean limited = false;
    private Number minimum = 0;
    private Number maximum = 0;
    private EDITOR_TYPE editorType = EDITOR_TYPE.SPINNER;

    public NumberFeatureTraits()
    {
        // Nothing to do
    }

    public boolean isLimited()
    {
        return limited;
    }

    public void setLimited(boolean limited)
    {
        this.limited = limited;
    }

    public Number getMinimum()
    {
        return minimum;
    }

    public void setMinimum(Number minimum)
    {
        this.minimum = minimum;
    }

    public Number getMaximum()
    {
        return maximum;
    }

    public void setMaximum(Number maximum)
    {
        this.maximum = maximum;
    }

    public EDITOR_TYPE getEditorType()
    {
        return editorType;
    }

    public void setEditorType(EDITOR_TYPE editorType)
    {
        this.editorType = editorType;
    }
}