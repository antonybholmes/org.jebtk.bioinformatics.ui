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
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;

import org.jebtk.bioinformatics.ext.ucsc.Cytobands;
import org.jebtk.bioinformatics.genomic.ChromosomeService;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.Mathematics;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Layout out peak plots in a column.
 * 
 * @author Antony Holmes
 *
 */
public class CytobandsRegionLayer extends CytobandsLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant DEFAULT_REGION. */
  private static final GenomicRegion DEFAULT_REGION = new GenomicRegion(
      ChromosomeService.getInstance().chr(Genome.HG19, "chr1"), 1, 1000);

  /** The Constant REGION_COLOR. */
  private static final Color REGION_COLOR = Color.RED;

  /** The Constant REGION_FILL_COLOR. */
  private static final Color REGION_FILL_COLOR = ColorUtils.getTransparentColor75(REGION_COLOR);

  /** The Constant TRIANGLE_SIZE. */
  private static final int TRIANGLE_SIZE = 5;

  /** The m triangle. */
  private GeneralPath mTriangle;

  /**
   * Instantiates a new cytobands region layer.
   *
   * @param cytobands the cytobands
   */
  public CytobandsRegionLayer(Cytobands cytobands) {
    super(cytobands);

    mTriangle = new GeneralPath();
    mTriangle.moveTo(0, 0);
    mTriangle.lineTo(-TRIANGLE_SIZE, TRIANGLE_SIZE);
    mTriangle.lineTo(TRIANGLE_SIZE, TRIANGLE_SIZE);
    mTriangle.closePath();

    setRegion(Genome.HG19, DEFAULT_REGION);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jebtk.bioinformatics.ui.external.ucsc.CytobandsLayer#plotLayer(java.awt
   * .Graphics2D, org.jebtk.ui.graphics.DrawingContext,
   * org.graphplot.figure.SubFigure, org.graphplot.figure.Axes)
   */
  @Override
  public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {
    super.plotLayer(g2, context, figure, subFigure, axes);

    int minX = axes.toPlotX1(axes.getX1Axis().getLimits().getMin());
    int maxX = axes.toPlotX1(axes.getX1Axis().getLimits().getMax()) - 1;

    int y1 = axes.toPlotY1(axes.getY1Axis().getLimits().getMin()) - 1;
    int y2 = axes.toPlotY1(axes.getY1Axis().getLimits().getMax()) + 1;
    int h = y1 - y2;

    //
    // Display region
    //

    // g2.drawString(id, textX - g2.getFontMetrics().stringWidth(id), textY);

    int x1 = Mathematics.bound(axes.toPlotX1(mDisplayRegion.getStart()), minX, maxX);
    int x2 = Mathematics.bound(axes.toPlotX1(mDisplayRegion.getEnd()), minX, maxX);

    // px1 = Mathematics.bound(x1, minX, maxX);
    // px2 = Mathematics.bound(x2, minX, maxX);

    int w = Math.max(3, x2 - x1);

    g2.setColor(REGION_FILL_COLOR);
    g2.fillRect(x1, y2, w, h);

    g2.setColor(REGION_COLOR);
    g2.drawRect(x1, y2, w, h);

    /*
     * // Triangles
     * 
     * int y = y1 - TRIANGLE_SIZE;
     * 
     * drawTriangle(g2, x1, y); drawTriangle(g2, Math.max(x1 + w, x2), y);
     */
  }

  /**
   * Draw triangle.
   *
   * @param g2 the g 2
   * @param x  the x
   * @param y  the y
   */
  private void drawTriangle(Graphics2D g2, int x, int y) {
    Graphics2D g2Temp = (Graphics2D) g2.create();

    g2Temp.translate(x, y);

    g2Temp.fill(mTriangle);

    g2Temp.dispose();
  }
}
