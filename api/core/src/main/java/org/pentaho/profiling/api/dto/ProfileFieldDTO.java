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
import org.pentaho.profiling.api.ProfileFieldValueType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * ProfileFieldDTO is a data transfer object for ProfileField created to be sent to front-end applications.
 * <p>
 * Created by bryan on 4/30/15.
 * 
 * @author bryan
 * @author Joao L. M. Pereira (Joao.Pereira{[at]}pentaho.com)
 * @version 1.1
 */

public class ProfileFieldDTO  {
  private String physicalName;
  private String logicalName;
  private Map<String, String> properties;
  private List<ProfileFieldValueTypeDTO> types;
  private Map<String, ProfileFieldValueTypeDTO> profileFieldValueTypeMap;

  /**
   * Empty constructor, it is required for json serialization.
   */
  public ProfileFieldDTO() {
  }

  /**
   * Creates a new ProfileFiledDTO containing the same information of profileField
   * @param profileField the profile field used to create the data transfer object
   */
  public ProfileFieldDTO( ProfileField profileField ) {
    this.physicalName = profileField.getPhysicalName();
    this.logicalName = profileField.getLogicalName();
    Map<String, String> properties = profileField.getProperties();
    this.properties = properties == null ? null : new HashMap<String, String>( properties );
    this.types = createFieldDtos( profileField.getTypes() );
  }
  
  /**
   * Creates a new ProfileFiledDTO containing the same information of profileField.
   * 
   * @param profileField
   *          the profile field used to create the data transfer object
   * @param nameToAppend
   *          its a suffix String to be appended to the ProfileFieldDTO name. It is used to distinguish sub fields from
   *          root fields and to identify their parent field
   * @param result
   *          the list of ProfileFieldDTO maintained by ProfileStatusDTO, it is used to add the sub profile fields of
   *          ProfileFields to ProfileStatusDTO because ProfileFieldDTO can not contain sub fields.
   */
  public ProfileFieldDTO( ProfileField profileField, String nameToAppend, List<ProfileFieldDTO> result ) {
    this.physicalName = nameToAppend  + profileField.getPhysicalName();
    this.logicalName = nameToAppend  + profileField.getLogicalName();
    Map<String, String> properties = profileField.getProperties();
    this.properties = properties == null ? null : new HashMap<String, String>( properties );
    this.types = createFieldDtos( profileField.getTypes() );
    
    createProfileSubFieldDtos(profileField.getProfileSubFields(),logicalName + ".",result);
    }

  private static List<ProfileFieldValueTypeDTO> createFieldDtos( List<ProfileFieldValueType> profileFieldValueTypes ) {
    if ( profileFieldValueTypes == null ) {
      return null;
    }
    List<ProfileFieldValueTypeDTO> result = new ArrayList<ProfileFieldValueTypeDTO>( profileFieldValueTypes.size() );
    for ( ProfileFieldValueType profileFieldValueType : profileFieldValueTypes ) {
      result.add( new ProfileFieldValueTypeDTO( profileFieldValueType ) );
    }
    return result;
  }

  
  private static void createProfileSubFieldDtos( List<ProfileField> profileField, String nameToAppend, List<ProfileFieldDTO> result) {
    if ( profileField == null ) {
      return;
    }
    for ( ProfileField profileSubField : profileField ) {
      result.add( new ProfileFieldDTO( profileSubField,nameToAppend, result ) );
    }
  }
  
  /**
   * 
   * @return the internal representation name for this ProfileFieldDTO
   */
  public String getPhysicalName() {
    return physicalName;
  }

  /**
   *
   * @param physicalName an internal representation name for this ProfileFieldDTO
   */
  public void setPhysicalName( String physicalName ) {
    this.physicalName = physicalName;
  }
  
  /**
   * 
   * @return a suitable name for this ProfileFieldDTO to be shown to users
   */
  public String getLogicalName() {
    return logicalName;
  }

  /**
   * 
   * @param logicalName a suitable name for this ProfileFieldDTO to be shown to users
   */
  public void setLogicalName( String logicalName ) {
    this.logicalName = logicalName;
  }

  /**
   * TODO JavaDoc
   * @return
   */
  public Map<String, String> getProperties() {
    return properties;
  }

  /**
   * TODO JavaDoc
   * @return
   */
  public void setProperties( Map<String, String> properties ) {
    this.properties = properties;
  }

  /**
   * 
   * @return a list of ProfileFieldValueTypeDTO. A ProfileFieldValueTypeDTO contains the statistical values per value
   *         type evaluated in this field.
   */
  public List<ProfileFieldValueTypeDTO> getTypes() {
    return types;
  }

  /**
   * 
   * @param types
   *          a list of ProfileFieldValueTypeDTO. A ProfileFieldValueTypeDTO contains the statistical values per value
   *          type evaluated in this field.
   */
  public void setTypes( List<ProfileFieldValueTypeDTO> types ) {
    this.types = types;
    profileFieldValueTypeMap = null;
  }

  /**
   * 
   * @param name
   *          the type of the field values
   * @return the ProfileFieldValueTypeDTO containing name as a value type
   */
  public ProfileFieldValueTypeDTO getType( String name ) {
    if ( types == null ) {
      return null;
    }
    if ( profileFieldValueTypeMap == null ) {
      profileFieldValueTypeMap = new HashMap<String, ProfileFieldValueTypeDTO>();
      for ( ProfileFieldValueTypeDTO profileFieldValueType : types ) {
        profileFieldValueTypeMap.put( profileFieldValueType.getTypeName(), profileFieldValueType );
      }
    }
    return profileFieldValueTypeMap.get( name );
  }

  /**
   * The value types evaluated for this field
   * 
   * @return a set of value types evaluated
   */
  public Set<String> typeKeys() {
    return profileFieldValueTypeMap.keySet();
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

    ProfileFieldDTO that = (ProfileFieldDTO) o;

    if ( physicalName != null ? !physicalName.equals( that.physicalName ) : that.physicalName != null ) {
      return false;
    }
    if ( logicalName != null ? !logicalName.equals( that.logicalName ) : that.logicalName != null ) {
      return false;
    }
    if ( properties != null ? !properties.equals( that.properties ) : that.properties != null ) {
      return false;
    }
    return !( types != null ? !types.equals( that.types ) :
      that.types != null );

  }

  @Override public int hashCode() {
    int result = physicalName != null ? physicalName.hashCode() : 0;
    result = 31 * result + ( logicalName != null ? logicalName.hashCode() : 0 );
    result = 31 * result + ( properties != null ? properties.hashCode() : 0 );
    result = 31 * result + ( types != null ? types.hashCode() : 0 );
    return result;
  }

  @Override public String toString() {
    return "ProfileFieldDTO{" +
      "physicalName='" + physicalName + '\'' +
      ", logicalName='" + logicalName + '\'' +
      ", properties=" + properties +
      ", types=" + types +
      '}';
  }
  //CHECKSTYLE:OperatorWrap:ON
}
