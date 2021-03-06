/*
 * Copyright 2018
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
package de.tudarmstadt.ukp.clarin.webanno.api.annotation.feature.editor;

import static org.apache.wicket.markup.head.JavaScriptHeaderItem.forReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.googlecode.wicket.jquery.core.template.IJQueryTemplate;
import com.googlecode.wicket.jquery.ui.settings.JQueryUILibrarySettings;

public class KendoChoiceDescriptionScriptReference
    extends JavaScriptResourceReference
{
    private static final long serialVersionUID = 1L;

    private static final KendoChoiceDescriptionScriptReference INSTANCE = new KendoChoiceDescriptionScriptReference();

    /**
     * Gets the instance of the resource reference
     *
     * @return the single instance of the resource reference
     */
    public static KendoChoiceDescriptionScriptReference get()
    {
        return INSTANCE;
    }

    /**
     * Private constructor
     */
    private KendoChoiceDescriptionScriptReference()
    {
        super(KendoChoiceDescriptionScriptReference.class,
                "KendoChoiceDescriptionScriptReference.js");
    }

    @Override
    public List<HeaderItem> getDependencies()
    {
        List<HeaderItem> dependencies = new ArrayList<>(super.getDependencies());

        // Required to load the tooltop plugin
        dependencies.add(forReference(JQueryUILibrarySettings.get().getJavaScriptReference()));

        return dependencies;
    }

    public static IJQueryTemplate template()
    {
        return new IJQueryTemplate()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public String getText()
            {
                // Some docs on how the templates work in Kendo, in case we need
                // more fancy dropdowns
                // http://docs.telerik.com/kendo-ui/framework/templates/overview
                return "<div title=\"#: data.description #\" "
                        + "onmouseover=\"javascript:applyTooltip(this)\">"
                        + "#: data.name #</div>\n";
            }

            @Override
            public List<String> getTextProperties()
            {
                return Arrays.asList("name", "description");
            }
        };
    }

    public static IJQueryTemplate templateReorderable()
    {
        return new IJQueryTemplate()
        {
            private static final long serialVersionUID = 1L;

            @Override
            public String getText()
            {
                // Some docs on how the templates work in Kendo, in case we need
                // more fancy dropdowns
                // http://docs.telerik.com/kendo-ui/framework/templates/overview
                return "# if (data.reordered == 'true') { #"
                        + "<div title=\"#: data.description #\" "
                        + "onmouseover=\"javascript:applyTooltip(this)\">"
                        + "<b>#: data.name #</b></div>\n" + "# } else { #"
                        + "<div title=\"#: data.description #\" "
                        + "onmouseover=\"javascript:applyTooltip(this)\">"
                        + "#: data.name #</div>\n" + "# } #";
            }

            @Override
            public List<String> getTextProperties()
            {
                return Arrays.asList("name", "description", "reordered");
            }
        };
    }
}
