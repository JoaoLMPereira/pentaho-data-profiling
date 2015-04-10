/*!
 * PENTAHO CORPORATION PROPRIETARY AND CONFIDENTIAL
 *
 * Copyright 2002 - 2015 Pentaho Corporation (Pentaho). All rights reserved.
 *
 * NOTICE: All information including source code contained herein is, and
 * remains the sole property of Pentaho and its licensors. The intellectual
 * and technical concepts contained herein are proprietary and confidential
 * to, and are trade secrets of Pentaho and may be covered by U.S. and foreign
 * patents, or patents in process, and are protected by trade secret and
 * copyright laws. The receipt or possession of this source code and/or related
 * information does not convey or imply any rights to reproduce, disclose or
 * distribute its contents, or to manufacture, use, or sell anything that it
 * may describe, in whole or in part. Any reproduction, modification, distribution,
 * or public display of this information without the express written authorization
 * from Pentaho is strictly prohibited and in violation of applicable laws and
 * international treaties. Access to the source code contained herein is strictly
 * prohibited to anyone except those individuals and entities who have executed
 * confidentiality and non-disclosure agreements or other agreements with Pentaho,
 * explicitly covering such access.
 */

package com.pentaho.profiling.rest.doc;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bryan on 4/6/15.
 */
public class EndpointDocumentation {
  private String path;
  private String method;
  private String name;
  private String description;
  private List<EndpointParameter> endpointQueryParameters;
  private List<EndpointParameter> endpointPathParameters;
  private Integer successResponseCode;
  private List<EndpointReturnCode> errorCodes;
  private List<EndpointExample> endpointExamples;
  private EndpointParameter endpointBodyParameter;
  private EndpointResponse endpointResponse;

  public EndpointDocumentation() {
    this( null, null, null, null, new ArrayList<EndpointParameter>(), new ArrayList<EndpointParameter>(),
      null, new ArrayList<EndpointReturnCode>(), null, null, null );
  }

  public EndpointDocumentation( String path, String method, String name, String description,
                                List<EndpointParameter> endpointQueryParameters,
                                List<EndpointParameter> endpointPathParameters,
                                Integer successResponseCode, List<EndpointReturnCode> errorCodes,
                                List<EndpointExample> endpointExamples,
                                EndpointParameter endpointBodyParameter, EndpointResponse endpointResponse ) {
    this.path = path;
    this.method = method;
    this.name = name;
    this.description = description;
    this.endpointQueryParameters = endpointQueryParameters;
    this.endpointPathParameters = endpointPathParameters;
    this.successResponseCode = successResponseCode;
    this.errorCodes = errorCodes;
    this.endpointExamples = endpointExamples;
    this.endpointBodyParameter = endpointBodyParameter;
    this.endpointResponse = endpointResponse;
  }

  public String getPath() {
    return path;
  }

  public void setPath( String path ) {
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription( String description ) {
    this.description = description;
  }

  public List<EndpointParameter> getEndpointQueryParameters() {
    return endpointQueryParameters;
  }

  public void setEndpointQueryParameters(
    List<EndpointParameter> endpointQueryParameters ) {
    this.endpointQueryParameters = endpointQueryParameters;
  }

  public List<EndpointReturnCode> getErrorCodes() {
    return errorCodes;
  }

  public void setErrorCodes(
    List<EndpointReturnCode> errorCodes ) {
    this.errorCodes = errorCodes;
  }

  public List<EndpointParameter> getEndpointPathParameters() {
    return endpointPathParameters;
  }

  public void setEndpointPathParameters(
    List<EndpointParameter> endpointPathParameters ) {
    this.endpointPathParameters = endpointPathParameters;
  }

  public EndpointParameter getEndpointBodyParameter() {
    return endpointBodyParameter;
  }

  public void setEndpointBodyParameter( EndpointParameter endpointBodyParameter ) {
    this.endpointBodyParameter = endpointBodyParameter;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod( String method ) {
    this.method = method;
  }

  public List<EndpointExample> getEndpointExamples() {
    return endpointExamples;
  }

  public void setEndpointExamples( List<EndpointExample> endpointExamples ) {
    this.endpointExamples = endpointExamples;
  }

  public Integer getSuccessResponseCode() {
    return successResponseCode;
  }

  public void setSuccessResponseCode( Integer successResponseCode ) {
    this.successResponseCode = successResponseCode;
  }

  public EndpointResponse getEndpointResponse() {
    return endpointResponse;
  }

  public void setEndpointResponse( EndpointResponse endpointResponse ) {
    this.endpointResponse = endpointResponse;
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

    EndpointDocumentation that = (EndpointDocumentation) o;

    if ( path != null ? !path.equals( that.path ) : that.path != null ) {
      return false;
    }
    if ( method != null ? !method.equals( that.method ) : that.method != null ) {
      return false;
    }
    if ( name != null ? !name.equals( that.name ) : that.name != null ) {
      return false;
    }
    if ( description != null ? !description.equals( that.description ) : that.description != null ) {
      return false;
    }
    if ( endpointQueryParameters != null ? !endpointQueryParameters.equals( that.endpointQueryParameters ) :
      that.endpointQueryParameters != null ) {
      return false;
    }
    if ( endpointPathParameters != null ? !endpointPathParameters.equals( that.endpointPathParameters ) :
      that.endpointPathParameters != null ) {
      return false;
    }
    if ( successResponseCode != null ? !successResponseCode.equals( that.successResponseCode ) :
      that.successResponseCode != null ) {
      return false;
    }
    if ( errorCodes != null ? !errorCodes.equals( that.errorCodes ) : that.errorCodes != null ) {
      return false;
    }
    if ( endpointExamples != null ? !endpointExamples.equals( that.endpointExamples ) :
      that.endpointExamples != null ) {
      return false;
    }
    if ( endpointBodyParameter != null ? !endpointBodyParameter.equals( that.endpointBodyParameter ) :
      that.endpointBodyParameter != null ) {
      return false;
    }
    return !( endpointResponse != null ? !endpointResponse.equals( that.endpointResponse ) :
      that.endpointResponse != null );

  }

  @Override public int hashCode() {
    int result = path != null ? path.hashCode() : 0;
    result = 31 * result + ( method != null ? method.hashCode() : 0 );
    result = 31 * result + ( name != null ? name.hashCode() : 0 );
    result = 31 * result + ( description != null ? description.hashCode() : 0 );
    result = 31 * result + ( endpointQueryParameters != null ? endpointQueryParameters.hashCode() : 0 );
    result = 31 * result + ( endpointPathParameters != null ? endpointPathParameters.hashCode() : 0 );
    result = 31 * result + ( successResponseCode != null ? successResponseCode.hashCode() : 0 );
    result = 31 * result + ( errorCodes != null ? errorCodes.hashCode() : 0 );
    result = 31 * result + ( endpointExamples != null ? endpointExamples.hashCode() : 0 );
    result = 31 * result + ( endpointBodyParameter != null ? endpointBodyParameter.hashCode() : 0 );
    result = 31 * result + ( endpointResponse != null ? endpointResponse.hashCode() : 0 );
    return result;
  }

  @Override public String toString() {
    return "EndpointDocumentation{" +
      "path='" + path + '\'' +
      ", method='" + method + '\'' +
      ", name='" + name + '\'' +
      ", description='" + description + '\'' +
      ", endpointQueryParameters=" + endpointQueryParameters +
      ", endpointPathParameters=" + endpointPathParameters +
      ", successResponseCode=" + successResponseCode +
      ", errorCodes=" + errorCodes +
      ", endpointExamples=" + endpointExamples +
      ", endpointBodyParameter=" + endpointBodyParameter +
      ", endpointResponse=" + endpointResponse +
      '}';
  }
  //CHECKSTYLE:OperatorWrap:ON
}