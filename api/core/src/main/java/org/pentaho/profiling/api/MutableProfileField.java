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

package org.pentaho.profiling.api;

/**
 * MutableProfileField specifies a mutable ProfileField behavior. It allows the field to receive new ProfileFields and
 * ProfileFieldValueTypes. It is mainly used during the profiling process to add new profiling information related to
 * this field.
 * <p>
 * Created by bryan on 4/30/15.
 * 
 * @author bryan
 * @author Joao L. M. Pereira (Joao.Pereira{[at]}pentaho.com)
 * @version 1.1
 */
public interface MutableProfileField extends ProfileField {

  @Override
    MutableProfileField getProfileSubField( String physicalName );

  /**
   * Adds a new mutable sub field
   * 
   * @param profileSubField
   *          a ProfileField used to create the mutable sub field
   */
  public void addProfileSubField( ProfileField profileSubField );

  /**
   * Tries to return a mutable sub field identified by a physical name, if it does not exist, this method creates and
   * saves a new ProfileField with a physical and a logical name
   * 
   * @param physicalName
   *          an unique internal representation name for the new mutable sub field
   * @param logicalName
   *          a suitable name for the new mutable sub field to be shown to users, only used if the method creates a new
   *          MutableProfileField
   * @return a mutable sub field of this MutableProfileField
   */
    MutableProfileField getOrCreateProfileSubField( String physicalName, String logicalName );

  /**
   * @param name
   *          a value type name
   * @return a MutableProfileFieldValueType which holds the metrics for value type name of this MutableProfileField
   */
    MutableProfileFieldValueType getValueTypeMetrics( String name );

  /**
   * Adds a new ProfileFieldValueType
   * 
   * @param name
   *          the type value name of the ProfileFieldValueType to be inserted
   * @param profileFieldValueType
   *          a ProfileFieldValueType to be inserted
   */
    void putValueTypeMetrics( String name, ProfileFieldValueType profileFieldValueType );

  /**
   * Tries to return a MutableProfileFieldValueType identified by the value type name, if it does not exist, this method
   * creates and saves a new MutableProfileFieldValueType initialized with the value type name
   * 
   * @param name
   *          the type value name of the MutableProfileFieldValueType to be found or created
   * @return a MutableProfileFieldValueType of this MutableProfileField
   */
    MutableProfileFieldValueType getOrCreateValueTypeMetrics( String name );
}
