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
import java.util.TreeMap;

/**
 * Created by jpereira on 8/9/15.
 */
public class ProfileFieldDistributedDTO implements ProfileField {
  private String physicalName;
  private String logicalName;
  private List<ProfileField> profileSubFields;
  private Map<String,ProfileField> profileSubFieldsMap;
  private Map<String, String> properties;
  private List<ProfileFieldValueType> types;
  private Map<String, ProfileFieldValueType> profileFieldValueTypeMap;

  public ProfileFieldDistributedDTO() {

  }

  public ProfileFieldDistributedDTO( ProfileField profileField ) {
    this.physicalName = profileField.getPhysicalName();
    this.logicalName = profileField.getLogicalName();
    this.profileSubFields = createSubFieldDtos(profileField.getProfileSubFields());
    Map<String, String> properties = profileField.getProperties();
    this.properties = properties == null ? null : new HashMap<String, String>( properties );
    this.types = createFieldDtos( profileField.getTypes() );
  }

  private static Map<String, ProfileField> subFieldsMapFromList( List<ProfileField> profileSubFields ) {
    if ( profileSubFields == null ) {
      return null;
    }
    TreeMap<String, ProfileField> treeMap = new TreeMap<String, ProfileField>();
    for ( ProfileField profileSubField : profileSubFields ) {
      treeMap.put( profileSubField.getPhysicalName(), profileSubField);
    }
    return treeMap;
  }
  
  private static List<ProfileField> createSubFieldDtos( List<ProfileField> profileSubFields ) {
    if ( profileSubFields == null ) {
      return null;
    }
    List<ProfileField> result = new ArrayList<ProfileField>( profileSubFields.size() );
    for ( ProfileField profileSubField : profileSubFields ) {
      result.add(new ProfileFieldDistributedDTO(profileSubField) );
    }
    return result;
  }

  private static List<ProfileFieldValueType> createFieldDtos( List<ProfileFieldValueType> profileFieldValueTypes ) {
    if ( profileFieldValueTypes == null ) {
      return null;
    }
    List<ProfileFieldValueType> result = new ArrayList<ProfileFieldValueType>( profileFieldValueTypes.size() );
    for ( ProfileFieldValueType profileFieldValueType : profileFieldValueTypes ) {
      result.add( new ProfileFieldValueTypeDistributedDTO( profileFieldValueType ) );
    }
    return result;
  }

  @Override public String getPhysicalName() {
    return physicalName;
  }

  public void setPhysicalName( String physicalName ) {
    this.physicalName = physicalName;
  }

  @Override public String getLogicalName() {
    return logicalName;
  }

  public void setLogicalName( String logicalName ) {
    this.logicalName = logicalName;
  }

  @Override public Map<String, String> getProperties() {
    return properties;
  }

  public void setProperties( Map<String, String> properties ) {
    this.properties = properties;
  }

  @Override public List<ProfileFieldValueType> getTypes() {
    return types;
  }

  public void setTypes( List<ProfileFieldValueType> types ) {
    this.types = types;
    profileFieldValueTypeMap = null;
  }

  @Override public ProfileFieldValueType getType( String name ) {
    if ( types == null ) {
      return null;
    }
    if ( profileFieldValueTypeMap == null ) {
      profileFieldValueTypeMap = new HashMap<String, ProfileFieldValueType>();
      for ( ProfileFieldValueType profileFieldValueType : types ) {
        profileFieldValueTypeMap.put( profileFieldValueType.getTypeName(), profileFieldValueType );
      }
    }
    return profileFieldValueTypeMap.get( name );
  }

  @Override public Set<String> typeKeys() {
    return profileFieldValueTypeMap.keySet();
  }
  
  @Override public ProfileField getProfileSubField( String physicalName ) {
    if ( profileSubFields == null ) {
      return null;
    }
    
    if(profileSubFieldsMap == null)
      profileSubFieldsMap = subFieldsMapFromList(profileSubFields);
    
    return profileSubFieldsMap.get( physicalName );
  }

  @Override public List<ProfileField> getProfileSubFields() {
    return new ArrayList<ProfileField>( profileSubFields);
  }
  
  public void setProfileSubFields (List<ProfileField> profileSubFields) {
    profileSubFieldsMap = null;
    this.profileSubFields = profileSubFields;
  }
  
  @Override public Object clone() {
    return new ProfileFieldDistributedDTO( this );
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

    ProfileFieldDistributedDTO that = (ProfileFieldDistributedDTO) o;

    if ( physicalName != null ? !physicalName.equals( that.physicalName ) : that.physicalName != null ) {
      return false;
    }
    if ( logicalName != null ? !logicalName.equals( that.logicalName ) : that.logicalName != null ) {
      return false;
    }
    if ( properties != null ? !properties.equals( that.properties ) : that.properties != null ) {
      return false;
    }
    if ( profileSubFields != null ? !profileSubFields.equals( that.profileSubFields ) : that.profileSubFields != null ) {
      return false;
    }
    return !( types != null ? !types.equals( that.types ) :
      that.types != null );

  }

  @Override public int hashCode() {
    int result = physicalName != null ? physicalName.hashCode() : 0;
    result = 31 * result + ( logicalName != null ? logicalName.hashCode() : 0 );
    result = 31 * result + ( properties != null ? properties.hashCode() : 0 );
    result = 31 * result + ( profileSubFields != null ? profileSubFields.hashCode() : 0 );
    result = 31 * result + ( types != null ? types.hashCode() : 0 );
    return result;
  }

  @Override public String toString() {
    return "ProfileFieldDistributedDTO{" +
      "physicalName='" + physicalName + '\'' +
      ", logicalName='" + logicalName + '\'' +
      ", properties=" + properties +
      ", profileSubFields=" + profileSubFields +
      ", types=" + types +
      '}';
  }
  //CHECKSTYLE:OperatorWrap:ON


}
