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

import org.pentaho.profiling.api.MutableProfileStatus;
import org.pentaho.profiling.api.Profile;
import org.pentaho.profiling.api.ProfileCreationException;
import org.pentaho.profiling.api.ProfileFactory;
import org.pentaho.profiling.api.ProfileState;
import org.pentaho.profiling.api.ProfileStatus;
import org.pentaho.profiling.api.ProfileStatusManager;
import org.pentaho.profiling.api.ProfileStatusReader;
import org.pentaho.profiling.api.ProfileStatusWriteOperation;
import org.pentaho.profiling.api.ProfilingService;
import org.pentaho.profiling.api.configuration.DataSourceMetadata;
import org.pentaho.profiling.api.configuration.ProfileConfiguration;
import org.pentaho.profiling.api.dto.ProfileStatusDTO;
import org.pentaho.profiling.api.metrics.MetricContributorService;
import org.pentaho.profiling.api.util.Pair;
import org.pentaho.osgi.notification.api.DelegatingNotifierImpl;
import org.pentaho.osgi.notification.api.NotificationListener;
import org.pentaho.osgi.notification.api.NotificationObject;
import org.pentaho.osgi.notification.api.NotifierWithHistory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

/**
 * ProfilingServiceImpl implements the internal functionalities expected for a object of ProfilingService. It also
 * implements the NotifierWithHistory which allows other services to register for its provided services
 * <p>
 * Created by bryan on 7/31/14.
 * 
 * @author bryan
 * @author Joao L. M. Pereira (Joao.Pereira{[at]}pentaho.com)
 * @version 1.1
 */
public class ProfilingServiceImpl implements ProfilingService, NotifierWithHistory {
  /**
   * Name to register for ProfileStatus notifications
   */
  public static final String PROFILE_STATUS_CANONICAL_NAME = ProfileStatus.class.getCanonicalName();
  /**
   * Name to register for ProfileStatusDTO notifications
   */
  public static final String PROFILE_STATUS_DTO_CANONICAL_NAME = ProfileStatusDTO.class.getCanonicalName();
  /**
   * Name to register for the ProfilingService
   */
  public static final String PROFILING_SERVICE_CANONICAL_NAME = ProfilingService.class.getCanonicalName();
  private final Map<String, Profile> profileMap = new ConcurrentHashMap<String, Profile>();
  private final Map<String, ProfileStatusManager> profileStatusManagerMap = new ConcurrentHashMap<String,
    ProfileStatusManager>();
  private final Map<String, NotificationObject> previousNotifications = new ConcurrentHashMap<String,
    NotificationObject>();
  private final DelegatingNotifierImpl delegatingNotifier =
    new DelegatingNotifierImpl(
      new HashSet<String>( Arrays.asList( PROFILING_SERVICE_CANONICAL_NAME, PROFILE_STATUS_CANONICAL_NAME, PROFILE_STATUS_DTO_CANONICAL_NAME ) ), this );

  private final ExecutorService executorService;
  private final MetricContributorService metricContributorService;
  private final AtomicLong profilesSequence = new AtomicLong( 1L );
  private List<Pair<Integer, ProfileFactory>> factories = new ArrayList<Pair<Integer, ProfileFactory>>();

  /**
   * Constructs the ProfilingServiceImpl 
   * 
   * @param executorService a service that accepts tasks and executes them
   * @param metricContributorService a service that provides metric contributors that produce metric values for the profile
   */
  public ProfilingServiceImpl( ExecutorService executorService, MetricContributorService metricContributorService ) {
    this.executorService = executorService;
    this.metricContributorService = metricContributorService;
  }

  // FOR UNIT TEST ONLY
  protected Map<String, Profile> getProfileMap() {
    return profileMap;
  }

  // FOR UNIT TEST ONLY
  protected Map<String, ProfileStatusManager> getProfileStatusManagerMap() {
    return profileStatusManagerMap;
  }

  @Override public ProfileFactory getProfileFactory(
    DataSourceMetadata dataSourceMetadata ) {
    synchronized ( factories ) {
      for ( Pair<Integer, ProfileFactory> factoryPair : factories ) {
        ProfileFactory factory = factoryPair.getSecond();
        if ( factory.accepts( dataSourceMetadata ) ) {
          return factory;
        }
      }
    }
    return null;
  }

  @Override public boolean accepts( DataSourceMetadata dataSourceMetadata ) {
    return getProfileFactory( dataSourceMetadata ) != null;
  }

  @Override
  public ProfileStatusManager create( ProfileConfiguration profileConfiguration ) throws ProfileCreationException {
    ProfileFactory profileOperationProviderFactory =
      getProfileFactory( profileConfiguration.getDataSourceMetadata() );
    String configName = profileConfiguration.getConfigName();
    if ( configName != null ) {
      profileConfiguration.setMetricContributors( metricContributorService.getDefaultMetricContributors( configName ) );
    } else if ( profileConfiguration.getMetricContributors() == null ) {
      profileConfiguration.setMetricContributors(
        metricContributorService.getDefaultMetricContributors( MetricContributorService.DEFAULT_CONFIGURATION ) );
    }
    if ( profileOperationProviderFactory != null ) {
      String profileId = UUID.randomUUID().toString();
      ProfileStatusManager profileStatusManager =
        new ProfileStatusManagerImpl( profileId, null, profileConfiguration, this );
      Profile profile = profileOperationProviderFactory.create( profileConfiguration, profileStatusManager );
      profile.start( executorService );
      profileMap.put( profile.getId(), profile );
      profileStatusManagerMap.put( profile.getId(), profileStatusManager );
      notifyProfiles();
      return profileStatusManager;
    }

    return null;
  }

  private void notifyProfiles() {
    NotificationObject notificationObject =
      new NotificationObject( PROFILING_SERVICE_CANONICAL_NAME, PROFILES, profilesSequence.getAndIncrement(),
        new ArrayList<Profile>( profileMap.values() ) );
    previousNotifications.put( PROFILES, notificationObject );
    try {
      delegatingNotifier.notify( notificationObject );
    } catch ( Exception e ) {
      e.printStackTrace();
    }
  }

  @Override
  public List<ProfileStatusReader> getActiveProfiles() {
    return new ArrayList<ProfileStatusReader>( profileStatusManagerMap.values() );
  }

  @Override
  public ProfileStatusManager getProfileUpdate( String profileId ) {
    return profileStatusManagerMap.get( profileId );
  }

  @Override public void stop( String profileId ) {
    Profile profile = profileMap.get( profileId );
    if ( profile != null ) {
      profile.stop();
    }
  }

  @Override public void stopAll() {
    for ( Profile profile : profileMap.values() ) {
      profile.stop();
    }
  }

  @Override public boolean isRunning( String profileId ) {
    return profileMap.get( profileId ).isRunning();
  }

  @Override public void discardProfile( String profileId ) {
    stop( profileId );
    profileMap.remove( profileId );
    ProfileStatusManager remove = profileStatusManagerMap.remove( profileId );
    if ( remove != null ) {
      remove.write( new ProfileStatusWriteOperation<Void>() {
        @Override public Void write( MutableProfileStatus profileStatus ) {
          profileStatus.setProfileState( ProfileState.DISCARDED );
          return null;
        }
      } );
    }
    previousNotifications.remove( profileId );
    notifyProfiles();
  }

  @Override public void discardProfiles() {
    for ( Map.Entry<String, Profile> entry : profileMap.entrySet() ) {
      discardProfile( entry.getKey() );
    }
  }

  @Override public List<NotificationObject> getPreviousNotificationObjects() {
    return new ArrayList<NotificationObject>( previousNotifications.values() );
  }

  @Override public Set<String> getEmittedTypes() {
    return delegatingNotifier.getEmittedTypes();
  }

  @Override public void register( NotificationListener notificationListener ) {
    delegatingNotifier.register( notificationListener );
  }

  @Override public void unregister( NotificationListener notificationListener ) {
    delegatingNotifier.unregister( notificationListener );
  }

  /**
   * The notify method receives a ProfileStatus and notifies two objects, one is the raw updated ProfileStatus, the
   * other is the ProfileStatusDTO of the first object.
   * 
   * TODO There are some doubts about the notification bundle behavior. I don't know if the NotificationObject is
   * supposed to be filtered by its type before being send to services that did not register to objects type. Proper
   * modifications should be done to prevent that both ProfileStauts and ProfileStatusDTO are sent to the same service.
   * 
   * @param profileStatus
   *          the ProfileStatus object to be the notification to other services
   */
  public void notify( ProfileStatus profileStatus ) {
    NotificationObject notificationObjectToContainer =
        new NotificationObject( PROFILE_STATUS_CANONICAL_NAME, profileStatus.getId(), profileStatus.getSequenceNumber(),
            profileStatus );
    previousNotifications.put( profileStatus.getId(), notificationObjectToContainer );
    try {
      delegatingNotifier.notify( notificationObjectToContainer );
    } catch ( Throwable e ) {
      e.printStackTrace();
    }

    NotificationObject notificationObjectToWebapp =
        new NotificationObject( PROFILE_STATUS_DTO_CANONICAL_NAME, profileStatus.getId(), profileStatus
            .getSequenceNumber(), new ProfileStatusDTO( profileStatus ) );
    previousNotifications.put( profileStatus.getId(), notificationObjectToWebapp );
    try {
      delegatingNotifier.notify( notificationObjectToWebapp );
    } catch ( Throwable e ) {
      e.printStackTrace();
    }
  }

  /**
   * Adds a new profile factory to this ProfilingService with a ranking number, this method is thread safe.
   * 
   * @param profileFactory the profile factory to be added
   * @param properties a map containing properties, where one can be the rank
   */
  public void profileFactoryAdded( ProfileFactory profileFactory, Map properties ) {
    Integer ranking = (Integer) properties.get( "service.ranking" );
    if ( ranking == null ) {
      ranking = 0;
    }
    synchronized ( factories ) {
      factories.add( Pair.of( ranking, profileFactory ) );
      Collections.sort( factories, new Comparator<Pair<Integer, ProfileFactory>>() {
        @Override public int compare( Pair<Integer, ProfileFactory> o1,
                                      Pair<Integer, ProfileFactory> o2 ) {
          int result = o2.getFirst() - o1.getFirst();
          if ( result == 0 ) {
            result = o1.getSecond().toString().compareTo( o2.getSecond().toString() );
          }
          return result;
        }
      } );
    }
  }

  /**
   * Removes a profile factory from this ProfilingService, this method is thread safe.
   * 
   * @param profileFactory the profile factory to be removed
   * @param properties a map containing properties
   */
  public void profileFactoryRemoved( ProfileFactory profileFactory, Map properties ) {
    List<Pair<Integer, ProfileFactory>> newFactories = null;
    synchronized ( this.factories ) {
      newFactories =
        new ArrayList<Pair<Integer, ProfileFactory>>( Math.max( 0, this.factories.size() - 1 ) );
      for ( Pair<Integer, ProfileFactory> factoryPair : this.factories ) {
        if ( !factoryPair.getSecond().equals( profileFactory ) ) {
          newFactories.add( factoryPair );
        }
      }
      this.factories = newFactories;
    }
  }

  @Override public Profile getProfile( String profileId ) {
    return profileMap.get( profileId );
  }

  /**
   * This method registers an existing Profile and its ProfileStatusManager into this ProfilingServiceImpl
   * 
   * @param profile the existing Profile to register
   * @param profileStatusManager the existing ProfileStatusManager associated to profile to register
   */
  public void registerProfile( Profile profile, ProfileStatusManager profileStatusManager ) {
    profileMap.put( profile.getId(), profile );
    profileStatusManagerMap.put( profileStatusManager.getId(), profileStatusManager );
  }
}
