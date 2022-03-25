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
package org.jebtk.bioinformatics.ui.external.ucsc;

import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.BedGraphElement;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.GenomicElement;
import org.jebtk.modern.table.ModernTableModel;

/**
 * Provides a view onto an excel workbook.
 * 
 * @author Antony Holmes
 *
 */
public class BedGraphTableModel extends ModernTableModel {

  /**
   * The constant HEADER.
   */
  private static final String[] HEADER = { "Chr", "Start", "End", "Value" };

  /**
   * The member features.
   */
  private List<GenomicElement> mFeatures;

  /**
   * Instantiates a new bed graph table model.
   *
   * @param bed the bed
   */
  public BedGraphTableModel(UCSCTrack bed) {
    mFeatures = bed.getElements();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.ui.ui.dataview.ModernDataModel#getColumn().getAnnotations(int)
   */
  @Override
  public String getColumnName(int column) {
    return HEADER[column]; // Integer.toString(row + 1);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.ui.ui.dataview.ModernDataModel#getColumnCount()
   */
  @Override
  public final int getColCount() {
    return HEADER.length;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.ui.ui.dataview.ModernDataModel#getRowCount()
   */
  @Override
  public final int getRowCount() {
    return mFeatures.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.ui.ui.dataview.ModernDataModel#getValueAt(int, int)
   */
  @Override
  public Object getValueAt(int row, int column) {
    switch (column) {
    case 0:
      return mFeatures.get(row).getChr().toString();
    case 1:
      return mFeatures.get(row).getStart();
    case 2:
      return mFeatures.get(row).getEnd();
    default:
      return ((BedGraphElement) mFeatures.get(row)).getValue();
    }
  }
}
