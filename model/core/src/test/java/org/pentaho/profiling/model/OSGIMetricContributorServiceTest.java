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

import org.junit.Before;
import org.junit.Test;
import org.pentaho.profiling.api.MutableProfileFieldValueType;
import org.pentaho.profiling.api.MutableProfileStatus;
import org.pentaho.profiling.api.ProfileFieldProperty;
import org.pentaho.profiling.api.ProfileFieldValueType;
import org.pentaho.profiling.api.ProfileStatus;
import org.pentaho.profiling.api.action.ProfileActionException;
import org.pentaho.profiling.api.metrics.MetricContributor;
import org.pentaho.profiling.api.metrics.MetricManagerContributor;
import org.pentaho.profiling.api.metrics.MetricMergeException;
import org.pentaho.profiling.api.metrics.field.DataSourceFieldValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by jpereira on 9/2/15
 */
public class OSGIMetricContributorServiceTest {

  private OSGIMetricContributorService oSGIMetricContributorService;

  @Before
  public void setup() {
    oSGIMetricContributorService = new OSGIMetricContributorService();
  }

  @Test
  public void testMetricContributorAddedRemovedList() {
    MetricContributor testMetricContributor = new TestMetricContributor();
    oSGIMetricContributorService.metricContributorAdded( testMetricContributor );
    assertEquals( new ArrayList<MetricContributor>( Arrays.asList( testMetricContributor.clone() ) ),
        oSGIMetricContributorService.getMetricContributorList() );

    oSGIMetricContributorService.metricContributorRemoved( testMetricContributor );
    assertEquals( new ArrayList<MetricContributor>(),
        oSGIMetricContributorService.getMetricContributorList() );
  }

  @Test
  public void testMetricManagerContributorAddedRemovedList() {
    MetricManagerContributor testMetricManagerContributor = new TestMetricManagerContributor();
    oSGIMetricContributorService.metricManagerContributorAdded( testMetricManagerContributor );
    assertEquals( new ArrayList<MetricManagerContributor>( Arrays.asList( testMetricManagerContributor.clone() ) ),
        oSGIMetricContributorService.getMetricManagerContributorList() );

    oSGIMetricContributorService.metricManagerContributorRemoved( testMetricManagerContributor );
    assertEquals( new ArrayList<MetricManagerContributor>(),
        oSGIMetricContributorService.getMetricManagerContributorList() );
  }

  public static class TestMetricContributor implements MetricContributor {
    private String param1;
    private String name;

    public TestMetricContributor( String param1, String name ) {
      this.param1 = param1;
      this.name = name;
    }

    public TestMetricContributor() {
      this( "initialValue", null );
    }

    public String getParam1() {
      return param1;
    }

    public void setParam1( String param1 ) {
      this.param1 = param1;
    }
    @Override
    public void processFields( MutableProfileStatus mutableProfileStatus, List<DataSourceFieldValue> values )
      throws ProfileActionException {

    }

    @Override
    public void setDerived( MutableProfileStatus mutableProfileStatus ) throws ProfileActionException {

    }

    @Override
    public void merge( MutableProfileStatus into, ProfileStatus from ) throws MetricMergeException {

    }

    @Override
    public List<ProfileFieldProperty> getProfileFieldProperties() {
      return null;
    }

    @Override public TestMetricContributor clone() {
      return new TestMetricContributor( param1, name );
    }

    @Override public boolean equals( Object o ) {
      if ( this == o ) {
        return true;
      }
      if ( o == null || getClass() != o.getClass() ) {
        return false;
      }

      TestMetricContributor that = (TestMetricContributor) o;

      if ( param1 != null ? !param1.equals( that.param1 ) : that.param1 != null ) {
        return false;
      }
      return !( name != null ? !name.equals( that.name ) : that.name != null );

    }

    @Override public int hashCode() {
      int result = param1 != null ? param1.hashCode() : 0;
      result = 31 * result + ( name != null ? name.hashCode() : 0 );
      return result;
    }

  }

  public static class TestMetricManagerContributor implements MetricManagerContributor {
    private String param1;
    private String name;

    public TestMetricManagerContributor( String param1, String name ) {
      this.param1 = param1;
      this.name = name;
    }

    public TestMetricManagerContributor() {
      this( "initialValue", null );
    }

    public String getParam1() {
      return param1;
    }

    public void setParam1( String param1 ) {
      this.param1 = param1;
    }

    @Override public String getName() {
      return name;
    }

    @Override public void setName( String name ) {
      this.name = name;
    }

    @Override public Set<String> supportedTypes() {
      return new HashSet<String>();
    }

    @Override
    public void process( MutableProfileFieldValueType dataSourceMetricManager,
                         DataSourceFieldValue dataSourceFieldValue )
      throws ProfileActionException {

    }

    @Override public void merge( MutableProfileFieldValueType into, ProfileFieldValueType from )
      throws MetricMergeException {

    }

    @Override public void setDerived( MutableProfileFieldValueType dataSourceMetricManager )
      throws ProfileActionException {

    }

    @Override public List<ProfileFieldProperty> profileFieldProperties() {
      return null;
    }

    @Override public TestMetricManagerContributor clone() {
      return new TestMetricManagerContributor( param1, name );
    }

    @Override public boolean equals( Object o ) {
      if ( this == o ) {
        return true;
      }
      if ( o == null || getClass() != o.getClass() ) {
        return false;
      }

      TestMetricManagerContributor that = (TestMetricManagerContributor) o;

      if ( param1 != null ? !param1.equals( that.param1 ) : that.param1 != null ) {
        return false;
      }
      return !( name != null ? !name.equals( that.name ) : that.name != null );

    }

    @Override public int hashCode() {
      int result = param1 != null ? param1.hashCode() : 0;
      result = 31 * result + ( name != null ? name.hashCode() : 0 );
      return result;
    }
  }
}
