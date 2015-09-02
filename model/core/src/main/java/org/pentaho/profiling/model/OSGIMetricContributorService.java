/*******************************************************************************
 *
 * Pentaho Data Profiling
 *
 * Copyright (C) 2002-2015 by Pentaho : http://www.pentaho.com
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package org.pentaho.profiling.model;

import org.pentaho.profiling.api.metrics.MetricContributor;
import org.pentaho.profiling.api.metrics.MetricManagerContributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The OSGIMetricContributorService provides a service where MetricContributor and MetricManagerContributors are added,
 * removed and listed. The objective is to control the addition and removal of metrics injected by bundle. The services
 * provided are thread safe.
 * <p>
 * This control is also needed for the same reason metrics need to be cloneable, so we maintain an object of the real
 * metric instead of a proxy object encapsulating the metric class that is not possible to convert to json.
 * 
 * Created by jpereira on 8/28/15
 * 
 * @author Joao L. M. Pereira (Joao.Pereira{[at]}pentaho.com)
 * @version 1.0
 */
public class OSGIMetricContributorService {
  private static final Logger LOGGER = LoggerFactory.getLogger( OSGIMetricContributorService.class );

  private Map<MetricContributor, MetricContributor> metricContributorsOSGIToClone;
  private Map<MetricManagerContributor, MetricManagerContributor> metricManagerContributorsOSGIToClone;

  /**
   * Constructs the OSGIMetricContributorService with empty lists of MetricContributor and MetricManagerContributor.
   */
  public OSGIMetricContributorService() {
    this.metricContributorsOSGIToClone = new HashMap<MetricContributor, MetricContributor>();
    this.metricManagerContributorsOSGIToClone = new HashMap<MetricManagerContributor, MetricManagerContributor>();
  }

  /**
   * Adds a new MetricContributor by cloning the object of MetricContributor received.
   * @param metricContributor MetricContributor to add
   */
  public void metricContributorAdded( MetricContributor metricContributor ) {
    MetricContributor metricContributorClone = metricContributor.clone();
    synchronized ( metricContributorsOSGIToClone ) {
      metricContributorsOSGIToClone.put( metricContributor, metricContributorClone );
    }
    LOGGER.info( "ADDED MetricContributor: " + metricContributor );
  }

  /**
   * Removes the MetricContributor from the service.
   * @param metricContributor MetricContributor to remove
   */
  public void metricContributorRemoved( MetricContributor metricContributor ) {
    synchronized ( metricContributorsOSGIToClone ) {
      metricContributorsOSGIToClone.remove( metricContributor );
    }
    LOGGER.info( "REMOVED MetricContributor: " + metricContributor );
  }

  /**
   * Returns a new list of MetricContributor instances provided by this service
   * @return a list of MetricContributor instances
   */
  public List<MetricContributor> getMetricContributorList() {
    List<MetricContributor> metricContributorList;
    synchronized ( metricContributorsOSGIToClone ) {
      metricContributorList = new ArrayList<MetricContributor>( metricContributorsOSGIToClone.values() );
    }
    return metricContributorList;
  }

  /**
   * Adds a new MetricContributor by cloning the object of MetricManagerContributor received.
   * @param metricManagerContributor MetricManagerContributor to add
   */
  public void metricManagerContributorAdded( MetricManagerContributor metricManagerContributor ) {
    MetricManagerContributor metricManagerContributorClone = metricManagerContributor.clone();
    synchronized ( metricManagerContributorsOSGIToClone ) {
      metricManagerContributorsOSGIToClone.put( metricManagerContributor, metricManagerContributorClone );
    }
    LOGGER.info( "ADDED MetricManagerContributor: " + metricManagerContributor );
  }

  /**
   * Removes the MetricManagerContributor from the service.
   * @param metricManagerContributor MetricManagerContributor to remove
   */
  public void metricManagerContributorRemoved( MetricManagerContributor metricManagerContributor ) {
    synchronized ( metricContributorsOSGIToClone ) {
      metricManagerContributorsOSGIToClone.remove( metricManagerContributor );
    }
    LOGGER.info( "REMOVED MetricManagerContributor: " + metricManagerContributor );
  }

  /**
   * Returns a new list of MetricManagerContributorList instances provided by this service
   * @return a list of MetricManagerContributorList instances
   */
  public List<MetricManagerContributor> getMetricManagerContributorList() {
    List<MetricManagerContributor> metricManagerContributorList;
    synchronized ( metricManagerContributorsOSGIToClone ) {
      metricManagerContributorList =
          new ArrayList<MetricManagerContributor>( metricManagerContributorsOSGIToClone.values() );
    }
    return metricManagerContributorList;
  }
}
