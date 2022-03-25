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

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.io.Io;
import org.jebtk.core.io.PathUtils;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.matrix.CsvMatrixParser;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.MatrixType;
import org.jebtk.math.matrix.MixedMatrixParser;
import org.jebtk.math.matrix.TextMatrixParser;
import org.jebtk.math.ui.external.microsoft.XlsTableModel;
import org.jebtk.math.ui.external.microsoft.XlsxTableModel;
import org.jebtk.math.ui.matrix.EditableMatrixTableModel;
import org.jebtk.modern.dataview.ModernDataModel;

/**
 * The class Bioinformatics.
 */
public class Bioinformatics {

  /**
   * Gets the model.
   *
   * @param file           the file
   * @param hasHeader      the has header
   * @param skipMatches    the skip matches
   * @param rowAnnotations the row annotations
   * @param delimiter      the delimiter
   * @return the model
   * @throws InvalidFormatException the invalid format exception
   * @throws IOException            Signals that an I/O exception has occurred.
   */
  public static ModernDataModel getModel(Path file, int headers, List<String> skipMatches, int rowAnnotations,
      String delimiter) throws InvalidFormatException, IOException {
    return getModel(file, headers, skipMatches, rowAnnotations, delimiter, MatrixType.MIXED);
  }

  /**
   * Gets the model.
   *
   * @param file        Whether the file has a header.
   * @param hasHeader   The has header
   * @param skipMatches The skip matches
   * @param rowAnns     The row annotations
   * @param delimiter   the delimiter
   * @param type        The annotation type to indicate to the parser how to
   *                    handle numbers (i.e. whether to attempt to parse text as a
   *                    primitive number or not).
   * @return the model
   * @throws InvalidFormatException the invalid format exception
   * @throws IOException            Signals that an I/O exception has occurred.
   */
  public static ModernDataModel getModel(Path file, int headers, List<String> skipMatches, int rowAnns,
      String delimiter, MatrixType type) throws InvalidFormatException, IOException {
    if (file == null) {
      return null;
    }

    String ext = PathUtils.getFileExt(file);

    if (ext.equals(Excel.XLS_EXTENSION)) {
      return new XlsTableModel(file, headers > 0, rowAnns, true);
    } else if (ext.equals(Excel.XLSX_EXTENSION)) {
      return new XlsxTableModel(file, headers > 0, rowAnns, true);
    } else if (ext.equals(Io.FILE_EXT_CSV)) {
      return new EditableMatrixTableModel(new CsvMatrixParser(headers > 0, rowAnns).parse(file));
    } else {
      DataFrame matrix;

      if (type == MatrixType.TEXT) {
        matrix = new TextMatrixParser(headers, skipMatches, rowAnns, delimiter).parse(file);
      } else {
        matrix = new MixedMatrixParser(headers, skipMatches, rowAnns, delimiter).parse(file);
      }

      return new EditableMatrixTableModel(matrix);
    }
  }
}
