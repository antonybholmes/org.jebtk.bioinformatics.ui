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
package org.jebtk.bioinformatics.ui.motifs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jebtk.bioinformatics.motifs.Motif;
import org.jebtk.bioinformatics.motifs.MotifsDataSourceService;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.text.Splitter;
import org.jebtk.core.text.TextUtils;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.core.tree.TreeRootNode;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.event.ModernSelectionListener;
import org.jebtk.modern.graphics.icons.MinusVectorIcon;
import org.jebtk.modern.graphics.icons.PlusVectorIcon;
import org.jebtk.modern.ribbon.RibbonButton;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.search.ModernSearchExtPanel;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.tree.ModernTreeEvent;
import org.jebtk.modern.tree.TreeEventListener;
import org.jebtk.modern.window.ModernWindow;

// TODO: Auto-generated Javadoc
/**
 * The class MotifsTreePanel.
 */
public final class MotifsTreePanel extends ModernComponent {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant NO_MOTIFS.
   */
  private static final List<Motif> NO_MOTIFS = Collections.unmodifiableList(new ArrayList<Motif>());

  /**
   * Determines whether to expand or collapse the motif tree based on how many
   * child nodes are in the tree.
   */
  private static final int EXPAND_THRESHOLD = 100;

  /**
   * The member tree.
   */
  private ModernTree<Motif> mTree = new ModernTree<Motif>();

  /**
   * The member expand button.
   */
  private ModernButton mExpandButton = new RibbonButton(AssetService.getInstance().loadIcon(PlusVectorIcon.class, 16));

  /**
   * The member collapse button.
   */
  private ModernButton mCollapseButton = new RibbonButton(
      AssetService.getInstance().loadIcon(MinusVectorIcon.class, 16));

  /**
   * The member refresh button.
   */
  private ModernButton mRefreshButton = new RibbonButton(AssetService.getInstance().loadIcon("refresh", 16));

  // private ModernButton mSearchButton =
  // new ModernButton(UIResources.getInstance().loadIcon("search", 16));

  /**
   * The member search panel.
   */
  private ModernSearchExtPanel mSearchPanel;

  /**
   * The member model.
   */
  private MotifModel mModel;

  /** The m window. */
  private ModernWindow mWindow;

  // private boolean mState = true;

  /**
   * The class TreeEvents.
   */
  private class TreeEvents implements TreeEventListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.jebtk.ui.ui.tree.TreeEventListener#treeNodeDragged(org.jebtk.ui.ui.
     * tree.ModernTreeEvent)
     */
    @Override
    public void treeNodeDragged(ModernTreeEvent e) {
      // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jebtk.ui.ui.tree.TreeEventListener#treeNodeClicked(org.jebtk.ui.ui.
     * tree.ModernTreeEvent)
     */
    @Override
    public void treeNodeClicked(ModernTreeEvent e) {
      if (mModel != null) {
        mModel.set(getSelectedMotifs());
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jebtk.ui.ui.tree.TreeEventListener#treeNodeDoubleClicked(org.jebtk.ui
     * .ui.tree.ModernTreeEvent)
     */
    @Override
    public void treeNodeDoubleClicked(ModernTreeEvent e) {
      // TODO Auto-generated method stub

    }

  }

  /**
   * The class RefreshEvents.
   */
  public class RefreshEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.jebtk.ui.ui.event.ModernClickListener#clicked(org.jebtk.ui.ui.event.
     * ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      try {
        refresh();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }

  }

  /**
   * The class CollapseEvents.
   */
  public class CollapseEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.jebtk.ui.ui.event.ModernClickListener#clicked(org.jebtk.ui.ui.event.
     * ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      setState(false);
    }
  }

  /**
   * The class ExpandEvents.
   */
  public class ExpandEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.jebtk.ui.ui.event.ModernClickListener#clicked(org.jebtk.ui.ui.event.
     * ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      setState(true);
    }
  }

  /**
   * The Class SelectionEvents.
   */
  private class SelectionEvents implements ModernSelectionListener {

    @Override
    public void selectionAdded(ChangeEvent e) {
      mModel.set(getSelectedMotifs());
    }

    @Override
    public void selectionRemoved(ChangeEvent e) {
      // TODO Auto-generated method stub

    }

  }

  /**
   * Instantiates a new motifs tree panel.
   *
   * @param window the window
   */
  public MotifsTreePanel(ModernWindow window) {
    this(window, new MotifModel());
  }

  /**
   * Instantiates a new motifs tree panel.
   *
   * @param window the window
   * @param model  the model
   */
  public MotifsTreePanel(ModernWindow window, MotifModel model) {
    mWindow = window;
    mModel = model;

    mSearchPanel = new ModernSearchExtPanel(window);

    setup();

    createUi();

    mRefreshButton.addClickListener(new RefreshEvents());
    mCollapseButton.addClickListener(new CollapseEvents());
    mExpandButton.addClickListener(new ExpandEvents());
    mSearchPanel.addClickListener(new RefreshEvents());

    try {
      refresh();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Search.
   *
   * @param items the items
   */
  public void search(Collection<String> items) {
    mSearchPanel.search(items);
  }

  /**
   * Creates the ui.
   */
  public void createUi() {
    ModernComponent c = new ModernComponent(mSearchPanel);
    c.setBorder(ModernWidget.BOTTOM_BORDER);
    setHeader(c);

    ModernScrollPane scrollPane = new ModernScrollPane(mTree).setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
    // scrollPane.setVerticalScrollBarPolicy(ScrollBarPolicy.ALWAYS);
    // Ui.setSize(scrollPane, new Dimension(250, Short.MAX_VALUE));

    // scrollPane.setMinimumSize(new Dimension(100, Short.MAX_VALUE));
    // scrollPane.setPreferredSize(new Dimension(250, Short.MAX_VALUE));
    // scrollPane.setMaximumSize(new Dimension(800, Short.MAX_VALUE));

    // ModernLineBorderPanel panel = new ModernLineBorderPanel(scrollPane);

    // panel.setBorder(TOP_BORDER);
    setBody(scrollPane);

    /*
     * Box box = HBox.create(); box.setBorder(TOP_BORDER);
     * mRefreshButton.setToolTip("Refresh", "Refresh the database.");
     * box.add(mRefreshButton); box.add(createHGap());
     * mExpandButton.setToolTip("Expand", "Expand all folders.");
     * box.add(mExpandButton); box.add(createHGap());
     * mCollapseButton.setToolTip("Collapse", "Collapse all folders.");
     * box.add(mCollapseButton); add(box, BorderLayout.PAGE_END);
     */
  }

  /**
   * Setup.
   */
  private void setup() {
    mTree.addTreeListener(new TreeEvents());

    mTree.addSelectionListener(new SelectionEvents());
  }

  /**
   * Generate a tree view of a sample folder and its sub folders.
   *
   * @throws Exception the exception
   */
  public void refresh() throws Exception {
    TreeRootNode<Motif> root = new TreeRootNode<>();

    List<String> terms = Splitter.on(TextUtils.COMMA_DELIMITER).trim().ignoreEmptyStrings()
        .text(mSearchPanel.getText());

    TreeNode<Motif> node = new TreeNode<>("Motifs");

    MotifsDataSourceService.getInstance().createTree(node, terms, mSearchPanel.getInList(), mSearchPanel.getExact(),
        mSearchPanel.getCaseSensitive());

    root.addChild(node);

    mTree.setRoot(root);

    // The motifs node is always expanded
    node.updateExpanded(true);
    // The motifs node's children are expanded on condition there are
    // fewer than EXPAND_THRESHOLD
    node.updateChildrenAreExpanded(node.getCumulativeChildCount() <= EXPAND_THRESHOLD, true);
    node.fireTreeNodeChanged();

    if (mTree.getRoot().getCumulativeChildCount() == 0) {
      ModernMessageDialog.createInformationDialog(mWindow, "No motifs were found.");
    }
  }

  // private void setState() {
  // setState(mState);
  // }

  /**
   * Sets the state.
   *
   * @param state the new state
   */
  private void setState(boolean state) {
    // mState = state;

    mTree.getRoot().setChildrenAreExpanded(state);
  }

  /**
   * Sets the selected.
   *
   * @param i the new selected
   */
  public void setSelected(int i) {
    mTree.selectNode(i);
  }

  /**
   * Gets the selected motifs.
   *
   * @return the selected motifs
   */
  public List<Motif> getSelectedMotifs() {
    if (mTree.getSelectedNodes().isEmpty()) {
      return NO_MOTIFS; // new ArrayList<ExperimentSearchResult>();
    }

    List<Motif> motifs = new ArrayList<>();

    for (TreeNode<Motif> node : mTree.getSelectedNodes()) {
      selectedMotifs(node, motifs);
    }

    return motifs;
  }

  /**
   * Recursively examine a node and its children to find those with experiments.
   *
   * @param node   the node
   * @param motifs the motifs
   */
  private void selectedMotifs(TreeNode<Motif> node, List<Motif> motifs) {
    if (node.getValue() != null) {
      motifs.add(node.getValue());
    }

    for (TreeNode<Motif> child : node) {
      selectedMotifs(child, motifs);
    }
  }
}
