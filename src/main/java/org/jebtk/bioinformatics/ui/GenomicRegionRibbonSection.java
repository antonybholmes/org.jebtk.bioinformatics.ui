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
package org.jebtk.bioinformatics.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Timer;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.ChromosomeService;
import org.jebtk.bioinformatics.genomic.GenesService;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicRegionModel;
import org.jebtk.bioinformatics.genomic.GenomicType;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.graphics.icons.MinusVectorIcon;
import org.jebtk.modern.graphics.icons.PlusVectorIcon;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonRoundButton;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.ribbon.RibbonStripContainer;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.text.SearchTextBorderPanel;

/**
 * Allows user to select a color map.
 *
 * @author Antony Holmes
 *
 */
public class GenomicRegionRibbonSection extends RibbonSection {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant SHIFT.
   */
  private static final double SHIFT = 0.05;

  /**
   * The constant TIMER_DELAY.
   */
  private static final int TIMER_DELAY = 10;

  /**
   * The constant LONG_TIMER_DELAY.
   */
  private static final int LONG_TIMER_DELAY = 200;

  /**
   * The constant LOCATION_SIZE.
   */
  private static final Dimension LOCATION_SIZE = new Dimension(240, ModernWidget.WIDGET_HEIGHT);

  /**
   * The member location field.
   */
  // protected ModernComboBox mLocationField = new ModernComboBox();
  protected ModernTextField mLocationField = new ModernTextField();

  // private ModernTextField m5pExtField = new ModernTextField("0");

  // private ModernTextField m3pExtField = new ModernTextField("0");

  /**
   * The member model.
   */
  private GenomicRegionModel mModel;

  /**
   * The member zoom in button.
   */
  private ModernButton mZoomInButton = new RibbonRoundButton(
      AssetService.getInstance().loadIcon(PlusVectorIcon.class, 16));

  /**
   * The member zoom out button.
   */
  private ModernButton mZoomOutButton = new RibbonRoundButton(
      AssetService.getInstance().loadIcon(MinusVectorIcon.class, 16));

  /**
   * The member move left button.
   */
  private ModernWidget mMoveLeftButton = new RibbonRoundButton(AssetService.getInstance().loadIcon("left_arrow", 16));

  /**
   * The member move right button.
   */
  private ModernWidget mMoveRightButton = new RibbonRoundButton(AssetService.getInstance().loadIcon("right_arrow", 16));

  /**
   * The member genome model.
   */
  protected GenomeModel mGenomeModel;

  /**
   * The member used.
   */
  private Set<String> mUsed = new HashSet<String>();

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
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        change();
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent e) {
      // TODO Auto-generated method stub

    }

  }

  /**
   * The class ZoomInEvents.
   */
  private class ZoomInEvents extends MouseAdapter implements ActionListener {

    /**
     * The member timer.
     */
    private Timer mTimer;

    /**
     * Instantiates a new zoom in events.
     */
    public ZoomInEvents() {
      mTimer = new Timer(LONG_TIMER_DELAY, this);
      mTimer.setInitialDelay(0);
      mTimer.setRepeats(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
      mTimer.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
      mTimer.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      zoomIn();
    }
  }

  /**
   * The class ZoomOutEvents.
   */
  private class ZoomOutEvents extends MouseAdapter implements ActionListener {

    /**
     * The member timer.
     */
    private Timer mTimer;

    /**
     * Instantiates a new zoom out events.
     */
    public ZoomOutEvents() {
      mTimer = new Timer(LONG_TIMER_DELAY, this);
      mTimer.setInitialDelay(0);
      mTimer.setRepeats(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
      mTimer.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
      mTimer.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      zoomOut();
    }
  }

  /**
   * The class MoveLeftEvents.
   */
  private class MoveLeftEvents extends MouseAdapter implements ActionListener {

    /**
     * The member timer.
     */
    private Timer mTimer;

    /**
     * Instantiates a new move left events.
     */
    public MoveLeftEvents() {
      mTimer = new Timer(TIMER_DELAY, this);
      mTimer.setInitialDelay(0);
      mTimer.setRepeats(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
      mTimer.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
      mTimer.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        moveLeft();
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * The class MoveRightEvents.
   */
  private class MoveRightEvents extends MouseAdapter implements ActionListener {

    /**
     * The member timer.
     */
    private Timer mTimer;

    /**
     * Instantiates a new move right events.
     */
    public MoveRightEvents() {
      mTimer = new Timer(TIMER_DELAY, this);
      mTimer.setInitialDelay(0);
      mTimer.setRepeats(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void mousePressed(MouseEvent e) {
      mTimer.start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent)
     */
    @Override
    public void mouseReleased(MouseEvent e) {
      mTimer.stop();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      try {
        moveRight();
      } catch (ParseException e1) {
        e1.printStackTrace();
      }
    }
  }

  /**
   * The class RefreshEvents.
   */
  private class RefreshEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      refresh();
    }

  }

  /**
   * Instantiates a new genomic region ribbon section.
   *
   * @param ribbon      the ribbon
   * @param regionModel the model
   * @param genomeModel the genome model
   */
  public GenomicRegionRibbonSection(Ribbon ribbon, GenomicRegionModel regionModel, GenomeModel genomeModel) {
    super(ribbon, "Region");

    mModel = regionModel;
    mGenomeModel = genomeModel;

    RibbonStripContainer box = new RibbonStripContainer();

    box.add(mZoomInButton);
    box.add(mZoomOutButton);
    box.add(createHGap());

    box.add(new ModernComponent(new SearchTextBorderPanel(mLocationField), BORDER, LOCATION_SIZE));

    box.add(createHGap());
    box.add(mMoveLeftButton);
    box.add(mMoveRightButton);

    add(box);

    RefreshEvents ce = new RefreshEvents();

    mLocationField.addKeyListener(new KeyEvents());
    // mLocationField.addClickListener(new LocationEvents());

    mModel.addChangeListener(ce);
    mGenomeModel.addChangeListener(ce);

    mZoomInButton.addMouseListener(new ZoomInEvents());
    mZoomOutButton.addMouseListener(new ZoomOutEvents());

    mMoveLeftButton.addMouseListener(new MoveLeftEvents());
    mMoveRightButton.addMouseListener(new MoveRightEvents());

    mLocationField.setEditable(true);

    refresh();
  }

  /**
   * Refresh.
   */
  private void refresh() {
    mLocationField.setText(mModel.get().getFormattedLocation());
  }

  /**
   * Change.
   *
   * @throws ParseException the parse exception
   */
  private void change() {
    GenomicRegion region = parse(mGenomeModel.get());

    if (region != null) {
      // Add the location before updating the model, since the model
      // refresh will change gene symbols to location and we want to
      // store what the user typed, not what we modified it to.

      String name = mLocationField.getText();

      if (!mUsed.contains(name)) {
        // mLocationField.addScrollMenuItem(name);
        mUsed.add(name);
      }

      mModel.set(region);
    }
  }

  /**
   * Parses the.
   *
   * @return the genomic region
   * @throws ParseException the parse exception
   */
  protected GenomicRegion parse(Genome genome) {
    System.err.println("genome ribbon section");

    String text = mLocationField.getText().toLowerCase();

    GenomicRegion region = null;

    if (GenomicRegion.CHR_ONLY_PATTERN.matcher(text).matches()) {
      // use the whole chromosome

      Chromosome chr = ChromosomeService.getInstance().chr(genome, text);

      int size = ChromosomeService.getInstance().size(genome, chr); // chromosome.getSize();

      region = new GenomicRegion(chr, 1, size);

    } else if (text.startsWith("chr")) { // remove commas
      region = GenomicRegion.parse(genome, mLocationField.getText());

      int size = ChromosomeService.getInstance().size(genome, region.mChr);

      region = new GenomicRegion(region.getChr(), Math.max(1, region.getStart()), Math.min(region.getEnd(), size));

    } else {
      // assume its a gene

      // Genome g = GenesService.getInstance().getFirstGeneDb(genome.getAssembly());

      try {
        region = GenesService.getInstance().getGenes(genome).getElement(genome, text, GenomicType.TRANSCRIPT);
      } catch (IOException e) {
        e.printStackTrace();
      }

      System.err.println("what " + genome + " " + text + " " + region);
    }

    return region;
  }

  /**
   * Zoom in.
   *
   * @throws ParseException the parse exception
   */
  private void zoomIn() {
    zoom(0.25);
  }

  /**
   * Zoom out.
   *
   * @throws ParseException the parse exception
   */
  private void zoomOut() {
    zoom(4);
  }

  /**
   * Zoom.
   *
   * @param scale the scale
   * @throws ParseException the parse exception
   */
  private void zoom(double scale) {
    GenomicRegion region = parse(mGenomeModel.get());

    if (region == null) {
      return;
    }

    int size = ChromosomeService.getInstance().size(mGenomeModel.get(), region.mChr);

    int midPoint = (region.getStart() + region.getEnd()) / 2;

    int d = (int) ((region.getEnd() - region.getStart()) * scale);
    int d2 = Math.max(d / 2, 1);

    int start = (int) Math.max(Math.min(midPoint - d2, size), 1);
    int end = (int) Math.max(Math.min(midPoint + d2, size), 1);

    GenomicRegion newRegion = new GenomicRegion(region.mChr, start, end);

    mModel.set(newRegion);
  }

  /**
   * Move left.
   *
   * @throws ParseException the parse exception
   */
  private void moveLeft() throws ParseException {
    move(-SHIFT);
  }

  /**
   * Move right.
   *
   * @throws ParseException the parse exception
   */
  private void moveRight() throws ParseException {
    move(SHIFT);
  }

  /**
   * Move.
   *
   * @param p the p
   * @throws ParseException the parse exception
   */
  private void move(double p) throws ParseException {
    Genome genome = mGenomeModel.get();

    GenomicRegion region = parse(genome);

    if (region == null) {
      return;
    }

    int shift = (int) (region.getLength() * p);

    GenomicRegion newRegion = GenomicRegion.shift(genome, region, shift);

    mModel.set(newRegion);
  }
}
