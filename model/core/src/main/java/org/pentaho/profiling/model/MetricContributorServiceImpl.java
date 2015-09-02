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

import org.pentaho.profiling.api.json.ObjectMapperFactory;
import org.pentaho.profiling.api.metrics.MetricContributorService;
import org.pentaho.profiling.api.metrics.MetricContributors;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bryan on 3/11/15.
 * Modified by jpereira on 8/28/15.
 */
public class MetricContributorServiceImpl implements MetricContributorService {
  public static final String ETC_METRIC_CONTRIBUTORS_JSON = "etc/metricContributors.json";
  private static final Logger LOGGER = LoggerFactory.getLogger( MetricContributorServiceImpl.class );

  private final String jsonFile;
  private final ObjectMapper objectMapper;
  private final OSGIMetricContributorService oSGIMetricContributorService;

  public MetricContributorServiceImpl( OSGIMetricContributorService oSGIMetricContributorService,
      ObjectMapperFactory objectMapperFactory ) {
    this( oSGIMetricContributorService, objectMapperFactory, System.getProperty( "karaf.home" ) );
  }

  public MetricContributorServiceImpl( OSGIMetricContributorService oSGIMetricContributorService,
      ObjectMapperFactory objectMapperFactory, String karafHome ) {
    this.objectMapper = objectMapperFactory.createMapper();
    this.oSGIMetricContributorService = oSGIMetricContributorService;
    this.jsonFile = findJsonFile( karafHome );
  }

  public static final String findJsonFile( String karafHome ) {
    String jsonFile;
    if ( karafHome != null ) {
      jsonFile =
        FileSystems.getDefault().getPath( karafHome ).resolve( ETC_METRIC_CONTRIBUTORS_JSON ).normalize().toFile()
          .getAbsolutePath();
    } else {
      jsonFile = null;
    }
    return jsonFile;
  }

  public String getJsonFile() {
    return jsonFile;
  }

  private MetricContributors getFullMetricContributors() {
    return new MetricContributors( oSGIMetricContributorService.getMetricContributorList(),
        oSGIMetricContributorService.getMetricManagerContributorList() );
  }

  @Override public synchronized Map<String, MetricContributors> getAllConfigurations() {
    // First try to read from file
    File metricContributorsJson = null;
    if ( jsonFile != null ) {
      metricContributorsJson = new File( jsonFile );
    }
    MetricContributors fullMetricContributors = getFullMetricContributors();
    if ( metricContributorsJson.exists() ) {
      FileInputStream fileInputStream = null;
      try {
        fileInputStream = new FileInputStream( metricContributorsJson );
        Map<String, MetricContributors> metricContributorMap = objectMapper.readValue( fileInputStream, Map.class );
        metricContributorMap.put( MetricContributorService.FULL_CONFIGURATION, fullMetricContributors );
        return metricContributorMap;
      } catch ( Exception e ) {
        LOGGER.error( "Unable to read saved metric contributor json, falling back to default", e );
      } finally {
        if ( fileInputStream != null ) {
          try {
            fileInputStream.close();
          } catch ( IOException e ) {
            //Ignore
          }
        }
      }
    }
    Map<String, MetricContributors> result = new HashMap<String, MetricContributors>();
    result.put( MetricContributorService.FULL_CONFIGURATION, fullMetricContributors );
    result.put( MetricContributorService.DEFAULT_CONFIGURATION, fullMetricContributors );
    return result;
  }

  @Override public synchronized MetricContributors getDefaultMetricContributors( String configuration ) {
    Map<String, MetricContributors> metricContributorMap = getAllConfigurations();
    MetricContributors metricContributors = metricContributorMap.get( configuration );
    if ( metricContributors == null ) {
      metricContributors = metricContributorMap.get( MetricContributorService.DEFAULT_CONFIGURATION );
    }
    return metricContributors;
  }

  @Override
  public synchronized void setDefaultMetricContributors( String configuration, MetricContributors metricContributors ) {
    Map<String, MetricContributors> metricContributorMap = getAllConfigurations();
    metricContributorMap.put( configuration, metricContributors );
    metricContributorMap.remove( MetricContributorService.FULL_CONFIGURATION );
    File metricContributorsJson = null;
    if ( jsonFile != null ) {
      metricContributorsJson = new File( jsonFile );
      File parentFile = metricContributorsJson.getParentFile();
      if ( parentFile.exists() ) {
        FileOutputStream fileOutputStream = null;
        try {
          fileOutputStream = new FileOutputStream( metricContributorsJson );
          objectMapper.writerWithDefaultPrettyPrinter().writeValue( fileOutputStream, metricContributorMap );
        } catch ( Exception e ) {
          LOGGER.error( "Error while persisting metric contributor defaults", e );
        } finally {
          if ( fileOutputStream != null ) {
            try {
              fileOutputStream.close();
            } catch ( IOException e ) {
              // Ignore
            }
          }
        }
      } else {
        LOGGER.warn( "Etc folder: " + parentFile + " doesn't exist, not persisting metric contributor defaults" );
      }
    }
  }
}
