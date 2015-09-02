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

package org.pentaho.profiling.api.osgi;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.karaf.kar.KarService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * ClassLoader for objects existing in Bundles, it is used to deserialize objects which class is not present in the
 * bundle that deserialize the object.
 * <p>
 * Additionally it can deserialize from hot-deploy bundles through kar files. To use this feature, pass the KarService
 * to the constructor of the BundleListClassloader, and add the kar files to folder ${karaf.home}/kar_deploy. Created by
 * bryan on 4/13/15.
 * 
 * @author bryan
 * @author Joao L. M. Pereira (Joao.Pereira{[at]}pentaho.com)
 * @version 1.1
 */
public class BundleListClassloader extends ClassLoader {
  private static final String KAR_DEPLOY_FOLDER_NAME = "kar_deploy";

  private final BundleContext bundleContext;
  private final List<Class> servicesToTrack;
  private volatile List<Bundle> bundles;
  private static final Logger LOGGER = LoggerFactory.getLogger( BundleListClassloader.class );

  /**
   * Constructs the BundleListClassLoader which will be responsible to load the classes definitions for objects that are
   * instances of each class passed as argument. 
   * 
   * @param bundleContext
   *          bundle context to allow to subscribe the services that implement the classes wanted to load and to know
   *          which bundles contain such classes
   * @param servicesToTrack
   *          a list of Class to be tracked
   */
  public BundleListClassloader( BundleContext bundleContext, List<Class> servicesToTrack ) {
    this( bundleContext, servicesToTrack, null );
  }

  /**
   * Constructs the BundleListClassLoader which will be responsible to load the classes definitions for objects that are
   * instances of each class passed as argument. 
   * <p>
   * Additionally, it receives a KarService that loads kar files before
   * the BundleListClassLoader starts. This is mainly used when we initialize a client server that immediately needs to
   * have every bundle from the kar file installed, so it can instantiate the objects from Json format.
   * 
   * @param bundleContext
   *          bundle context to allow to subscribe the services that implement the classes wanted to load and to know
   *          which bundles contain such classes
   * @param servicesToTrack
   *          a list of Class to be tracked
   * @param karService
   *          a service that is used to install kar files
   */
  public BundleListClassloader( BundleContext bundleContext, List<Class> servicesToTrack, KarService karService ) {
    if ( karService != null )
      installDeployedKars( karService );

    this.bundleContext = bundleContext;
    this.servicesToTrack = servicesToTrack;
    List<ServiceTracker> serviceTrackers = new ArrayList<ServiceTracker>( servicesToTrack.size() );
    for ( Class aClass : servicesToTrack ) {
      serviceTrackers.add( new ServiceTracker( bundleContext, aClass, new ServiceTrackerCustomizer() {
        @Override public Object addingService( ServiceReference reference ) {
          synchronized ( BundleListClassloader.this ) {
            bundles = null;
          }
          return reference;
        }

        @Override public void modifiedService( ServiceReference reference, Object service ) {
          synchronized ( BundleListClassloader.this ) {
            bundles = null;
          }
        }

        @Override public void removedService( ServiceReference reference, Object service ) {
          synchronized ( BundleListClassloader.this ) {
            bundles = null;
          }
        }
      } ) );
    }
    for ( final ServiceTracker serviceTracker : serviceTrackers ) {
      serviceTracker.open();
      bundleContext.addBundleListener( new BundleListener() {
        @Override public void bundleChanged( BundleEvent event ) {
          if ( event.getType() == BundleEvent.STOPPING ) {
            serviceTracker.close();
          }
        }
      } );
    }
  }

  /**
   * This method is used to install bundles inside kar files before starting using the classloader, so this way we can guarantee that the
   * classes definitions are available when the system has to deserialize an object.
   */
  private void installDeployedKars( KarService karService ) {
    String karafHome = System.getProperty( "karaf.home" );
    File karDeployFolder = new File( karafHome + "/" + KAR_DEPLOY_FOLDER_NAME );
    if ( karDeployFolder.isDirectory() ) {
      File[] karFiles = karDeployFolder.listFiles();
      if ( karFiles != null )
        for ( File karFile : karFiles )
          try {
            karService.install( karFile.toURI() );
          } catch ( Exception e ) {
            LOGGER.error(e.getMessage(),e);
          }
    }
  }

  private synchronized List<Bundle> getBundles() {
    if ( bundles != null ) {
      return bundles;
    }
    Set<Bundle> bundleSet = new HashSet<Bundle>();
    List<Bundle> bundleList = new ArrayList<Bundle>();
    for ( Class aClass : servicesToTrack ) {
      try {
        for ( Object serviceReferenceObj : bundleContext.getServiceReferences( aClass, null ) ) {
          ServiceReference serviceReference = (ServiceReference) serviceReferenceObj;
          Bundle bundle = serviceReference.getBundle();
          if ( bundleSet.add( bundle ) ) {
            bundleList.add( bundle );
          }
        }
      } catch ( InvalidSyntaxException e ) {
        e.printStackTrace();
      }
    }
    bundleList.add( bundleContext.getBundle() );
    this.bundles = bundleList;
    return bundleList;
  }

  @Override public Class<?> loadClass( String name ) throws ClassNotFoundException {
    for ( Bundle bundle : getBundles() ) {
      try {
        return bundle.loadClass( name );
      } catch ( ClassNotFoundException e ) {
        // Ignore
      }
    }
    return super.loadClass( name );
  }
}
