/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jebtk.bioinformatics.ui.groups;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.xml.XmlDoc;
import org.jebtk.graphplot.ColorCycle;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.contentpane.HTabToolbar;
import org.jebtk.modern.dataview.ModernDataViewListener;
import org.jebtk.modern.dialog.DialogEvent;
import org.jebtk.modern.dialog.DialogEventListener;
import org.jebtk.modern.dialog.MessageDialogType;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.icons.CrossVectorIcon;
import org.jebtk.modern.graphics.icons.PlusVectorIcon;
import org.jebtk.modern.graphics.icons.TrashVectorIcon;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.ribbon.ToolbarButton;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.window.ModernRibbonWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The class GroupsPanel.
 */
public class GroupsPanel extends ModernComponent implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member region group list.
   */
  private GroupList mRegionGroupList = new GroupList();

  /**
   * The member list model.
   */
  private GroupListModel mListModel = new GroupListModel();

  /**
   * The member new button.
   */
  private ModernButton mNewButton = new ModernButton(AssetService.getInstance().loadIcon(PlusVectorIcon.class, 16));

  /**
   * The member delete button.
   */
  private ModernButton mDeleteButton = new ModernButton(AssetService.getInstance().loadIcon(TrashVectorIcon.class, 16));

  /**
   * The member edit button.
   */
  private ModernButton mEditButton = new ModernButton(AssetService.getInstance().loadIcon("edit_bw", 16));

  /**
   * The member clear button.
   */
  private ModernButton mClearButton = new ToolbarButton(AssetService.getInstance().loadIcon(CrossVectorIcon.class, 16));

  /**
   * The member model.
   */
  private GroupsModel mModel;

  /**
   * The member parent.
   */
  private ModernRibbonWindow mParent;

  /** The m color cycle. */
  private ColorCycle mColorCycle = new ColorCycle();

  /**
   * The class TrackEvents.
   */
  private class TrackEvents implements ModernDataViewListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.jebtk.ui.ui.dataview.ModernDataViewListener#dataChanged(org.abh.lib.
     * event.ChangeEvent)
     */
    @Override
    public void dataChanged(ChangeEvent e) {
      update();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jebtk.ui.ui.dataview.ModernDataViewListener#dataUpdated(org.abh.lib.
     * event.ChangeEvent)
     */
    @Override
    public void dataUpdated(ChangeEvent e) {
      update();
    }
  }

  /**
   * The class DeleteGroupsCallBack.
   */
  private class DeleteGroupsCallBack implements DialogEventListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jebtk.ui.ui.dialog.DialogEventListener#statusChanged(org.jebtk.ui.ui.
     * dialog.DialogEvent)
     */
    @Override
    public void statusChanged(DialogEvent e) {
      if (e.getStatus() == ModernDialogStatus.OK) {
        ArrayList<Integer> indices = new ArrayList<Integer>();

        for (int i : mRegionGroupList.getSelectionModel()) {
          indices.add(i);
        }

        mListModel.removeValuesAt(indices);
      }
    }

  }

  /**
   * Instantiates a new groups panel.
   *
   * @param parent the parent
   * @param model  the model
   */
  public GroupsPanel(ModernRibbonWindow parent, GroupsModel model) {
    mParent = parent;
    mModel = model;

    setup();

    createUi();

    // Sync ui
    mRegionGroupList.getModel().fireDataChanged();
  }

  /**
   * Creates the ui.
   */
  public void createUi() {
    Box box = HBox.create();

    mNewButton.setToolTip("New Group", "Creat a new group.");
    box.add(mNewButton);
    mEditButton.setToolTip("Edit Groups", "Edit group properties.");
    box.add(mEditButton);
    mDeleteButton.setToolTip("Delete", "Delete selected groups.");
    box.add(mDeleteButton);

    setHeader(new HTabToolbar("Groups", box));

    ModernScrollPane scrollPane = new ModernScrollPane(mRegionGroupList);
    scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
    // scrollPane.setBorder(BorderService.getInstance().createTopBorder(10));
    setBody(scrollPane);

    setBorder(BORDER);

    /*
     * Box box = new ToolbarBottomBox();
     * 
     * mCreateButton.setToolTip("New Group", "Creat a new group.");
     * box.add(mCreateButton); box.add(ModernTheme.createHorizontalGap());
     * //mSaveButton.setToolTip("Save Groups", "Save groups for reuse.");
     * //box.add(mSaveButton); //box.add(ModernTheme.createHorizontalGap());
     * mEditButton.setToolTip("Edit Groups", "Edit group properties.");
     * box.add(mEditButton); box.add(ModernTheme.createHorizontalGap());
     * //box.add(Box.createHorizontalGlue()); mDeleteButton.setToolTip("Delete",
     * "Delete selected groups."); box.add(mDeleteButton);
     * box.add(ModernTheme.createHorizontalGap()); mClearButton.setToolTip("Clear",
     * "Remove all groups."); box.add(mClearButton);
     * 
     * add(box, BorderLayout.PAGE_END);
     */
  }

  /**
   * Setup.
   */
  private void setup() {

    mRegionGroupList.setModel(mListModel);

    mRegionGroupList.addDataViewListener(new TrackEvents());

    mNewButton.addClickListener(this);
    mDeleteButton.addClickListener(this);
    mClearButton.addClickListener(this);
    mEditButton.addClickListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.ui.ui.event.ModernClickListener#clicked(org.jebtk.ui.ui.event.
   * ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mNewButton)) {
      createGroup();
    } else if (e.getSource().equals(mEditButton)) {
      editGroups();
    } else if (e.getSource().equals(mDeleteButton)) {
      deleteGroups();
    } else if (e.getSource().equals(mClearButton)) {
      clearGroups();
    } else {
      // do nothing
    }
  }

  // Allow user to edit tracks

  /**
   * Edits the groups.
   */
  private void editGroups() {
    // tmp disable drag
    for (int i : mRegionGroupList.getSelectionModel()) {
      Group regionGroup = mRegionGroupList.getValueAt(i);

      GroupDialog dialog = new GroupDialog(mParent, regionGroup);

      dialog.setVisible(true);

      if (dialog.getStatus() == ModernDialogStatus.OK) {
        regionGroup.setName(dialog.getName());
        regionGroup.setColor(dialog.getColor());
        regionGroup.setEntries(dialog.getEntries());
      }
    }

    // Notify listeners that the tracks may have changed.
    mListModel.fireDataChanged();
  }

  /**
   * Notify the app that the tracks have changed.
   */
  private void update() {
    List<Group> regions = new ArrayList<Group>();

    for (Group region : mListModel) {
      regions.add(region);
    }

    mModel.set(regions);
  }

  /**
   * Delete groups.
   */
  private void deleteGroups() {
    ModernMessageDialog.createDialog(mParent, "Are you sure you want to delete the selected groups?",
        MessageDialogType.WARNING_OK_CANCEL, new DeleteGroupsCallBack());
  }

  /**
   * Clear groups.
   */
  private void clearGroups() {
    ModernDialogStatus status = ModernMessageDialog.createDialog(mParent, "Are you sure you want to delete all groups?",
        MessageDialogType.WARNING_OK_CANCEL);

    if (status == ModernDialogStatus.OK) {
      mListModel.clear();
    }
  }

  /**
   * Creates the group.
   */
  private void createGroup() {
    // Create a default group for the user to edit
    Group group = new Group("Group " + (mListModel.getItemCount() + 1), mColorCycle.next());

    GroupDialog dialog = new GroupDialog(mParent, group);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.OK) {
      group.setName(dialog.getName());
      group.setColor(dialog.getColor());
      group.setEntries(dialog.getEntries());

      mListModel.addValue(group);
    }
  }

  /**
   * Save groups.
   *
   * @throws TransformerException         the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws IOException                  Signals that an I/O exception has
   *                                      occurred.
   */
  public void saveGroups() throws TransformerException, ParserConfigurationException, IOException {
    if (mListModel.getItemCount() == 0) {
      return;
    }

    saveGroups(RecentFilesService.getInstance().getPwd());
  }

  /**
   * Save groups.
   *
   * @param pwd the working directory
   * @throws TransformerException         the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   * @throws IOException                  Signals that an I/O exception has
   *                                      occurred.
   */
  public void saveGroups(Path pwd) throws TransformerException, ParserConfigurationException, IOException {
    if (mListModel.getItemCount() == 0) {
      return;
    }

    saveGroupsFile(FileDialog.save(mParent).filter(new GroupsGuiFileFilter()).getFile(pwd));
  }

  /**
   * Save groups file.
   *
   * @param file the file
   * @throws TransformerException         the transformer exception
   * @throws ParserConfigurationException the parser configuration exception
   */
  public void saveGroupsFile(Path file) throws TransformerException, ParserConfigurationException {
    if (mListModel.getItemCount() == 0) {
      return;
    }

    if (file == null) {
      return;
    }

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

    // root elements
    Document doc = docBuilder.newDocument();

    Element element = doc.createElement("region-groups");
    doc.appendChild(element);

    for (Group region : mListModel) {
      region.toXml(doc, element);
    }

    XmlDoc.write(doc, file);

    RecentFilesService.getInstance().add(file);
  }

  /**
   * Adds the groups.
   *
   * @param groups the groups
   */
  public void addGroups(List<Group> groups) {
    mListModel.addValues(groups);
  }

  /**
   * Adds the group.
   *
   * @param group the group
   */
  public void addGroup(Group group) {
    mListModel.addValue(group);
  }
}
