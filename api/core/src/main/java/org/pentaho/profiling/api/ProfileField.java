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

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ProfileField specifies the field behavior contained in a profile status object. A field represents a field in a data
 * table, for instance for an identification number it contains its name and can contain statistical information about
 * the values. The ProfileField should allow one to obtain information such as, its name, its properties, its
 * ProfieFieldValueType which contain metric holders per value type. Additionally, it can contain other Profile Fields
 * which are a kind of sub fields at the disposal of metric contributors.
 * <p>
 * Created by bryan on 4/30/15.
 * 
 * @author bryan
 * @author Joao L. M. Pereira (Joao.Pereira{[at]}pentaho.com)
 * @version 1.1
 */
public interface ProfileField extends PublicCloneable {

  /**
   * 
   * @return an internal representation name for this ProfileField
   */
    String getPhysicalName();

  /**
   * 
   * @return a suitable name for this ProfileField to be shown to users
   */
    String getLogicalName();

  /**
   * TODO JavaDoc
   * @return
   */
  Map<String, String> getProperties();

  /**
   * 
   * @return a list of ProfileFieldValueType. A ProfileFieldValueType contains the statistical values per value type
   *         evaluated in this field.
   */
    List<ProfileFieldValueType> getTypes();

  /**
   * 
   * @param name
   *          the type of the field values
   * @return the ProfileFieldValueType containing name as a value type
   */
    ProfileFieldValueType getType( String name );

  /**
   * The value types evaluated for this field
   * 
   * @return a set of value types evaluated
   */
    Set<String> typeKeys();

  /**
   * 
   * @return the list of sub fields of this ProfileField
   */
    List<ProfileField> getProfileSubFields();

  /**
   * 
   * @param name
   *          the sub profile physical name
   * @return the sub ProfileField of this ProfileField containing type value name
   */
    ProfileField getProfileSubField( String name );
}
