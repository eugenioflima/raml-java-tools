package org.raml.builder;

import amf.client.model.domain.NodeShape;
import amf.client.model.domain.Shape;
import com.google.common.base.Suppliers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created. There, you have it.
 */
public class NodeShapeBuilder extends TypeShapeBuilder<NodeShape, NodeShapeBuilder> {


    private Shape[] types;

    private final List<PropertyShapeBuilder> properties = new ArrayList<>();

    private final Supplier<NodeShape> response;

    public NodeShapeBuilder(Shape... types) {

        this.types = types;
        this.response = Suppliers.memoize(this::calculateNodeShape);
    }

    private static NodeShape doInheritance(Shape t) {
        NodeShape nodeShape = new NodeShape();
        nodeShape.withLinkTarget(t);
        nodeShape.withLinkLabel(t.name().value());

        return nodeShape;
    }


    public NodeShapeBuilder withProperty(PropertyShapeBuilder... properties) {

        this.properties.addAll(Arrays.asList(properties));
        return this;
    }


    @Override
    protected NodeShape buildNodeLocally() {

        return response.get();
    }

    public NodeShape calculateNodeShape() {
        NodeShape nodeShape = new NodeShape();
        commonNodeInfo(nodeShape);
        nodeShape.withName("anonymous");

        if ( types != null && types.length != 0) {
                //Not sure....
                nodeShape.withInherits(Arrays.stream(types).map(NodeShapeBuilder::doInheritance).collect(Collectors.toList()));
        }

        if ( ! properties.isEmpty() ) {

            nodeShape.withProperties(properties.stream().map(PropertyShapeBuilder::buildNode).collect(Collectors.toList()));
        }

        return nodeShape;
    }

}
