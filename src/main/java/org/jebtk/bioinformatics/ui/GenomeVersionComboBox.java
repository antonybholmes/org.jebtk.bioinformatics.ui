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

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.modern.UI;
import org.jebtk.modern.combobox.ModernComboBox;

/**
 * The class GenomeVersionComboBox.
 */
public class GenomeVersionComboBox extends ModernComboBox {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /** The Constant SIZE. */
  private static final Dimension SIZE = new Dimension(150, WIDGET_HEIGHT);

  /**
   * Instantiates a new genome version combo box.
   *
   * @param defaultGenome the default genome
   */
  public GenomeVersionComboBox(String defaultGenome) {
    // ComboPopupMenu popup = new ComboPopupMenu(10);
    // popup.setWidth(200);
    // setPopup(popup);

    addMenuItem("HG18");
    addMenuItem("HG19");

    // addBreakLine();

    // addModernMenuItem(new ForwardRequestMenuItem("Other...", null));

    if (defaultGenome.equals(Genome.HG19)) {
      setSelectedIndex(1);
    } else {
      setSelectedIndex(0);
    }

    UI.setSize(this, SIZE);
  }
}
