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

import org.pentaho.profiling.api.ProfileField;
import org.pentaho.profiling.api.ProfileFieldValueType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.pentaho.profiling.api.util.PublicCloneableUtil.copyMap;

/**
 * ProfileFieldImpl implements the internal functionalities expected for a object of ProfileField
 * <p>
 * Created by bryan on 4/30/15.
 * 
 * @author bryan
 * @author Joao L. M. Pereira (Joao.Pereira{[at]}pentaho.com)
 * @version 1.1
 */
public class ProfileFieldImpl implements ProfileField {
  protected Map<String, ProfileField> profileSubFields;
  protected Map<String, ProfileFieldValueType> types;
  protected Map<String, String> properties;
  private String physicalName;
  private String logicalName;

  /**
   * Creates an empty ProfileFiledImpl by just receiving two names, one is the internal representation that works as Id
   * and the other is a name representation for the user
   * 
   * @param physicalName
   *          an unique internal name representation
   * @param logicalName
   *          a suitable name to be shown to users
   */
  public ProfileFieldImpl( String physicalName, String logicalName ) {
    this( physicalName, logicalName, new HashMap<String, ProfileField>(), new HashMap<String, String>(), new HashMap<String, ProfileFieldValueType>() );
  }

  /**
   * Creates a ProfileFieldImpl with the same information as the profileField
   * @param profileField a ProfileField which variables are used to initialize a ProfileFieldImpl
   */
  public ProfileFieldImpl( ProfileField profileField ) {
    this( profileField.getPhysicalName(), profileField.getLogicalName(), subFieldsMapFromList( profileField
        .getProfileSubFields() ), profileField.getProperties(), mapFromList( profileField.getTypes() ) );
  }

  /**
   * Creates a ProfileFiledImpl by providing its structure including the representation names, sub fields and value
   * types containing metric holders.
   * 
   * @param physicalName
   *          an unique internal name representation
   * @param logicalName
   *          a suitable name to be shown to users
   * @param profileSubFields
   *          a list of ProfileFields to be the sub fields of this ProfileField
   * @param properties
   *          TODO
   * @param types
   *          a list of ProfileFieldValueType. A ProfileFieldValueType contains the statistical values per value type
   *          evaluated in this field.
   */
  public ProfileFieldImpl( String physicalName, String logicalName, Map<String, ProfileField> profileSubFields,
      Map<String, String> properties, Map<String, ProfileFieldValueType> types ) {
    this.physicalName = physicalName;
    this.logicalName = logicalName;
    this.profileSubFields = new HashMap<String, ProfileField>( profileSubFields );
    this.properties = new HashMap<String, String>( properties );
    this.types = new HashMap<String, ProfileFieldValueType>();
    copyMap( types, this.types );
  }

  private static Map<String, ProfileField> subFieldsMapFromList( List<ProfileField> profileSubFields ) {
    if ( profileSubFields == null ) {
      return null;
    }
    TreeMap<String, ProfileField> treeMap = new TreeMap<String, ProfileField>();
    for ( ProfileField profileSubField : profileSubFields ) {
      treeMap.put( profileSubField.getPhysicalName(), profileSubField );
    }
    return treeMap;
  }

  private static Map<String, ProfileFieldValueType> mapFromList( List<ProfileFieldValueType> profileFieldValueTypes ) {
    if ( profileFieldValueTypes == null ) {
      return null;
    }
    TreeMap<String, ProfileFieldValueType> treeMap = new TreeMap<String, ProfileFieldValueType>();
    for ( ProfileFieldValueType profileFieldValueType : profileFieldValueTypes ) {
      treeMap.put( profileFieldValueType.getTypeName(), profileFieldValueType );
    }
    return treeMap;
  }

  @Override public String getPhysicalName() {
    return physicalName;
  }

  @Override public String getLogicalName() {
    return logicalName;
  }

  @Override public ProfileField getProfileSubField( String physicalName ) {
    return profileSubFields.get( physicalName );
  }

  @Override public List<ProfileField> getProfileSubFields() {
    return new ArrayList<ProfileField>( profileSubFields.values() );
  }

  @Override public List<ProfileFieldValueType> getTypes() {
    return new ArrayList<ProfileFieldValueType>( types.values() );
  }

  @Override public Map<String, String> getProperties() {
    return Collections.unmodifiableMap( properties );
  }

  @Override public ProfileFieldValueType getType( String name ) {
    return types.get( name );
  }

  @Override public Set<String> typeKeys() {
    return new HashSet<String>( types.keySet() );
  }

  @Override public Object clone() {
    return new ProfileFieldImpl( this );
  }
}
