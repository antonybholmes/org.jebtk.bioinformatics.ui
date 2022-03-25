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

import org.jebtk.bioinformatics.motifs.Motif;
import org.jebtk.bioinformatics.motifs.MotifView;
import org.jebtk.core.text.TextUtils;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Axis;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.math.Linspace;

/**
 * Draw peaks.
 * 
 * @author Antony Holmes
 *
 */
public class SeqLogoCanvas extends SubFigure {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new seq logo canvas.
   */
  public SeqLogoCanvas() {
    // Do nothing
  }

  /**
   * Instantiates a new seq logo canvas.
   *
   * @param motif the motif
   */
  public SeqLogoCanvas(Motif motif) {
    this(motif, MotifView.P);
  }

  /**
   * Instantiates a new seq logo canvas.
   *
   * @param motif the motif
   * @param view  the view
   */
  public SeqLogoCanvas(Motif motif, MotifView view) {
    setMotif(motif, view);
  }

  /**
   * Sets the motif.
   *
   * @param motif the new motif
   */
  public void setMotif(Motif motif) {
    setMotif(motif, MotifView.P);
  }

  /**
   * Sets the motif.
   *
   * @param motif the motif
   * @param view  the view
   */
  public void setMotif(Motif motif, MotifView view) {
    if (motif == null) {
      return;
    }

    double min = 0;
    double max = motif.getBaseCount() + 1;

    Axes axes = currentAxes();

    Axis axis = axes.getX1Axis();

    // set the graph limits
    axis.setLimits(min, max);
    axis.getTicks().setTicks(Linspace.evenlySpaced(min + 1, max - 1, 1));
    axis.getGrid().setVisible(false);
    axis.getTicks().getMinorTicks().getLineStyle().setVisible(false);

    if (view == MotifView.BITS) {
      max = 2;
    } else {
      max = 1;
    }

    // Y axis

    axis = axes.getY1Axis();

    axis.setLimits(min, max, 1);

    if (view == MotifView.BITS) {
      axis.getTitle().setText("Bits");
    } else {
      axis.getTitle().setText(TextUtils.EMPTY_STRING);
    }

    axes.setInternalSize((motif.getBaseCount() + 1) * 50, 100);

    // axes.setMargins(50, 50, 50, 50);

    // Now lets create a plot

    Plot plot = axes.currentPlot();

    plot.clear();

    if (view == MotifView.BITS) {
      plot.addChild(new LettersPlotBitsLayer(motif));
    } else {
      plot.addChild(new LettersPlotPLayer(motif));
    }

    axes.setMargins(50);

    String title = motif.getName();

    if (!motif.getId().equals(title)) {
      title += " (" + motif.getId() + ")";
    }

    if (motif.getDatabase() != null) {
      title += " - " + motif.getDatabase();
    }

    axes.getTitle().setText(title);
  }
}
