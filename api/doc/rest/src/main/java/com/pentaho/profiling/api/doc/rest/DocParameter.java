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

package com.pentaho.profiling.api.doc.rest;

/**
 * Created by bryan on 4/6/15.
 */
public class DocParameter {
  private String name;
  private String comment;

  public DocParameter() {
    this( null, null );
  }

  public DocParameter( String name, String comment ) {
    this.name = name;
    this.comment = comment;
  }

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getComment() {
    return comment;
  }

  public void setComment( String comment ) {
    this.comment = comment;
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

    DocParameter that = (DocParameter) o;

    if ( name != null ? !name.equals( that.name ) : that.name != null ) {
      return false;
    }
    return !( comment != null ? !comment.equals( that.comment ) : that.comment != null );

  }

  @Override public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + ( comment != null ? comment.hashCode() : 0 );
    return result;
  }

  @Override public String toString() {
    return "DocParameter{" +
      "name='" + name + '\'' +
      ", comment='" + comment + '\'' +
      '}';
  }
  //CHECKSTYLE:OperatorWrap:ON
}