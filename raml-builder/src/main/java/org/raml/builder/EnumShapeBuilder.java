package org.raml.builder;

import amf.client.model.domain.DataNode;
import amf.client.model.domain.ScalarNode;
import amf.client.model.domain.ScalarShape;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created. There, you have it.
 */
public class EnumShapeBuilder extends TypeShapeBuilder<ScalarShape, EnumShapeBuilder> {

    private final String type;
    private List<DataNode> enumValues;

    public EnumShapeBuilder(String type) {
        this.type = type;
    }

    public EnumShapeBuilder enumValues(String... enumValues) {

        this.enumValues = Arrays.stream(enumValues)
                .map(x -> new ScalarNode(x, "http://www.w3.org/2001/XMLSchema#string"))
                .collect(Collectors.toList());
        return this;
    }

    public EnumShapeBuilder enumValues(long... enumValues) {

        this.enumValues = Arrays.stream(enumValues)
                .mapToObj(Long::toString)
                .map(x -> new ScalarNode(x, "http://www.w3.org/2001/XMLSchema#long"))
                .collect(Collectors.toList());
        return this;
    }

    public EnumShapeBuilder enumValues(boolean... enumValues) {

        this.enumValues = IntStream.range(0, enumValues.length)
                .mapToObj(idx -> enumValues[idx])
                .map(x -> Boolean.toString(x))
                .map(x -> new ScalarNode(x, "http://www.w3.org/2001/XMLSchema#boolean"))
                .collect(Collectors.toList());
        return this;
    }


    @Override
    public ScalarShape buildNode() {

        ScalarShape shape = new ScalarShape();
        shape.withDataType("http://www.w3.org/2001/XMLSchema#boolean"); // todo fix me!
        commonNodeInfo(shape);
        if ( enumValues != null ) {

            shape.withValues(enumValues);
        } else {

        }

        return shape;
    }

    public String id() {

        return "[" + type + "]";
    }
}
