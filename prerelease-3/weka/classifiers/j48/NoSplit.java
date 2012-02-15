/*
 *    NoSplit.java
 *    Copyright (C) 1999 Eibe Frank
 *
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package weka.classifiers.j48;

import weka.core.*;

/**
 * Class implementing a "no-split"-split.
 *
 * @author Eibe Frank (eibe@cs.waikato.ac.nz)
 * @version $Revision: 1.2 $
 */
public final class NoSplit extends ClassifierSplitModel{

  /**
   * Creates "no-split"-split for given distribution.
   */
  public NoSplit(Distribution distribution){
    
    m_distribution = new Distribution(distribution);
    m_numSubsets = 1;
  }
  
  /**
   * Creates a "no-split"-split for a given set of instances.
   *
   * @exception Exception if split can't be built successfully
   */
  public final void buildClassifier(Instances instances) 
       throws Exception {

    m_distribution = new Distribution(instances);
    m_numSubsets = 1;
  }

  /**
   * Always returns 0 because only there is only one subset.
   */
  public final int whichSubset(Instance instance){
    
    return 0;
  }

  /**
   * Always returns null because there is only one subset.
   */
  public final double [] weights(Instance instance){

    return null;
  }
  
  /**
   * Does nothing because no condition has to be satisfied.
   */
  public final String leftSide(Instances instances){

    return "";
  }
  
  /**
   * Does nothing because no condition has to be satisfied.
   */
  public final String rightSide(int index, Instances instances){

    return "";
  }
}







