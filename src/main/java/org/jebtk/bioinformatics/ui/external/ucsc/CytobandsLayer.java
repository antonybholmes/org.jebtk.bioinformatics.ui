/**
 * Copyright 2017 Antony Holmes
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
 */
package org.jebtk.bioinformatics.ui.external.ucsc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.Cytoband;
import org.jebtk.bioinformatics.ext.ucsc.Cytobands;
import org.jebtk.bioinformatics.ext.ucsc.CytobandsService;
import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesClippedLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;
import org.jebtk.modern.theme.ThemeService;

/**
 * Layout out peak plots in a column.
 * 
 * @author Antony Holmes
 *
 */
public class CytobandsLayer extends AxesClippedLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  //
  // Stain ids to control band color.
  //

  /** The Constant CENTROMERE. */
  private static final String CENTROMERE = "acen";

  /** The Constant STAIN_25. */
  private static final String STAIN_25 = "gpos25";

  /** The Constant STAIN_50. */
  private static final String STAIN_50 = "gpos50";

  /** The Constant STAIN_75. */
  private static final String STAIN_75 = "gpos75";

  /** The Constant STAIN_100. */
  private static final String STAIN_100 = "gpos100";

  /** The Constant STAIN_GVAR. */
  private static final String STAIN_GVAR = "gvar";

  /** The Constant STAIN_STALK. */
  private static final String STAIN_STALK = "stalk";

  /** The Constant STAIN_33. */
  private static final String STAIN_33 = "gpos33";

  /** The Constant STAIN_66. */
  private static final String STAIN_66 = "gpos66";

  //
  // Band colors.
  //

  /** The Constant BORDER_COLOR. */
  private static final Color BORDER_COLOR = ThemeService.getInstance().getColors().getGray(10);

  /** The Constant CENTROMERE_COLOR. */
  private static final Color CENTROMERE_COLOR = SettingsService.getInstance().getColor("cytobands.centromere.color"); // #d35f5f");

  /** The Constant COLOR_100. */
  private static final Color COLOR_100 = Color.BLACK;

  /** The Constant COLOR_75. */
  private static final Color COLOR_75 = ColorUtils.getGrayScale(1 - 0.75); // ColorUtils.decodeHtmlColor("#404040");

  /** The Constant COLOR_50. */
  private static final Color COLOR_50 = ColorUtils.getGrayScale(0.5); // ColorUtils.decodeHtmlColor("#808080");

  /** The Constant COLOR_25. */
  private static final Color COLOR_25 = ColorUtils.getGrayScale(0.25); // ColorUtils.decodeHtmlColor("#c0c0c0");

  /** The Constant COLOR_BACKGROUND. */
  private static final Color COLOR_BACKGROUND = ColorUtils.decodeHtmlColor("#e6e6e6");

  /** The Constant COLOR_66. */
  private static final Color COLOR_66 = ColorUtils.getGrayScale(0.66);

  /** The Constant COLOR_33. */
  private static final Color COLOR_33 = ColorUtils.getGrayScale(0.33);

  /** The m prev chr. */
  private Chromosome mPrevChr;

  /** The m image. */
  private BufferedImage mImage;

  protected GenomicRegion mDisplayRegion;

  private Genome mGenome;

  /**
   * Instantiates a new cytobands layer.
   *
   * @param chr       the chr
   * @param cytobands the cytobands
   */
  public CytobandsLayer(Cytobands cytobands) {
    super("Cytobands");

    // mCytobands = cytobands;
  }

  /**
   * Sets the region.
   *
   * @param displayRegion the new region
   */
  public void setRegion(Genome genome, GenomicRegion displayRegion) {
    mGenome = genome;
    mDisplayRegion = displayRegion;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.figure.AxesClippedLayer#plotLayer(java.awt.Graphics2D,
   * org.jebtk.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure,
   * org.graphplot.figure.Axes)
   */
  @Override
  public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {
    if (context == DrawingContext.UI) {
      g2.drawImage(cacheImage(context, subFigure, axes), 0, 0, null);
    } else {
      plotCytobands(g2, context, subFigure, axes);
    }

    // plotCytobands(g2, context, figure, axes);
  }

  /**
   * Cache image.
   *
   * @param context the context
   * @param figure  the figure
   * @param axes    the axes
   * @return the buffered image
   */
  private BufferedImage cacheImage(DrawingContext context, SubFigure figure, Axes axes) {
    Chromosome chr = mDisplayRegion.getChr();

    if (mPrevChr == null || !chr.equals(mPrevChr)) {
      int minX = axes.toPlotX1(axes.getX1Axis().getLimits().getMin());
      int maxX = axes.toPlotX1(axes.getX1Axis().getLimits().getMax());
      int y1 = axes.toPlotY1(axes.getY1Axis().getLimits().getMin());
      int y2 = axes.toPlotY1(axes.getY1Axis().getLimits().getMax());

      int w = maxX - minX + 1;
      int h = y1 - y2 + 1;

      mImage = ImageUtils.createImage(w, h);

      Graphics g = mImage.getGraphics();

      try {
        Graphics2D g2 = ImageUtils.createAATextGraphics(g);

        try {
          plotCytobands(g2, context, figure, axes);
        } finally {
          g2.dispose();
        }
      } finally {
        g.dispose();
      }

      mPrevChr = chr;
    }

    return mImage;
  }

  /**
   * Plot cytobands.
   *
   * @param g2      the g 2
   * @param context the context
   * @param figure  the figure
   * @param axes    the axes
   */
  private void plotCytobands(Graphics2D g2, DrawingContext context, SubFigure figure, Axes axes) {

    Chromosome chr = mDisplayRegion.getChr();
    
    System.err.println("cytolayer:" + chr + " " + mGenome);
    System.err.println("cytolayerss:" + chr + " " + CytobandsService.getInstance().getCytobands(mGenome));
    List<Cytoband> bands = CytobandsService.getInstance().getCytobands(mGenome).getCytobands(chr); // mCytobands.getCytobands(mDisplayRegion.getChr());

    // Clipping
    int centi1 = Integer.MAX_VALUE;
    int centi2 = Integer.MIN_VALUE;

    boolean hasCentromere = false;

    for (int i = 0; i < bands.size(); ++i) {
      Cytoband cytoband = bands.get(i);

      // determine the look from the stain value

      if (cytoband.getStain().contains(CENTROMERE)) {
        centi1 = Math.min(i, centi1);
        centi2 = Math.max(i, centi2);

        hasCentromere = true;
      }
    }

    if (hasCentromere) {
      plotCentromere(g2, axes, bands, centi1, centi2);
    } else {
      plot(g2, axes, bands);
    }
  }

  /**
   * Plot centromere.
   *
   * @param g2     the g 2
   * @param axes   the axes
   * @param bands  the bands
   * @param centi1 the centi 1
   * @param centi2 the centi 2
   */
  private static void plotCentromere(Graphics2D g2, Axes axes, List<Cytoband> bands, int centi1, int centi2) {
    int y1 = axes.toPlotY1(axes.getY1Axis().getLimits().getMax() * 0.8);
    int y2 = axes.toPlotY1(axes.getY1Axis().getLimits().getMax() * 0.2);
    int h = y2 - y1 + 1;
    int h2 = h / 2;

    int minX = axes.toPlotX1(axes.getX1Axis().getLimits().getMin()) + 1;
    int maxX = axes.toPlotX1(axes.getX1Axis().getLimits().getMax()) - 1;

    int cent1Start = axes.toPlotX1(bands.get(centi1).getStart());
    int cent2End = axes.toPlotX1(bands.get(centi2).getEnd());
    int centW = cent2End - cent1Start;
    int centW2 = centW / 2 + centW % 2;
    int centMid = (cent1Start + cent2End) / 2;

    Graphics2D g2Temp;

    //
    // band set 1
    //

    GeneralPath clip1 = new GeneralPath();

    // offset x by 1 to ensure clip is itself not clipped by the graph
    // boundary
    clip1.moveTo(minX + h2, y2);
    clip1.lineTo(centMid - h2, y2);
    clip1.append(new Arc2D.Double(centMid - h, y1, h, h - 1, 270, 180, Arc2D.OPEN), true);
    clip1.lineTo(minX + h2, y1);
    clip1.append(new Arc2D.Double(minX, y1, h, h - 1, 90, 180, Arc2D.OPEN), true);

    clip1.closePath();

    g2Temp = ImageUtils.clone(g2);

    try {
      g2Temp.clip(clip1);

      drawBands(g2Temp, axes, bands, y1, h, 0, centi1 - 1);

      g2Temp.setColor(CENTROMERE_COLOR);
      g2Temp.fillRect(centMid - centW2, y1, centW2, h);
    } finally {
      g2Temp.dispose();
    }

    //
    // band 2
    //

    GeneralPath clip2 = new GeneralPath();

    clip2.moveTo(centMid + h2, y2);
    clip2.lineTo(maxX - h2, y2);
    clip2.append(new Arc2D.Double(maxX - h, y1, h, h - 1, 270, 180, Arc2D.OPEN), true);
    clip2.lineTo(centMid + h2, y1);
    clip2.append(new Arc2D.Double(centMid, y1, h, h - 1, 90, 180, Arc2D.OPEN), true);

    clip2.closePath();

    g2Temp = ImageUtils.clone(g2);

    try {
      g2Temp.clip(clip2);

      drawBands(g2Temp, axes, bands, y1, h, centi2 + 1, bands.size() - 1);

      g2Temp.setColor(CENTROMERE_COLOR);
      g2Temp.fillRect(centMid, y1, centW2, h);
    } finally {
      g2Temp.dispose();
    }

    //
    // Border
    //

    g2.setColor(BORDER_COLOR);

    g2.draw(clip1);
    g2.draw(clip2);
  }

  /**
   * Draw cytobands without centromeres.
   *
   * @param g2    the g 2
   * @param axes  the axes
   * @param bands the bands
   */
  private static void plot(Graphics2D g2, Axes axes, List<Cytoband> bands) {
    int y1 = axes.toPlotY1(axes.getY1Axis().getLimits().getMax() * 0.8);
    int y2 = axes.toPlotY1(axes.getY1Axis().getLimits().getMax() * 0.2);
    int h = y2 - y1 + 1;
    int h2 = h / 2;

    int minX = axes.toPlotX1(axes.getX1Axis().getLimits().getMin()) + 1;
    int maxX = axes.toPlotX1(axes.getX1Axis().getLimits().getMax()) - 1;

    Graphics2D g2Temp;

    //
    // band set 1
    //

    GeneralPath clip1 = new GeneralPath();

    // offset x by 1 to ensure clip is itself not clipped by the graph
    // boundary
    clip1.moveTo(minX + h2, y2);
    clip1.lineTo(maxX - h2, y2);
    clip1.append(new Arc2D.Double(maxX - h, y1, h, h - 1, 270, 180, Arc2D.OPEN), true);
    clip1.lineTo(minX + h2, y1);
    clip1.append(new Arc2D.Double(minX, y1, h, h - 1, 90, 180, Arc2D.OPEN), true);

    clip1.closePath();

    g2Temp = ImageUtils.createAATextGraphics(g2);

    try {
      g2Temp.clip(clip1);

      drawBands(g2Temp, axes, bands, y1, h, 0, bands.size() - 1);
    } finally {
      g2Temp.dispose();
    }

    //
    // Border
    //

    g2.setColor(Color.BLACK);

    g2.draw(clip1);
  }

  /**
   * Draw bands.
   *
   * @param g2    the g 2
   * @param axes  the axes
   * @param bands the bands
   * @param y     the y
   * @param h     the h
   * @param i1    the i 1
   * @param i2    the i 2
   */
  private static void drawBands(Graphics2D g2, Axes axes, List<Cytoband> bands, int y, int h, int i1, int i2) {
    for (int i = i1; i <= i2; ++i) {
      Cytoband cytoband = bands.get(i);

      // determine the look from the stain value

      boolean isCentromere = cytoband.getStain().contains(CENTROMERE);

      Color color;

      if (isCentromere) {
        color = CENTROMERE_COLOR;
      } else if (cytoband.getStain().equals(STAIN_100)) {
        color = COLOR_100; // ThemeService.getInstance().getColors().getHighlight(6);
      } else if (cytoband.getStain().equals(STAIN_GVAR)) {
        color = COLOR_100;
      } else if (cytoband.getStain().equals(STAIN_STALK)) {
        color = COLOR_50;
      } else if (cytoband.getStain().equals(STAIN_75)) {
        color = COLOR_75;
      } else if (cytoband.getStain().equals(STAIN_66)) {
        color = COLOR_66;
      } else if (cytoband.getStain().equals(STAIN_50)) {
        color = COLOR_50;
      } else if (cytoband.getStain().equals(STAIN_33)) {
        color = COLOR_33;
      } else if (cytoband.getStain().equals(STAIN_25)) {
        color = COLOR_25;
      } else {
        color = COLOR_BACKGROUND;
      }

      g2.setColor(color);

      // g2.drawString(id, textX - g2.getFontMetrics().stringWidth(id), textY);

      int x1 = axes.toPlotX1(cytoband.getStart());
      int x2 = axes.toPlotX1(cytoband.getEnd());

      // System.err.println("x1 " + x1 + " " + x2 + " " + g.getStart() + " " +
      // g.getEnd());

      // px1 = Mathematics.bound(x1, minX, maxX);
      // px2 = Mathematics.bound(x2, minX, maxX);

      int w = Math.max(1, x2 - x1);

      g2.fillRect(x1, y, w, h);
    }
  }
}
