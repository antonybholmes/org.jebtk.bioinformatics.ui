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
package org.jebtk.bioinformatics.ui.icons;

import java.awt.Color;
import java.awt.Graphics2D;

import org.jebtk.core.Props;
import org.jebtk.modern.graphics.icons.ModernVectorIcon;
import org.jebtk.modern.theme.ModernTheme;
import org.jebtk.modern.theme.ThemeService;

/**
 * The class ModernDNA32VectorIcon.
 */
public class ModernDNA32VectorIcon extends ModernVectorIcon {

  /**
   * The color2.
   */
  private Color color2;

  /**
   * The color1.
   */
  private Color color1;

  /**
   * The constant SCALE.
   */
  private static final double SCALE = 0.9;

  /**
   * The constant LINE_SCALE.
   */
  private static final double LINE_SCALE = 0.8;

  /**
   * The constant OFFSET.
   */
  private static final double OFFSET = 0.1;

  /**
   * The constant OUTLINE.
   */
  private static final Color OUTLINE = ThemeService.getInstance().getColors().getGray(6);

  /**
   * Instantiates a new modern dn a32 vector icon.
   *
   * @param color1 the color1
   * @param color2 the color2
   */
  public ModernDNA32VectorIcon(Color color1, Color color2) {
    this.color1 = color1;
    this.color2 = color2;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.jebtk.ui.ui.icons.ModernIcon#drawForeground(java.awt.Graphics2D,
   * java.awt.Rectangle)
   */
  @Override
  public void drawIcon(Graphics2D g2, int x, int y, int w, int h, Props props) {
    double wf = w * SCALE;
    double w2 = w / 2.0;
    double lineWidth = w * LINE_SCALE;

    double o = w * OFFSET;

    double xf = x + (w - wf) / 2;
    double yf = y + (h - wf) / 2;

    g2.setColor(Color.WHITE);
    g2.fillRect((int) Math.round(xf), (int) Math.round(yf), (int) Math.round(wf), (int) Math.round(wf));

    g2.setColor(OUTLINE);
    g2.drawRect((int) Math.round(xf), (int) Math.round(yf), (int) Math.round(wf), (int) Math.round(wf));

    g2.setStroke(ModernTheme.DOUBLE_LINE_STROKE);

    xf = xf + (wf - lineWidth) / 2;

    g2.setColor(props.getColor("color1"));
    g2.drawLine((int) Math.round(xf), (int) Math.round(yf + w2 - o), (int) Math.round(xf + lineWidth),
        (int) Math.round(yf + w2 - o));

    g2.setColor(props.getColor("color2"));
    g2.drawLine((int) Math.round(xf), (int) Math.round(yf + w2 + o), (int) Math.round(xf + lineWidth),
        (int) Math.round(yf + w2 + o));
  }
}
