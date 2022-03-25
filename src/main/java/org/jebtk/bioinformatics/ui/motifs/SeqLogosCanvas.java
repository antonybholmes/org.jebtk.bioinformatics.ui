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
import java.util.List;

import org.jebtk.bioinformatics.motifs.Motif;
import org.jebtk.bioinformatics.motifs.MotifView;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.plotbox.PlotBoxRowLayout;

/**
 * Layout out peak plots in a column.
 * 
 * @author Antony Holmes
 *
 */
public class SeqLogosCanvas extends Figure {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The maximum number of logos that can be displayed at once. */
  private static final int MAX_PLOTS = 100;

  /**
   * The member motifs.
   */
  private List<Motif> mMotifs;

  /**
   * The member view.
   */
  private MotifView mView = MotifView.BITS;

  /**
   * Sets the view.
   *
   * @param view the new view
   */
  public void setView(MotifView view) {
    mView = view;

    fireChanged();
  }

  public SeqLogosCanvas() {
    super("Seq Logos Figure", new PlotBoxRowLayout());
  }

  /**
   * Sets the motifs.
   *
   * @param motifs the motifs
   * @param view   the view
   */
  public void setMotifs(List<Motif> motifs, MotifView view) {
    mMotifs = motifs;
    mView = view;

    // If the genomic model changes, create new plots
    // mGenomicModel.addChangeListener(new GenomicEvents());

    init();
  }

  /**
   * Inits the.
   */
  private void init() {
    if (mMotifs == null) {
      return;
    }

    List<SubFigure> plots = new ArrayList<SubFigure>(mMotifs.size());

    // Add the plots to this canvas
    for (int i = 0; i < Math.min(mMotifs.size(), MAX_PLOTS); ++i) {
      // create a new plot

      SeqLogoCanvas plot = new SeqLogoCanvas(mMotifs.get(i), mView);

      plots.add(plot);

    }

    setChildren(plots);
  }
}
