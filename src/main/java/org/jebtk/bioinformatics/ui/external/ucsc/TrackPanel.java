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
package org.jebtk.bioinformatics.ui.external.ucsc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;

import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.spinner.ModernCompactSpinner;
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
public class TrackPanel extends ModernPanel {

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
   * The member description field.
   */
  private ModernTextField mDescriptionField = new ModernClipboardTextField("Description");

  /**
   * The member height field.
   */
  private ModernCompactSpinner mHeightField = new ModernCompactSpinner(1, 128);

  /**
   * The member track.
   */
  private UCSCTrack mTrack;

  /**
   * The class ClickEvents.
   */
  private class ClickEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.jebtk.ui.ui.event.ModernClickListener#clicked(org.jebtk.ui.ui.event.
     * ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      edit();
    }

  }

  /**
   * The class KeyEvents.
   */
  private class KeyEvents implements KeyListener {

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
      // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent e) {
      edit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

  }

  /**
   * Instantiates a new track panel.
   *
   * @param parent the parent
   * @param track  the track
   */
  public TrackPanel(ModernWindow parent, UCSCTrack track) {
    mTrack = track;

    mNameField.setText(track.getName());
    mDescriptionField.setText(track.getDescription());
    mHeightField.setValue(track.getHeight());

    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    int[] rows = { ModernWidget.WIDGET_HEIGHT };
    int[] cols = { 120, 400 };

    MatrixPanel matrixPanel = new MatrixPanel(rows, cols, ModernWidget.PADDING, ModernWidget.PADDING);

    mColorButton = new ColorSwatchButton(parent, mTrack.getColor());

    matrixPanel.add(new ModernAutoSizeLabel("Name"));
    matrixPanel.add(new ModernTextBorderPanel(mNameField));
    matrixPanel.add(new ModernAutoSizeLabel("Description"));
    matrixPanel.add(new ModernTextBorderPanel(mDescriptionField));
    matrixPanel.add(new ModernAutoSizeLabel("Color"));

    Box box = HBox.create();
    box.add(mColorButton);

    matrixPanel.add(box);

    matrixPanel.add(new ModernAutoSizeLabel("Height (pixels)"));

    box = HBox.create();

    // ModernTextPanel panel = new ModernTextPanel(mHeightField);
    // Ui.setSize(panel, new Dimension(100, 24));

    box.add(mHeightField);

    matrixPanel.add(box);

    matrixPanel.setBorder(ModernWidget.DOUBLE_BORDER);

    add(matrixPanel);

    mColorButton.addClickListener(new ClickEvents());
    mNameField.addKeyListener(new KeyEvents());
    mDescriptionField.addKeyListener(new KeyEvents());
    mHeightField.addKeyListener(new KeyEvents());
  }

  /**
   * Edits the.
   */
  private void edit() {
    mTrack.setName(mNameField.getText());
    mTrack.setDescription(mDescriptionField.getText());
    mTrack.setColor(mColorButton.getSelectedColor());
    mTrack.setHeight(mHeightField.getIntValue());
  }

  /**
   * Gets the track.
   *
   * @return the track
   */
  public UCSCTrack getTrack() {
    return mTrack; // new MatrixGroup(nameField.getText(), regexes,
                   // colorButton.getSelectedColor());
  }
}
