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
import org.jebtk.core.collections.CollectionUtils;

/**
 * Draws motif bases where the height of each letter is proportional to its
 * probability.
 * 
 * @author Antony Holmes
 *
 */
public class LettersPlotPLayer extends LettersPlotLayer {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new letters plot p layer.
   *
   * @param motif the motif
   */
  public LettersPlotPLayer(Motif motif) {
    List<BaseCounts> counts = new ArrayList<BaseCounts>();

    // Normalize the matrix
    for (BaseCounts base : motif) {
      counts.add(new BaseCounts(base.getA(), base.getC(), base.getG(), base.getT(), base.getN(), true));
    }

    Motif pMotif = new Motif(motif.getName(), counts);

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

    mMotifHeights = new MotifHeights(pMotif.getName());

    for (int i = 0; i < pMotif.getBaseCount(); ++i) {
      BaseCounts baseCounts = pMotif.getCounts(i);

      BaseHeights baseHeights = new BaseHeights();

      Map<Double, List<Character>> sortMap = new HashMap<Double, List<Character>>();

      addChar('A', baseCounts, sortMap);
      addChar('C', baseCounts, sortMap);
      addChar('G', baseCounts, sortMap);
      addChar('T', baseCounts, sortMap);
      addChar('N', baseCounts, sortMap);

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
