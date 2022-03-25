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

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.BaseCounts;
import org.jebtk.bioinformatics.genomic.SequenceService;
import org.jebtk.bioinformatics.motifs.BaseHeight;
import org.jebtk.bioinformatics.motifs.BaseHeights;
import org.jebtk.bioinformatics.motifs.MotifHeights;
import org.jebtk.core.collections.DefaultTreeMap;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.collections.TreeMapCreator;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotClippedLayer;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.modern.font.FontService;
import org.jebtk.modern.graphics.AAMode;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ImageUtils;

/**
 * Draws scales motif letters.
 * 
 * @author Antony Holmes
 *
 */
public abstract class LettersPlotLayer extends PlotClippedLayer {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The Constant LETTER_FONT. */
  protected static final Font LETTER_FONT = FontService.getInstance().loadFont("motifs.letters.font");

  /**
   * The minimum normalized height to consider drawing a letter. This stops non
   * existent bases which still have a small p-value from being rendered
   */
  protected static final double DRAWING_HEIGHT_THRESHOLD = 0.01;

  /**
   * The motif heights.
   */
  protected MotifHeights mMotifHeights;

  /** The m Y scale map. */
  protected IterMap<Character, IterMap<Integer, Double>> mYScaleMap = DefaultTreeMap
      .create(new TreeMapCreator<Integer, Double>());

  /** The m X scale map. */
  protected IterMap<Character, IterMap<Integer, Double>> mXScaleMap = DefaultTreeMap
      .create(new TreeMapCreator<Integer, Double>());

  /**
   * Instantiates a new letters plot p layer.
   */
  public LettersPlotLayer() {
    super("Sequence");

    getAAModes().add(AAMode.AA);
    setRasterMode(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.lib.bioinformatics.plot.figure.PlotClippedLayer#plotLayer(
   * java.awt.Graphics2D, org.jebtk.ui.ui.graphics.DrawingContext,
   * edu.columbia.rdf.lib.bioinformatics.plot.figure.Figure,
   * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes,
   * edu.columbia.rdf.lib.bioinformatics.plot.figure.Plot,
   * org.abh.lib.math.matrix.DataFrame)
   */
  @Override
  public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes, Plot plot,
      DataFrame m) {
    int x1;
    int y1;

    g2.setFont(LETTER_FONT);

    int w1 = axes.toPlotX1(1) - axes.toPlotX1(0);

    // int letterHeight = ImageUtils.getg2.getFontMetrics().getAscent();// -
    // g2.getFontMetrics().getDescent();

    int h1 = axes.toPlotY1(0) - axes.toPlotY1(1);

    // how much to scale the letter so it fills the y-axis
    // double hs1 = (double)h1 / (double)letterHeight;
    // double ws1 = (double)w1 / (double)letterWidth;

    double x = 0.5;
    double y = 0;

    for (int i = 0; i < mMotifHeights.getItemCount(); ++i) {
      BaseHeights baseHeights = mMotifHeights.get(i);

      y = 0;

      for (BaseHeight baseHeight : baseHeights) {
        // System.err.println(i + " " + baseHeight.getHeight());

        if (baseHeight.getHeight() < DRAWING_HEIGHT_THRESHOLD) {
          continue;
        }

        x1 = axes.toPlotX1(x);
        y1 = axes.toPlotY1(y);

        char c = baseHeight.getChar();

        String s = String.valueOf(c);

        if (!mYScaleMap.get(c).containsKey(i)) {
          int letterHeight = ImageUtils.getFontHeight(g2, s);

          double hs1 = h1 / (double) letterHeight;

          double scaleY = hs1 * baseHeight.getHeight();

          mYScaleMap.get(c).put(i, scaleY);

          // System.err.println("hs1 " + hs1 + " " + h1 + " " + letterHeight);
          // System.err.println("scale " + scaleY + " " + baseHeight.getChar() +
          // " " +baseHeight.getHeight());

          double ws1 = (double) w1 / (double) g2.getFontMetrics().stringWidth(s);

          mXScaleMap.get(c).put(i, ws1);
        }

        double scaleX = mXScaleMap.get(c).get(i);
        double scaleY = mYScaleMap.get(c).get(i);

        switch (baseHeight.getChar()) {
        case 'A':
          g2.setColor(SequenceService.getInstance().getBaseAColor());
          break;
        case 'C':
          g2.setColor(SequenceService.getInstance().getBaseCColor());
          break;
        case 'G':
          g2.setColor(SequenceService.getInstance().getBaseGColor());
          break;
        case 'T':
          g2.setColor(SequenceService.getInstance().getBaseTColor());
          break;
        default:
          g2.setColor(SequenceService.getInstance().getBaseNColor());
          break;
        }

        Graphics2D g2Temp = ImageUtils.clone(g2);

        try {
          g2Temp.translate(x1, y1);
          g2Temp.scale(scaleX, scaleY);
          g2Temp.drawString(s, 0, 0);
        } finally {
          g2Temp.dispose();
        }

        y += baseHeight.getHeight();
      }

      ++x;
    }
  }

  /**
   * Adds the char.
   *
   * @param letter     the letter
   * @param baseCounts the base
   * @param sortMap    the sort map
   */
  protected static void addChar(char letter, BaseCounts baseCounts, Map<Double, List<Character>> sortMap) {

    double height = baseCounts.getCount(letter); // * r.get(i);

    // System.err.println(letter + " " + height);

    if (!sortMap.containsKey(height)) {
      sortMap.put(height, new ArrayList<Character>());
    }

    sortMap.get(height).add(letter);
  }
}
