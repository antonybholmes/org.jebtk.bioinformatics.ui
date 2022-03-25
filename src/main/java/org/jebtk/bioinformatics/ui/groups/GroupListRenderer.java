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
package org.jebtk.bioinformatics.ui.groups;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;

import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.list.ModernList;
import org.jebtk.modern.list.ModernListCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * Renders a file as a list item.
 * 
 * @author Antony Holmes
 *
 */
public class GroupListRenderer extends ModernListCellRenderer {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant ORB_SIZE.
   */
  private static final int ORB_SIZE = 10;

  /**
   * The member text.
   */
  private String mText = "";

  /**
   * The member color.
   */
  private Color mColor = Color.RED;

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.ui.ui.ModernWidget#drawForegroundAA(java.awt.Graphics2D)
   */
  @Override
  public void drawForegroundAA(Graphics2D g2) {
    int x = DOUBLE_PADDING;
    int y = getHeight() / 2;

    g2.setColor(TEXT_COLOR);
    g2.drawString(mText, x, getTextYPosCenter(g2, getHeight()));

    g2.setColor(mColor);

    x = getWidth() - ORB_SIZE - DOUBLE_PADDING;

    g2.drawLine(x, y, x + ORB_SIZE, y);

    y -= 2;

    g2.drawLine(x, y, x + ORB_SIZE, y);

    y += 4;

    g2.drawLine(x, y, x + ORB_SIZE, y);

    // g2.setColor(ModernWidget.DARK_LINE_COLOR);
    // g2.drawRect(iconX, iconY, UIResources.ICON_SIZE_16,
    // UIResources.ICON_SIZE_16);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.jebtk.ui.ui.list.ModernListCellRenderer#getCellRendererComponent(org.
   * jebtk.ui.ui.list.ModernList, java.lang.Object, boolean, boolean, boolean,
   * int)
   */
  @Override
  public Component getCellRendererComponent(ModernList<?> list, Object value, boolean highlight, boolean isSelected,
      boolean hasFocus, int row) {

    Group g = (Group) value;

    mText = TextUtils.truncate(g.getName(), 30) + " (" + g.size() + ")";
    mColor = g.getColor();

    return super.getCellRendererComponent(list, value, highlight, isSelected, hasFocus, row);
  }
}