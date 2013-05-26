/*******************************************************************************
 * Copyright 2012
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.tudarmstadt.ukp.clarin.webanno.webapp.dialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.ui.resizable.ResizableBehavior;
import org.springframework.security.core.context.SecurityContextHolder;

import de.tudarmstadt.ukp.clarin.webanno.api.RepositoryService;
import de.tudarmstadt.ukp.clarin.webanno.brat.ApplicationUtils;
import de.tudarmstadt.ukp.clarin.webanno.model.Project;
import de.tudarmstadt.ukp.clarin.webanno.model.SourceDocument;
import de.tudarmstadt.ukp.clarin.webanno.model.Mode;
import de.tudarmstadt.ukp.clarin.webanno.model.User;

/**
 * A panel used as Open dialog. It Lists all projects a user is member of for annotation/curation
 * and associated documents
 *
 * @author Seid Muhie Yimam
 *
 */
public class OpenModalWindowPanel
    extends Panel
{
    private static final long serialVersionUID = 1299869948010875439L;

    @SpringBean(name = "documentRepository")
    private RepositoryService projectRepository;

    // Project list, Document List and buttons List, contained in separet forms
    private ProjectSelectionForm projectSelectionForm;
    private DocumentSelectionForm documentSelectionForm;
    private ButtonsForm buttonsForm;

    // The first project - selected by default
    private Project selectedProject;
    // The first document in the project // auto selected in the first time.
    private SourceDocument selectedDocument;

    private ListChoice<SourceDocument> documents;
    private OpenDocumentModel openDataModel;

    private String username;
    private User user;

    // Dialog is for annotation or curation

    private Mode subject;

    List<Project> allowedProject = new ArrayList<Project>();

    public OpenModalWindowPanel(String aId, OpenDocumentModel aOpenDataModel, ModalWindow aModalWindow, Mode aSubject)
    {
        super(aId);
        this.subject = aSubject;
        username = SecurityContextHolder.getContext().getAuthentication().getName();
        user = projectRepository.getUser(username);
        if (getAllowedProjects(aSubject).size() > 0) {
            selectedProject = getAllowedProjects(aSubject).get(0);
        }

        this.openDataModel = aOpenDataModel;
        projectSelectionForm = new ProjectSelectionForm("projectSelectionForm");
        documentSelectionForm = new DocumentSelectionForm("documentSelectionForm", aModalWindow);
        buttonsForm = new ButtonsForm("buttonsForm", aModalWindow);

        add(buttonsForm);
        add(projectSelectionForm);
        add(documentSelectionForm);
    }

    private class ProjectSelectionForm
        extends Form<SelectionModel>
    {
        private static final long serialVersionUID = -1L;
        @SuppressWarnings({ "unchecked" })
        private ListChoice<Project> projects;

        public ProjectSelectionForm(String id)
        {
            // super(id);
            super(id, new CompoundPropertyModel<SelectionModel>(new SelectionModel()));

            add(projects = new ListChoice<Project>("project")
            {
                private static final long serialVersionUID = 1L;

                {
                    setChoices(new LoadableDetachableModel<List<Project>>()
                    {
                        private static final long serialVersionUID = 1L;

                        @Override
                        protected List<Project> load()
                        {

                            return getAllowedProjects(subject);
                        }
                    });
                    setChoiceRenderer(new ChoiceRenderer<Project>("name"));
                    setNullValid(false);

                }

                @Override
                protected CharSequence getDefaultChoice(String aSelectedValue)
                {
                    return "";
                }
            });
            projects.setOutputMarkupId(true);
            projects.setMaxRows(10);
            projects.add(new OnChangeAjaxBehavior()
            {

                @Override
                protected void onUpdate(AjaxRequestTarget aTarget)
                {
                    selectedProject = getModelObject().project;
                    // Remove selected document from other project
                    selectedDocument = null;
                    aTarget.add(documents.setOutputMarkupId(true));
                }
            }).add(new ResizableBehavior());

            /*
             * add(new StaticImage("icon", new
             * Model("static/img/Fugue-shadowless-folder-horizontal-open.png"))); RepeatingView
             * projectIconRepeator = new RepeatingView("projectIconRepeator");
             * add(projectIconRepeator);
             *
             * for (final Project project : getAllowedProjects(allowedProject)) { AbstractItem item
             * = new AbstractItem(projectIconRepeator.newChildId()); projectIconRepeator.add(item);
             * item. add(new StaticImage("icon", new
             * Model("static/img/Fugue-shadowless-folder-horizontal-open.png"))); }
             */
        }
    }

    public List<Project> getAllowedProjects(Mode aSubject)
    {

        List<Project> allowedProject = new ArrayList<Project>();

        if(aSubject.equals(subject.ANNOTATION)){
        for (Project project : projectRepository.listProjects()) {
            if (projectRepository.existProjectPermission(user, project)
                    && ApplicationUtils.isMember(project, projectRepository, user)) {
                allowedProject.add(project);
            }
        }
        }
        else if(aSubject.equals(subject.CURATION)){
            for (Project project : projectRepository.listProjects()) {
                if (projectRepository.existProjectPermission(user, project)
                        && ApplicationUtils.isCurator(project, projectRepository, user)) {
                    allowedProject.add(project);
                }
            }
        }

        return allowedProject;
    }

    public class StaticImage
        extends WebComponent
    {

        public StaticImage(String id, IModel model)
        {
            super(id, model);
        }

        @Override
        protected void onComponentTag(ComponentTag tag)
        {
            super.onComponentTag(tag);
            checkComponentTag(tag, "img");
            tag.put("src", getDefaultModelObjectAsString());
            // since Wicket 1.4 you need to use getDefaultModelObjectAsString() instead of
            // getModelObjectAsString()
        }

    }

    private class SelectionModel
        implements Serializable
    {
        private static final long serialVersionUID = -1L;

        private Project project;
        private SourceDocument document;
    }

    private class DocumentSelectionForm
        extends Form<SelectionModel>
    {
        private static final long serialVersionUID = -1L;

        public DocumentSelectionForm(String id, final ModalWindow modalWindow)
        {
            // super(id);
            super(id, new CompoundPropertyModel<SelectionModel>(new SelectionModel()));

            add(documents = new ListChoice<SourceDocument>("document")
            {
                private static final long serialVersionUID = 1L;

                {
                    setChoices(new LoadableDetachableModel<List<SourceDocument>>()
                    {
                        private static final long serialVersionUID = 1L;

                        @Override
                        protected List<SourceDocument> load()
                        {
                            if (selectedProject != null) {
                                List<SourceDocument> allDocuments = projectRepository
                                        .listSourceDocuments(selectedProject);
                                return allDocuments;
                            }
                            else {
                                return new ArrayList<SourceDocument>();
                            }
                        }
                    });
                    setChoiceRenderer(new ChoiceRenderer<SourceDocument>("name"));
                    setNullValid(false);
                }

                @Override
                protected CharSequence getDefaultChoice(String aSelectedValue)
                {
                    return "";
                }
            });
            documents.setOutputMarkupId(true);
            documents.setMaxRows(10);
            documents
                    .add(new OnChangeAjaxBehavior()
                    {
                        private static final long serialVersionUID = 1L;

                        @Override
                        protected void onUpdate(AjaxRequestTarget aTarget)
                        {
                            selectedDocument = getModelObject().document;
                        }
                    })
                    .add(new AjaxEventBehavior("ondblclick")
                    {

                        private static final long serialVersionUID = 1L;

                        @Override
                        protected void onEvent(final AjaxRequestTarget aTarget)
                        {
                            openDataModel.setProject(selectedProject);
                            openDataModel.setDocument(selectedDocument);
                            modalWindow.close(aTarget);
                        }
                    }).add(new ResizableBehavior());
        }
    }

    private class ButtonsForm
        extends Form<Void>
    {
        public ButtonsForm(String id, final ModalWindow modalWindow)
        {
            super(id);
            add(new AjaxSubmitLink("openButton")
            {
                private static final long serialVersionUID = -755759008587787147L;

                @Override
                protected void onSubmit(AjaxRequestTarget aTarget, Form<?> aForm)
                {
                    if (selectedProject == null) {
                        aTarget.appendJavaScript("alert('No project is selected!')"); // If there is no project at all
                    }
                    else if (selectedDocument == null) {
                        aTarget.appendJavaScript("alert('Please select a document for project: " + selectedProject.getName()+"')");
                    }
                    else {
                        openDataModel.setProject(selectedProject);
                        openDataModel.setDocument(selectedDocument);
                        modalWindow.close(aTarget);
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget aTarget, Form<?> aForm)
                {

                }
            });

            add(new AjaxLink<Void>("cancelButton")
            {
                private static final long serialVersionUID = 7202600912406469768L;

                @Override
                public void onClick(AjaxRequestTarget aTarget)
                {
                    projectSelectionForm.detach();
                    documentSelectionForm.detach();
                    if(subject.equals(Mode.CURATION))
                     {
                        openDataModel.setDocument(null); // on cancel, go welcomePage
                    }
                    modalWindow.close(aTarget);

                }
            });
        }
    }
}