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

import org.pentaho.profiling.api.MutableProfileField;
import org.pentaho.profiling.api.MutableProfileFieldValueType;
import org.pentaho.profiling.api.ProfileField;
import org.pentaho.profiling.api.ProfileFieldValueType;

import java.util.Map;

/**
 * Created by bryan on 4/30/15.
 * Modified by jpereira
 */
public class MutableProfileFieldImpl extends ProfileFieldImpl implements MutableProfileField {
  public MutableProfileFieldImpl( String physicalName, String logicalName ) {
    super( physicalName, logicalName );
  }

  public MutableProfileFieldImpl( ProfileField profileField ) {
    super( profileField );
  }

  @Override public MutableProfileField getProfileSubField( String physicalName ) {
    ProfileField profileSubField = profileSubFields.get( physicalName );
    MutableProfileField result;
    if ( profileSubField == null ) {
      return null;
    }
    if ( profileSubField instanceof MutableProfileField ) {
      return (MutableProfileField) profileSubField;
    }
    result = new MutableProfileFieldImpl( profileSubField );
    profileSubFields.put( physicalName, result );
    return result;
  }

  @Override public void putProfileSubField( String physicalName, ProfileField profileSubField ) {
    profileSubFields.put( physicalName, new MutableProfileFieldImpl( profileSubField ) );
  }

  @Override public MutableProfileField getOrCreateProfileSubField( String physicalName, String logicalName ) {
    ProfileField profileSubField = profileSubFields.get( physicalName );
    MutableProfileField result;
    if ( profileSubField == null ) {
      result = new MutableProfileFieldImpl( physicalName, logicalName );
    } else {
      if ( profileSubField instanceof MutableProfileField ) {
        return (MutableProfileField) profileSubField;
      } else {
        result = new MutableProfileFieldImpl( profileSubField );
      }
    }
    profileSubFields.put( physicalName, result );
    return result;
  }

  @Override public MutableProfileFieldValueType getValueTypeMetrics( String name ) {
    ProfileFieldValueType profileFieldValueType = types.get( name );
    MutableProfileFieldValueType result;
    if ( profileFieldValueType == null ) {
      return null;
    }
    if ( profileFieldValueType instanceof MutableProfileFieldValueType ) {
      return (MutableProfileFieldValueType) profileFieldValueType;
    }
    result = new MutableProfileFieldValueTypeImpl( profileFieldValueType );
    types.put( name, result );
    return result;
  }

  @Override public void putValueTypeMetrics( String name, ProfileFieldValueType profileFieldValueType ) {
    types.put( name, new MutableProfileFieldValueTypeImpl( profileFieldValueType ) );
  }

  @Override public MutableProfileFieldValueType getOrCreateValueTypeMetrics( String name ) {
    ProfileFieldValueType profileFieldValueType = types.get( name );
    MutableProfileFieldValueType result;
    if ( profileFieldValueType == null ) {
      result = new MutableProfileFieldValueTypeImpl();
      result.setTypeName( name );
    } else {
      if ( profileFieldValueType instanceof MutableProfileFieldValueType ) {
        return (MutableProfileFieldValueType) profileFieldValueType;
      } else {
        result = new MutableProfileFieldValueTypeImpl( profileFieldValueType );
      }
    }
    types.put( name, result );
    return result;
  }

  @Override public Map<String, String> getProperties() {
    return properties;
  }
}
