package org.raml.builder;

import amf.client.model.domain.DomainElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created. There, you have it.
 */
public class AnnotationTypeBuilder extends KeyValueNodeBuilder<AnnotationBuilder> implements NodeBuilder {

    private List<NodeBuilder> properties = new ArrayList<>();

    private AnnotationTypeBuilder(String name) {
        super(name);
    }

    static public AnnotationTypeBuilder annotationType(String name) {

        return new AnnotationTypeBuilder(name);
    }

    @Override
    public DomainElement buildNode() {

        KeyValueNode node = super.buildNode();

        ObjectNodeImpl valueNode = new ObjectNodeImpl();
        KeyValueNodeImpl kvn = new KeyValueNodeImpl(new StringNodeImpl("properties"), valueNode);
        node.getValue().addChild(0, kvn);

        for (NodeBuilder property : properties) {

            valueNode.addChild(property.buildNode());
        }

        return node;
    }

    public AnnotationTypeBuilder withProperty(NodeBuilder... properties) {

        this.properties.addAll(Arrays.asList(properties));
        return this;
    }

}
