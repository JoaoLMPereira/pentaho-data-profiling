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

package org.pentaho.model.metrics.contributor.metricManager.impl;

import org.pentaho.profiling.api.MutableProfileFieldValueType;
import org.pentaho.profiling.api.ProfileFieldProperty;
import org.pentaho.profiling.api.ProfileFieldValueType;
import org.pentaho.profiling.api.action.ProfileActionException;
import org.pentaho.profiling.api.metrics.MetricManagerContributor;
import org.pentaho.profiling.api.metrics.MetricMergeException;
import org.pentaho.profiling.api.metrics.field.DataSourceFieldValue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mhall on 27/01/15.
 * Modified by jpereira
 */
public class StringLengthMetricContributor extends BaseMetricManagerContributor implements MetricManagerContributor {
  private final NumericMetricContributor numericMetricManagerContributor;

  public StringLengthMetricContributor() {
    this( new NumericMetricContributor() );
  }

  public StringLengthMetricContributor( NumericMetricContributor numericMetricManagerContributor ) {
    this.numericMetricManagerContributor = numericMetricManagerContributor;
  }

  @Override public Set<String> supportedTypes() {
    return new HashSet<String>( Arrays.asList( String.class.getCanonicalName() ) );
  }

  @Override
  public void process( MutableProfileFieldValueType mutableProfileFieldValueType,
                       DataSourceFieldValue dataSourceFieldValue )
    throws ProfileActionException {
    numericMetricManagerContributor
      .processValue( mutableProfileFieldValueType, ( (String) dataSourceFieldValue.getFieldValue() ).length() );
  }

  @Override public void merge( MutableProfileFieldValueType into, ProfileFieldValueType from )
    throws MetricMergeException {
    numericMetricManagerContributor.merge( into, from );
  }

  @Override public void setDerived( MutableProfileFieldValueType mutableProfileFieldValueType )
    throws ProfileActionException {
    numericMetricManagerContributor.setDerived( mutableProfileFieldValueType );
  }

  @Override public List<ProfileFieldProperty> profileFieldProperties() {
    return NumericMetricContributor.getProfileFieldPropertiesStatic();
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

    StringLengthMetricContributor that = (StringLengthMetricContributor) o;

    return !( numericMetricManagerContributor != null ?
      !numericMetricManagerContributor.equals( that.numericMetricManagerContributor ) :
      that.numericMetricManagerContributor != null );

  }

  @Override public int hashCode() {
    return numericMetricManagerContributor != null ? numericMetricManagerContributor.hashCode() : 0;
  }

  @Override public String toString() {
    return "StringLengthMetricContributor{" + numericMetricManagerContributor + "}";
  }

  @Override
  public StringLengthMetricContributor clone() {
    return new StringLengthMetricContributor();
  }
  //CHECKSTYLE:OperatorWrap:ON
}
