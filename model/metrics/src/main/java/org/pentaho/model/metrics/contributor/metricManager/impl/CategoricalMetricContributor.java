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

import org.pentaho.model.metrics.contributor.Constants;
import org.pentaho.model.metrics.contributor.metricManager.impl.metrics.CategoricalHolder;
import org.pentaho.profiling.api.MessageUtils;
import org.pentaho.profiling.api.MutableProfileFieldValueType;
import org.pentaho.profiling.api.ProfileFieldProperty;
import org.pentaho.profiling.api.ProfileFieldValueType;
import org.pentaho.profiling.api.action.ProfileActionException;
import org.pentaho.profiling.api.metrics.MetricContributorUtils;
import org.pentaho.profiling.api.metrics.MetricManagerContributor;
import org.pentaho.profiling.api.metrics.MetricMergeException;
import org.pentaho.profiling.api.metrics.field.DataSourceFieldValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mhall on 28/01/15.
 * Modified by jpereira.
 */
public class CategoricalMetricContributor extends BaseMetricManagerContributor implements MetricManagerContributor {
  public static final String KEY_PATH =
    MessageUtils.getId( Constants.KEY, CategoricalMetricContributor.class );
  public static final String SIMPLE_NAME = CategoricalMetricContributor.class.getSimpleName();
  public static final ProfileFieldProperty CATEGORICAL_FIELD = MetricContributorUtils
    .createMetricProperty( KEY_PATH, SIMPLE_NAME, SIMPLE_NAME,
      MetricContributorUtils.CATEGORICAL );
  private int distinctAllowed = 100;

  public int getDistinctAllowed() {
    return distinctAllowed;
  }

  public void setDistinctAllowed( int distinctAllowed ) {
    this.distinctAllowed = distinctAllowed;
  }

  private CategoricalHolder getOrCreateCategoricalHolder( MutableProfileFieldValueType mutableProfileFieldValueType ) {
    CategoricalHolder result = (CategoricalHolder) mutableProfileFieldValueType.getValueTypeMetrics( SIMPLE_NAME );
    if ( result == null ) {
      result = new CategoricalHolder( distinctAllowed, new HashMap<String, Long>() );
      mutableProfileFieldValueType.setValueTypeMetrics( SIMPLE_NAME, result );
    }
    return result;
  }

  @Override public Set<String> supportedTypes() {
    HashSet<String> types = new HashSet<String>( Arrays.asList( String.class.getCanonicalName() ) );
    types.addAll( NumericMetricContributor.getTypesStatic() );
    return types;
  }

  @Override
  public void process( MutableProfileFieldValueType mutableProfileFieldValueType,
                       DataSourceFieldValue dataSourceFieldValue )
    throws ProfileActionException {
    getOrCreateCategoricalHolder( mutableProfileFieldValueType )
      .addEntry( String.valueOf( dataSourceFieldValue.getFieldValue() ) );
  }

  @Override public void merge( MutableProfileFieldValueType mutableProfileFieldValueType,
                               ProfileFieldValueType profileFieldValueType )
    throws MetricMergeException {
    CategoricalHolder from = (CategoricalHolder) profileFieldValueType.getValueTypeMetrics( SIMPLE_NAME );
    if ( from != null ) {
      CategoricalHolder into = getOrCreateCategoricalHolder( mutableProfileFieldValueType );
      into.add( from );
    }
  }

  @Override
  public CategoricalMetricContributor clone() {
    return new CategoricalMetricContributor();
  }

  @Override public List<ProfileFieldProperty> profileFieldProperties() {
    return Arrays.asList( CATEGORICAL_FIELD );
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

    CategoricalMetricContributor that = (CategoricalMetricContributor) o;

    return distinctAllowed == that.distinctAllowed;

  }

  @Override public int hashCode() {
    return distinctAllowed;
  }

  @Override public String toString() {
    return "CategoricalMetricContributor{" +
      "distinctAllowed=" + distinctAllowed +
      "} " + super.toString();
  }
  //CHECKSTYLE:OperatorWrap:ON
}
