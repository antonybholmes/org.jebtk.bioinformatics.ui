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

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.ribbon.RibbonStripContainer;

/**
 * Standardized ribbon menu section for genome.
 *
 * @author Antony Holmes
 *
 */
public class GenomeRibbonSection extends RibbonSection implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant MESSAGE_QUEUE.
   */
  public static final String MESSAGE_QUEUE = "genome";

  // private AbstractButton hg18Button = new
  // FlatModernCheckButton(Genome.HG18.toString(),
  // Resources.getInstance().loadIcon("hg18", Resources.ICON_SIZE_16));

  // private AbstractButton hg19Button = new
  // FlatModernCheckButton(Genome.HG19.toString(),
  // Resources.getInstance().loadIcon("hg19", Resources.ICON_SIZE_16));

  /**
   * The member hg18 button.
   */
  private ModernRadioButton mHg18Button = new ModernRadioButton(Genome.HG18.getAssembly());

  /**
   * The member hg19 button.
   */
  private ModernRadioButton mHg19Button = new ModernRadioButton(Genome.HG19.getAssembly());

  /**
   * Instantiates a new genome ribbon section.
   *
   * @param ribbon the ribbon
   * @param genome the default genome
   */
  public GenomeRibbonSection(Ribbon ribbon, Genome genome) {
    super(ribbon, "Genome");

    ModernButtonGroup group = new ModernButtonGroup();

    group.add(mHg18Button);
    group.add(mHg19Button);

    if (genome.equals(Genome.HG19)) {
      mHg19Button.setSelected(true);
    } else {
      mHg18Button.setSelected(true);
    }

    mHg18Button.setToolTip(Genome.HG18.getAssembly(),
        "Use the " + Genome.HG18.getAssembly() + " genome as a reference for coordinates.");
    mHg19Button.setToolTip(Genome.HG19.getAssembly(),
        "Use the " + Genome.HG19.getAssembly() + " genome as a reference for coordinates.");

    RibbonStripContainer c = new RibbonStripContainer();
    c.add(mHg18Button);
    c.add(mHg19Button);
    add(c);

    mHg18Button.addClickListener(this);
    mHg19Button.addClickListener(this);
  }

  /**
   * Gets the genome.
   *
   * @return the genome
   */
  public final Genome getGenome() {
    if (mHg19Button.isSelected()) {
      return Genome.HG19;
    } else {
      return Genome.HG18;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.ui.ui.event.ModernClickListener#clicked(org.jebtk.ui.ui.event.
   * ModernClickEvent)
   */
  public void clicked(ModernClickEvent e) {
    fireClicked(new ModernClickEvent(this, "genome_changed"));
  }
}
