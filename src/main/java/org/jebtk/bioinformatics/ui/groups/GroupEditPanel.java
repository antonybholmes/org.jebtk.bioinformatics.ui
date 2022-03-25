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

import java.awt.Color;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;

import org.jebtk.bioinformatics.ext.ucsc.Bed;
import org.jebtk.bioinformatics.ext.ucsc.BedGraph;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.file.BioPathUtils;
import org.jebtk.bioinformatics.genomic.GenomicElement;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicType;
import org.jebtk.bioinformatics.ui.external.ucsc.BedGraphGuiFileFilter;
import org.jebtk.bioinformatics.ui.external.ucsc.BedGuiFileFilter;
import org.jebtk.bioinformatics.ui.genome.RegionsTextArea;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.ui.external.microsoft.AllXlsxGuiFileFilter;
import org.jebtk.math.ui.external.microsoft.XlsxGuiFileFilter;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.BorderService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.graphics.icons.OpenFolderVectorIcon;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.io.TxtGuiFileFilter;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.ribbon.RibbonButton;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernClipboardTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.window.ModernWindow;

/**
 * Allows a matrix group to be edited.
 *
 * @author Antony Holmes
 */
public class GroupEditPanel extends ModernComponent implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member color button.
   */
  private ColorSwatchButton mColorButton;

  /**
   * The member name field.
   */
  private ModernTextField mNameField = new ModernClipboardTextField("Name");

  /**
   * The member group.
   */
  private Group mGroup;

  /**
   * The member text area.
   */
  private RegionsTextArea mTextArea = new RegionsTextArea();

  /**
   * The member parent.
   */
  private ModernWindow mParent;

  /**
   * The member load button.
   */
  private RibbonButton mLoadButton = new RibbonButton("Load...",
      AssetService.getInstance().loadIcon(OpenFolderVectorIcon.class, 16));

  /**
   * Instantiates a new group edit panel.
   *
   * @param parent the parent
   * @param group  the group
   */
  public GroupEditPanel(ModernWindow parent, Group group) {
    mParent = parent;
    mGroup = group;

    mNameField.setText(group.getName());
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    int[] rows = { ModernWidget.WIDGET_HEIGHT };
    int[] cols = { 80, 300 };

    MatrixPanel matrixPanel = new MatrixPanel(rows, cols, ModernWidget.PADDING, ModernWidget.PADDING);

    mColorButton = new ColorSwatchButton(parent, mGroup.getColor());

    matrixPanel.add(new ModernAutoSizeLabel("Name"));
    matrixPanel.add(new ModernTextBorderPanel(mNameField));
    matrixPanel.add(new ModernAutoSizeLabel("Color"));

    Box box = HBox.create();
    box.add(mColorButton);

    matrixPanel.add(box);

    matrixPanel.setBorder(BorderService.getInstance().createBottomBorder(10));

    setHeader(matrixPanel);

    ModernScrollPane scrollPane = new ModernScrollPane(mTextArea).setVerticalScrollBarPolicy(ScrollBarPolicy.ALWAYS);

    setBody(scrollPane);

    box = HBox.create();

    box.setBorder(TOP_BORDER);

    box.add(mLoadButton);

    setFooter(box);

    mLoadButton.addClickListener(this);

    List<String> regions = new ArrayList<String>();

    for (String region : mGroup) {
      regions.add(region);
    }

    mTextArea.setText(regions);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.Component#getName()
   */
  public String getName() {
    return mNameField.getText();
  }

  /**
   * Gets the color.
   *
   * @return the color
   */
  public Color getColor() {
    return mColorButton.getSelectedColor();
  }

  /**
   * Gets the entries.
   *
   * @return the entries
   */
  public List<String> getEntries() {
    return mTextArea.getLines();
  }

  /**
   * Open files.
   *
   * @throws Exception the exception
   */
  public void openFiles() throws Exception {
    openFiles(RecentFilesService.getInstance().getPwd());
  }

  /**
   * Open files.
   *
   * @param pwd the pwd
   * @throws Exception the exception
   */
  public void openFiles(Path pwd) throws Exception {
    openFiles(FileDialog.open(mParent).filter(new AllXlsxGuiFileFilter(), new XlsxGuiFileFilter(),
        new TxtGuiFileFilter(), new BedGuiFileFilter(), new BedGraphGuiFileFilter()).getFiles(pwd));
  }

  /**
   * Open files.
   *
   * @param files the files
   * @throws Exception the exception
   */
  public void openFiles(List<Path> files) throws Exception {
    if (files == null) {
      return;
    }

    for (Path file : files) {

      if (BioPathUtils.ext().bedgraph().test(file)) { // getFileExt(file).equals("bedgraph"))
                                                      // {
        List<BedGraph> bedGraphs = BedGraph.parse(file);

        List<GenomicRegion> regions = new ArrayList<GenomicRegion>();

        for (UCSCTrack bedGraph : bedGraphs) {
          for (GenomicElement region : bedGraph.getElements()) {
            regions.add(region);
          }
        }

        mTextArea.setRegions(regions);
      } else if (BioPathUtils.ext().bed().test(file)) { // PathUtils.getFileExt(file).equals("bed"))
                                                        // {
        mTextArea.setRegions(Bed.parseTracks(GenomicType.REGION, file).get(0));
      } else {
        // mTextArea.setRegions(GenomicRegion.parse(Excel.getTextFromFile(file,
        // true)));

        mTextArea.setText(Excel.getTextFromFile(file, true));
      }

      RecentFilesService.getInstance().add(file);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.ui.ui.event.ModernClickListener#clicked(org.jebtk.ui.ui.event.
   * ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    try {
      openFiles();
    } catch (Exception e1) {
      e1.printStackTrace();
    }
  }
}
