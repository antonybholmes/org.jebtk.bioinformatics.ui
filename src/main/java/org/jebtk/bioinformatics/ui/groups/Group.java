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
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.xml.XmlStream;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Represents a group of regions or gene symbols that a user wants to search for
 * motifs in.
 * 
 * @author Antony Holmes
 *
 */
public class Group implements Iterable<String>, XmlStream {

  /**
   * The member entries.
   */
  private List<String> mEntries = new ArrayList<String>();

  /**
   * The member name.
   */
  private String mName;

  /**
   * The member color.
   */
  private Color mColor;

  /**
   * Instantiates a new group.
   *
   * @param name  the name
   * @param color the color
   */
  public Group(String name, Color color) {
    mName = name;
    mColor = color;
  }

  /**
   * Clone a group.
   *
   * @param group the group
   */
  public Group(Group group) {
    copy(group);
  }

  /**
   * Copy the Props of one group to another.
   *
   * @param group the group
   */
  public void copy(Group group) {
    setName(group.mName);
    setColor(group.mColor);
    setEntries(group.mEntries);
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return mName;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    mName = name;
  }

  /**
   * Gets the color.
   *
   * @return the color
   */
  public Color getColor() {
    return mColor;
  }

  /**
   * Sets the color.
   *
   * @param color the new color
   */
  public void setColor(Color color) {
    mColor = color;
  }

  /**
   * Size.
   *
   * @return the int
   */
  public int size() {
    return mEntries.size();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<String> iterator() {
    return mEntries.iterator();
  }

  /**
   * Sets the entries.
   *
   * @param entries the new entries
   */
  public void setEntries(List<String> entries) {
    mEntries.clear();
    mEntries.addAll(entries);
  }

  /**
   * Adds the.
   *
   * @param entry the entry
   */
  public void add(String entry) {
    mEntries.add(entry);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.xml.XmlStream#toXml(org.w3c.dom.Document,
   * org.w3c.dom.Element)
   */
  @Override
  public void toXml(Document doc, Element parent) throws DOMException {
    Element element = doc.createElement("region-group");
    parent.appendChild(element);

    element.setAttribute("name", getName());
    element.setAttribute("color", ColorUtils.toHtml(getColor()));

    Element regionsElement = doc.createElement("regions");
    element.appendChild(regionsElement);

    for (String region : this) {
      Element regionElement = doc.createElement("region");
      regionsElement.appendChild(regionElement);

      regionElement.setAttribute("id", region);
    }
  }

  /**
   * Load groups.
   *
   * @param file the file
   * @return the list
   * @throws SAXException                 the SAX exception
   * @throws IOException                  Signals that an I/O exception has
   *                                      occurred.
   * @throws ParserConfigurationException the parser configuration exception
   */
  public static List<Group> loadGroups(Path file) throws SAXException, IOException, ParserConfigurationException {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser saxParser = factory.newSAXParser();

    GroupsXmlHandler handler = new GroupsXmlHandler();

    saxParser.parse(file.toFile(), handler);

    return handler.getGroups();
  }
}
