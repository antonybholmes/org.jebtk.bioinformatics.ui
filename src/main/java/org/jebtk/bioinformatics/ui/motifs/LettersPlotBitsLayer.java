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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.BaseCounts;
import org.jebtk.bioinformatics.motifs.BaseHeight;
import org.jebtk.bioinformatics.motifs.BaseHeights;
import org.jebtk.bioinformatics.motifs.Motif;
import org.jebtk.bioinformatics.motifs.MotifHeights;
import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.CollectionUtils;

/**
 * Draws scales motif letters in bits.
 * 
 * @author Antony Holmes
 *
 */
public class LettersPlotBitsLayer extends LettersPlotLayer {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new letters plot bits layer.
   *
   * @param motif the motif
   */
  public LettersPlotBitsLayer(Motif motif) {
    List<BaseCounts> counts = new ArrayList<BaseCounts>();

    double error = 1.0 / Math.log(2) * (4 - 1) / (2 * motif.getBaseCount());

    for (BaseCounts bc : motif) {
      double sum = bc.getSum();

      double a = bc.getA() / sum;

      double c = bc.getC() / sum;

      double g = bc.getG() / sum;

      double t = bc.getT() / sum;

      double n = bc.getN() / sum;

      double information = 2 - (-a * Mathematics.log2(a) + -c * Mathematics.log2(c) + -g * Mathematics.log2(g)
          + -t * Mathematics.log2(t) + -n * Mathematics.log2(n));

      counts.add(
          new BaseCounts(a * information, c * information, g * information, t * information, n * information, false));
    }

    Motif bitsMotif = new Motif(motif.getName(), counts);

    // Now we have each base as a pvalue, calculate information

    /*
     * List<Double> h = new ArrayList<Double>();
     * 
     * for (BaseCounts base : pMotif) { double bits = base.getA() *
     * Mathematics.log2(base.getA()) + base.getC() * Mathematics.log2(base.getC()) +
     * base.getG() * Mathematics.log2(base.getG()) + base.getT() *
     * Mathematics.log2(base.getT());
     * 
     * h.add(-bits); }
     */

    mMotifHeights = new MotifHeights(bitsMotif.getName());

    for (int i = 0; i < bitsMotif.getBaseCount(); ++i) {
      BaseCounts base = bitsMotif.getCounts(i);

      BaseHeights baseHeights = new BaseHeights();

      Map<Double, List<Character>> sortMap = new HashMap<Double, List<Character>>();

      addChar('A', base, sortMap);
      addChar('C', base, sortMap);
      addChar('G', base, sortMap);
      addChar('T', base, sortMap);
      addChar('N', base, sortMap);

      // Sort by height, then by letters with the same height
      // Letters are reverse sorted to ensure that they are
      // display alphabetically vertically down on the plot
      for (double height : CollectionUtils.sort(sortMap.keySet())) {
        for (char c : CollectionUtils.reverse(CollectionUtils.sort(sortMap.get(height)))) {
          baseHeights.add(new BaseHeight(c, height));
        }
      }

      mMotifHeights.add(baseHeights);
    }

  }
}
