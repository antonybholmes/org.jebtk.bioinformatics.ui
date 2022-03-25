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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.jebtk.bioinformatics.BaseCounts;
import org.jebtk.bioinformatics.motifs.Motif;
import org.jebtk.bioinformatics.motifs.MotifDataSource;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.http.URLPath;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.path.Path;
import org.jebtk.core.path.RootPath;
import org.jebtk.core.search.SearchStackElement;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.tree.TreeNode;

/**
 * The class MotifsWeb.
 */
public class MotifsWeb extends MotifDataSource {

  /**
   * The constant BASE_DIR.
   */
  private static final Path BASE_DIR = new RootPath();

  /**
   * The member url.
   */
  private URLPath mUrl;

  /**
   * The member paths url.
   */
  private URLPath mPathsUrl;

  /**
   * The member motifs url.
   */
  private URLPath mMotifsUrl;

  /**
   * The member parser.
   */
  private JsonParser mParser;

  /**
   * Instantiates a new motifs web.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public MotifsWeb() throws IOException {
    this(SettingsService.getInstance().getSetting("motifs.motifsdb.remote-url").getUrl());
  }

  /**
   * Instantiates a new motifs web.
   *
   * @param url the url
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public MotifsWeb(URL url) throws IOException {
    mUrl = URLPath.fromUrl(url);
    mPathsUrl = mUrl.join("paths");
    mMotifsUrl = mUrl.join("motifs");

    mParser = new JsonParser();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.lib.bioinformatics.motifs.MotifsDB#createTree(org.abh.lib.
   * tree.TreeRootNode, java.lang.String)
   */
  @Override
  public void createTree(TreeNode<Motif> root, List<String> terms, boolean inList, boolean exactMatch,
      boolean caseSensitive) throws IOException, ParseException {
    // TreeRootNode<Motif> root = new TreeRootNode<Motif>();

    createTreeDir(BASE_DIR, root, terms);

    // return root;
  }

  /**
   * Creates the tree dir.
   *
   * @param root     the root
   * @param rootNode the root node
   * @param terms    the terms
   * @return true, if successful
   * @throws IOException    Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private boolean createTreeDir(Path root, TreeNode<Motif> rootNode, List<String> terms)
      throws IOException, ParseException {
    /*
     * for (Path path : getPaths(root)) { TreeNode<Motif> node = new
     * TreeNode<Motif>(path.getName());
     * 
     * node.setIsParent(true); rootNode.addChild(node);
     * 
     * createTreeDir(path, node, terms);
     * 
     * 
     * }
     * 
     * Deque<SearchStackElement<Motif>> searchStack =
     * SearchStackElement.parseQuery(search);
     * 
     * List<Motif> motifs = search(getMotifs(root), searchStack);
     * 
     * for (Motif motif : motifs) { rootNode.addChild(new
     * TreeNode<Motif>(motif.getName() + " (" + motif.getId() + ")", motif)); }
     * 
     * return motifs.size() > 0;
     */

    return false;
  }

  /**
   * Search.
   *
   * @param motifs      the motifs
   * @param searchStack the search stack
   * @return the list
   */
  public static List<Motif> search(List<Motif> motifs, Deque<SearchStackElement> searchStack) {

    if (searchStack.size() == 0) {
      return motifs;
    }

    // int categoryId = getAllCategoriesId(connection);

    SearchStackElement op = null;
    SearchStackElement newOp;
    List<Motif> op1;
    List<Motif> op2;

    Deque<List<Motif>> resultStack = new ArrayDeque<List<Motif>>();

    List<Motif> sampleIds;

    while (searchStack.size() > 0) {
      op = searchStack.pop();

      switch (op.mOp) {
      case MATCH:
        String s = op.mText.toLowerCase();

        sampleIds = new ArrayList<Motif>();

        for (Motif motif : motifs) {
          if (motif.getName().toLowerCase().contains(s) || motif.getId().toLowerCase().contains(s)
              || motif.getGene().toLowerCase().contains(s)) {
            sampleIds.add(motif);
          }
        }

        resultStack.push(sampleIds);

        break;
      case AND:
        op2 = resultStack.pop();
        op1 = resultStack.pop();

        sampleIds = CollectionUtils.intersect(op1, op2);

        resultStack.push(sampleIds);

        break;
      case OR:
        op2 = resultStack.pop();
        op1 = resultStack.pop();

        sampleIds = CollectionUtils.union(op1, op2);

        resultStack.push(sampleIds);

        break;
      case XOR:
      case NAND:
        op2 = resultStack.pop();
        op1 = resultStack.pop();

        sampleIds = CollectionUtils.xor(op1, op2);

        resultStack.push(sampleIds);

        break;
      default:
        break;
      }
    }

    // The final result is on the top of result stack
    return resultStack.pop(); // ArrayUtils.sort(sampleIds);
  }

  /**
   * Gets the paths.
   *
   * @param root the root
   * @return the paths
   * @throws IOException    Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  public List<Path> getPaths(Path root) throws IOException, ParseException {
    List<Path> paths = new ArrayList<Path>();

    try {

      URL url = mPathsUrl.param("p", root.toString()).toURL();

      System.err.println(url);

      Json json = mParser.parse(url);

      for (int i = 0; i < json.size(); ++i) {
        Json pathJson = json.get(i);

        Path path = new Path(pathJson.get("path").getString());

        paths.add(path);
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return paths;
  }

  /**
   * Gets the motifs.
   *
   * @param path the path
   * @return the motifs
   * @throws IOException    Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  public List<Motif> getMotifs(Path path) throws IOException, ParseException {
    List<Motif> motifs = new ArrayList<Motif>();

    try {
      URLPath url = mMotifsUrl.param("p", path.toString());

      System.err.println(url);

      Json json = mParser.parse(url);

      for (int i = 0; i < json.size(); ++i) {
        Json motifJson = json.get(i);

        Json countsJson = motifJson.get("counts");

        // System.err.println(countsJson.toString());

        int l = countsJson.size();

        List<BaseCounts> counts = new ArrayList<BaseCounts>(l);

        for (int j = 0; j < l; ++j) {
          Json countJson = countsJson.get(j);
          // Convert the values of each column to percentages

          double af = countJson.get("a").getDouble();
          double cf = countJson.get("c").getDouble();
          double gf = countJson.get("g").getDouble();
          double tf = countJson.get("t").getDouble();

          counts.add(new BaseCounts(af, cf, gf, tf, true));
        }

        Motif motif = new Motif(motifJson.get("id").getString(), motifJson.get("name").getString(),
            motifJson.get("gene").getString(), motifJson.get("database").getString(), counts);

        motifs.add(motif);
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return motifs;
  }
}
