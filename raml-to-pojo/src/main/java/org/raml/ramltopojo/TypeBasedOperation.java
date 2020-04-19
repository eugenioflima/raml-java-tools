/*
 * Copyright 2013-2018 (c) MuleSoft, Inc.
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
package org.raml.ramltopojo;

import amf.client.model.domain.*;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created. There, you have it.
 */
public interface TypeBasedOperation<T> {

  static <T> T run(Shape shape, TypeBasedOperation<T> operation) {

    return run((AnyShape)shape, operation);
  }

  static <T> T run(AnyShape anyShape, TypeBasedOperation<T> operation) {
    if (anyShape instanceof NodeShape) {
      return operation.on((NodeShape) anyShape);
    }

    if (anyShape instanceof UnionShape) {
      return operation.on((UnionShape) anyShape);
    }

    if (anyShape instanceof ArrayShape) {
      return operation.on((ArrayShape) anyShape);
    }

    if (anyShape instanceof FileShape) {
      return operation.on((FileShape) anyShape);
    }

    if (anyShape instanceof ScalarShape) {
      return operation.on((ScalarShape) anyShape);
    }

    if (anyShape instanceof SchemaShape) {
      return operation.on((SchemaShape) anyShape);
    }

    if (anyShape instanceof NilShape) {
      return operation.on((NilShape) anyShape);
    }

    return operation.on(anyShape);
  }

  class OptionalDefault<T> extends Default<Optional<T>> {

    public OptionalDefault() {
      super((x) -> Optional.empty());
    }
  }

  class Default<T> implements TypeBasedOperation<T> {

    private final Function<AnyShape, T> defaultOperation;

    public Default(Function<AnyShape, T> defaultOperation) {
      this.defaultOperation = defaultOperation;
    }

    @Override
    public T on(AnyShape anyShape) {
      return defaultOperation.apply(anyShape);
    }

    @Override
    public T on(NodeShape anyShape) {
      return defaultOperation.apply(anyShape);
    }

    @Override
    public T on(ArrayShape anyShape) {
      return defaultOperation.apply(anyShape);
    }

    @Override
    public T on(UnionShape anyShape) {
      return defaultOperation.apply(anyShape);
    }

    @Override
    public T on(FileShape anyShape) {
      return defaultOperation.apply(anyShape);
    }

    @Override
    public T on(ScalarShape anyShape) {
      return defaultOperation.apply(anyShape);
    }

    @Override
    public T on(SchemaShape schemaShape) {
      return defaultOperation.apply(schemaShape);
    }

    @Override
    public T on(NilShape nilShape) {
      return defaultOperation.apply(nilShape);
    }
  }

  T on(AnyShape anyShape);

  T on(NodeShape anyShape);

  T on(ArrayShape anyShape);

  T on(UnionShape anyShape);

  T on(FileShape anyShape);

  T on(ScalarShape anyShape);

  T on(SchemaShape schemaShape);

  T on(NilShape nilShape);
}
