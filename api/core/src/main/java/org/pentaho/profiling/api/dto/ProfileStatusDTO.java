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

package org.pentaho.profiling.api.dto;

import org.pentaho.profiling.api.ProfileField;
import org.pentaho.profiling.api.ProfileFieldProperty;
import org.pentaho.profiling.api.ProfileState;
import org.pentaho.profiling.api.ProfileStatus;
import org.pentaho.profiling.api.ProfileStatusMessage;
import org.pentaho.profiling.api.action.ProfileActionExceptionWrapper;
import org.pentaho.profiling.api.configuration.ProfileConfiguration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * ProfileStatusDTO is a data transfer object for ProfileStatus created to be sent to front-end applications.
 * <p>
 * Created by bryan on 3/27/15.
 * 
 * @author bryan
 * @author Joao L. M. Pereira (Joao.Pereira{[at]}pentaho.com)
 * @version 1.1
 */
public class ProfileStatusDTO {
  private ProfileState profileState;
  private String name;
  private String id;
  private ProfileConfiguration profileConfiguration;
  private List<ProfileFieldDTO> fields;
  private Long totalEntities;
  private List<ProfileStatusMessage> statusMessages;
  private ProfileActionExceptionWrapper operationError;
  private List<ProfileFieldProperty> profileFieldProperties;
  private long sequenceNumber;

  /**
   * Empty constructor, it is required for json serialization.
   */
  public ProfileStatusDTO() {
    this( null, null, null, null, null, null, null, null, null, 0L );
  }

  /**
   * Creates a new ProfileStatusDTO containing the same information of profileStatus
   * 
   * @param profileStatus
   *          the profile status used to create the data transfer object
   */
  public ProfileStatusDTO( ProfileStatus profileStatus ) {
    this( profileStatus.getProfileState(), profileStatus.getName(), profileStatus.getId(), profileStatus
        .getProfileConfiguration(), createFieldDtos( profileStatus.getFields() ), profileStatus.getTotalEntities(),
        profileStatus.getStatusMessages(), profileStatus.getOperationError(), profileStatus.getProfileFieldProperties(),
        profileStatus.getSequenceNumber() );
  }

  /**
   * Creates a new ProfileStatusDTO
   * 
   * @param profileState
   *          a profile state that indicates if a Profile is active, stopped, discarder, finished with errors or
   *          finished successfully
   * @param name
   *          a name for the profile status
   * @param id
   *          an id to identify the profile status
   * @param profileConfiguration
   *          a profile configuration that contains the metric contributors and metadata information
   * @param fields
   *          a list of ProfileField objects
   * @param totalEntities
   *          TODO
   * @param statusMessages
   *          a list of ProfileStatusMessage which contains information to the user
   * @param operationError
   *          a ProfileActionExceptionWrapper TODO JavaDoc
   * @param profileFieldProperties
   *          a list of ProfileFieldProperty which indicates the location of the metric values
   * @param sequenceNumber
   *          a sequence number, incremented after updates
   */
  public ProfileStatusDTO( ProfileState profileState, String name, String id, ProfileConfiguration profileConfiguration,
      List<ProfileFieldDTO> fields, Long totalEntities, List<ProfileStatusMessage> statusMessages,
      ProfileActionExceptionWrapper operationError, List<ProfileFieldProperty> profileFieldProperties,
      long sequenceNumber ) {
    this.profileState = profileState;
    this.name = name;
    this.id = id;
    this.profileConfiguration = profileConfiguration;
    this.fields = fields;
    this.totalEntities = totalEntities;
    this.statusMessages = statusMessages;
    this.operationError = operationError;
    this.profileFieldProperties = profileFieldProperties;
    this.sequenceNumber = sequenceNumber;
  }

  private static List<ProfileFieldDTO> createFieldDtos( List<ProfileField> profileFields ) {
    if ( profileFields == null ) {
      return null;
    }
    List<ProfileFieldDTO> result = new ArrayList<ProfileFieldDTO>( profileFields.size() );
    for ( ProfileField profileField : profileFields ) {
      result.add( new ProfileFieldDTO( profileField, "", result ) );
    }
    return result;
  }

  /**
   * 
   * @return the name of this profile status
   */
  public String getName() {
    return name;
  }

  /**
   * 
   * @param name
   *          a name for the profile status
   */
  public void setName( String name ) {
    this.name = name;
  }

  /**
   * 
   * @return the profile state of this profile status that indicates if a Profile is active, stopped, discarder,
   *         finished with errors or finished successfully
   */
  @XmlElement
  public ProfileState getProfileState() {
    return profileState;
  }

  /**
   * 
   * @param profileState
   *          a profile state of this profile status that indicates if a Profile is active, stopped, discarder, finished
   *          with errors or finished successfully
   */
  public void setProfileState( ProfileState profileState ) {
    this.profileState = profileState;
  }

  /**
   * 
   * @return the id that identifies the profile status
   */
  @XmlElement
  public String getId() {
    return id;
  }

  /**
   * 
   * @param id
   *          an id to identify the profile status
   */
  public void setId( String id ) {
    this.id = id;
  }

  /**
   * 
   * @return the profile configuration of this profile status that contains the metric contributors and metadata
   *         information
   */
  @XmlElement
  public ProfileConfiguration getProfileConfiguration() {
    return profileConfiguration;
  }

  /**
   * 
   * @param profileConfiguration
   *          a profile configuration that contains the metric contributors and metadata information
   */
  public void setProfileConfiguration( ProfileConfiguration profileConfiguration ) {
    this.profileConfiguration = profileConfiguration;
  }

  /**
   * 
   * @return the list of ProfileField objects of this profile status
   */
  @XmlElement
  public List<ProfileFieldDTO> getFields() {
    return fields;
  }

  /**
   * 
   * @param fields
   *          a list of ProfileField objects
   */
  public void setFields( List<ProfileFieldDTO> fields ) {
    this.fields = fields;
  }

  /**
   * TODO JavaDoc
   * 
   * @return
   */
  @XmlElement
  public Long getTotalEntities() {
    return totalEntities;
  }

  /**
   * TODO JavaDoc
   * 
   * @return
   */
  public void setTotalEntities( Long totalEntities ) {
    this.totalEntities = totalEntities;
  }

  /**
   * 
   * @return the list of ProfileStatusMessage of this profile status which contains information to the user
   */
  @XmlElement
  public List<ProfileStatusMessage> getStatusMessages() {
    return statusMessages;
  }

  /**
   * 
   * @param statusMessages
   *          a list of ProfileStatusMessage which contains information to the user
   */
  public void setStatusMessages( List<ProfileStatusMessage> statusMessages ) {
    this.statusMessages = statusMessages;
  }

  /**
   * TODO JavaDoC
   * 
   * @return
   */
  @XmlElement
  public ProfileActionExceptionWrapper getOperationError() {
    return operationError;
  }

  /**
   * TODO JavaDoc
   * 
   * @param operationError
   */
  public void setOperationError( ProfileActionExceptionWrapper operationError ) {
    this.operationError = operationError;
  }

  /**
   * 
   * @return the list of ProfileFieldProperty of this profile status which indicate the location of the metric values
   */
  @XmlElement
  public List<ProfileFieldProperty> getProfileFieldProperties() {
    return profileFieldProperties;
  }

  /**
   * 
   * @param profileFieldProperties
   *          a list of ProfileFieldProperty which indicates the location of the metric values
   */
  public void setProfileFieldProperties( List<ProfileFieldProperty> profileFieldProperties ) {
    this.profileFieldProperties = profileFieldProperties;
  }

  /**
   * 
   * @return the sequence number, incremented after updates
   */
  @XmlElement
  public long getSequenceNumber() {
    return sequenceNumber;
  }

  /**
   * 
   * @param sequenceNumber
   *          a sequence number, incremented after updates
   */
  public void setSequenceNumber( long sequenceNumber ) {
    this.sequenceNumber = sequenceNumber;
  }

  
  //OperatorWrap isn't helpful for autogenerated methods
  //CHECKSTYLE:OperatorWrap:OFF
  @Override public boolean equals( Object o ) {
    if ( this == o ) {
      return true;
    }
    if ( o == null || getClass() != o.getClass() ) {
      return false;
    }

    ProfileStatusDTO that = (ProfileStatusDTO) o;

    if ( sequenceNumber != that.sequenceNumber ) {
      return false;
    }
    if ( profileState != that.profileState ) {
      return false;
    }
    if ( name != null ? !name.equals( that.name ) : that.name != null ) {
      return false;
    }
    if ( id != null ? !id.equals( that.id ) : that.id != null ) {
      return false;
    }
    if ( profileConfiguration != null ? !profileConfiguration.equals( that.profileConfiguration ) :
      that.profileConfiguration != null ) {
      return false;
    }
    if ( fields != null ? !fields.equals( that.fields ) : that.fields != null ) {
      return false;
    }
    if ( totalEntities != null ? !totalEntities.equals( that.totalEntities ) : that.totalEntities != null ) {
      return false;
    }
    if ( statusMessages != null ? !statusMessages.equals( that.statusMessages ) : that.statusMessages != null ) {
      return false;
    }
    if ( operationError != null ? !operationError.equals( that.operationError ) : that.operationError != null ) {
      return false;
    }
    return !( profileFieldProperties != null ? !profileFieldProperties.equals( that.profileFieldProperties ) :
      that.profileFieldProperties != null );

  }

  @Override public int hashCode() {
    int result = profileState != null ? profileState.hashCode() : 0;
    result = 31 * result + ( name != null ? name.hashCode() : 0 );
    result = 31 * result + ( id != null ? id.hashCode() : 0 );
    result = 31 * result + ( profileConfiguration != null ? profileConfiguration.hashCode() : 0 );
    result = 31 * result + ( fields != null ? fields.hashCode() : 0 );
    result = 31 * result + ( totalEntities != null ? totalEntities.hashCode() : 0 );
    result = 31 * result + ( statusMessages != null ? statusMessages.hashCode() : 0 );
    result = 31 * result + ( operationError != null ? operationError.hashCode() : 0 );
    result = 31 * result + ( profileFieldProperties != null ? profileFieldProperties.hashCode() : 0 );
    result = 31 * result + (int) ( sequenceNumber ^ ( sequenceNumber >>> 32 ) );
    return result;
  }

  @Override public String toString() {
    return "ProfileStatusDTO{" +
      "profileState=" + profileState +
      ", name='" + name + '\'' +
      ", id='" + id + '\'' +
      ", profileConfiguration=" + profileConfiguration +
      ", fields=" + fields +
      ", totalEntities=" + totalEntities +
      ", statusMessages=" + statusMessages +
      ", operationError=" + operationError +
      ", profileFieldProperties=" + profileFieldProperties +
      ", sequenceNumber=" + sequenceNumber +
      '}';
  }
  //CHECKSTYLE:OperatorWrap:ON


//  public Object clone() throws CloneNotSupportedException {
//    return new ProfileStatusDTO( this );
//  }
}
