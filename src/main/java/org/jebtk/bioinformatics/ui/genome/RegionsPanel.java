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
package org.jebtk.bioinformatics.ui.genome;

import java.text.ParseException;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.modern.ModernWidget;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;

/**
 * Stores an ad-hoc list of regions.
 * 
 * @author Antony Holmes
 *
 */
public class RegionsPanel extends ModernWidget {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member regions field.
   */
  private RegionsTextArea mRegionsField = new RegionsTextArea();

  /**
   * Instantiates a new regions panel.
   */
  public RegionsPanel() {
    ModernScrollPane scrollPane = new ModernScrollPane(mRegionsField);
    scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
    scrollPane.setVerticalScrollBarPolicy(ScrollBarPolicy.ALWAYS);
    // Ui.setSize(scrollPane, new Dimension(150, 400));
    // box.add(new ModernDialogContentPanel(scrollPane));
    // box.setMinimumSize(new Dimension(150, 0));

    add(scrollPane);

    // setBorder(BORDER);
  }

  /**
   * Gets the regions.
   *
   * @return the regions
   * @throws ParseException the parse exception
   */
  public List<GenomicRegion> getRegions(Genome genome) {
    return mRegionsField.getRegions(genome);
  }

  /**
   * Sets the regions.
   *
   * @param regions the new regions
   */
  public void setRegions(List<GenomicRegion> regions) {
    mRegionsField.setRegions(regions);
  }

  /**
   * Sets the regions.
   *
   * @param track the new regions
   */
  public void setRegions(UCSCTrack track) {
    mRegionsField.setRegions(track);
  }
}
