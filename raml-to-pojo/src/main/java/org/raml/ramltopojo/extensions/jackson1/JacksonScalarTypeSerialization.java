/*
 * Copyright 2013-2017 (c) MuleSoft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.raml.ramltopojo.extensions.jackson1;

import amf.client.model.StrField;
import amf.client.model.domain.PropertyShape;
import amf.client.model.domain.ScalarShape;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import org.raml.ramltopojo.EventType;
import org.raml.ramltopojo.extensions.ObjectPluginContext;
import org.raml.ramltopojo.extensions.ObjectTypeHandlerPlugin;

import java.util.Optional;

/**
 * Created by Jean-Philippe Belanger on 1/8/17. Just potential zeroes and ones
 */
public class JacksonScalarTypeSerialization extends ObjectTypeHandlerPlugin.Helper {

  @Override
  public FieldSpec.Builder fieldBuilt(ObjectPluginContext objectPluginContext, PropertyShape typeDeclaration, FieldSpec.Builder builder, EventType eventType) {


    if ( typeDeclaration.range() instanceof ScalarShape) {
      ScalarShape propertyType = (ScalarShape) typeDeclaration.range();

      if ( "datetime-only".equals(propertyType.dataType().value())) {

        builder.addAnnotation(AnnotationSpec.builder(JsonFormat.class)
                .addMember("shape", "$T.STRING", JsonFormat.Shape.class)
                .addMember("pattern", "$S", "yyyy-MM-dd'T'HH:mm:ss").build());
      }

      if ( "time-only".equals(propertyType.dataType().value())) {

        builder.addAnnotation(AnnotationSpec.builder(JsonFormat.class)
                .addMember("shape", "$T.STRING", JsonFormat.Shape.class)
                .addMember("pattern", "$S", "HH:mm:ss").build());
      }

      if ( "date".equals(propertyType.dataType().value())) {

        builder.addAnnotation(AnnotationSpec.builder(JsonFormat.class)
                .addMember("shape", "$T.STRING", JsonFormat.Shape.class)
                .addMember("pattern", "$S", "yyyy-MM-dd").build());
      }

      if ( "datetime".equals(propertyType.dataType().value())) {

        // TODO: do better
        Optional<String> format = Optional.ofNullable(propertyType.format()).map(StrField::value);
        if (format.isPresent() && "rfc2616".equals(format.get())) {

          builder.addAnnotation(AnnotationSpec.builder(JsonFormat.class)
                  .addMember("shape", "$T.STRING", JsonFormat.Shape.class)
                  .addMember("pattern", "$S", "EEE, dd MMM yyyy HH:mm:ss z").build());
        } else {
          builder.addAnnotation(AnnotationSpec.builder(JsonFormat.class)
                  .addMember("shape", "$T.STRING", JsonFormat.Shape.class)
                  .addMember("pattern", "$S", "yyyy-MM-dd'T'HH:mm:ssZ").build());
        }
      }
    }

    return builder;
  }
}
