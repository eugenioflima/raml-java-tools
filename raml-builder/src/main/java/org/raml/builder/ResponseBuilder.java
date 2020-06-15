package org.raml.builder;

import amf.client.model.domain.DomainElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created. There, you have it.
 */
public class ResponseBuilder extends KeyValueNodeBuilder<ResponseBuilder> implements NodeBuilder, AnnotableBuilder<ResponseBuilder> {

    private List<PayloadBuilder> bodies = new ArrayList<>();
    private List<AnnotationBuilder> annotations = new ArrayList<>();
    private String description;

    private ResponseBuilder(int code) {
        super((long) code);
    }

    static public ResponseBuilder response(int code) {

        return new ResponseBuilder(code);
    }

    public ResponseBuilder withBodies(PayloadBuilder... builder) {

        this.bodies.addAll(Arrays.asList(builder));
        return this;
    }

    @Override
    public ResponseBuilder withAnnotations(AnnotationBuilder... builders) {

        this.annotations.addAll(Arrays.asList(builders));
        return this;
    }


    @Override
    public DomainElement buildNode() {
        KeyValueNode node =  super.buildNode();

        addProperty(node.getValue(), "descrption", description);

        if ( ! bodies.isEmpty()) {
            ObjectNodeImpl valueNode = new ObjectNodeImpl();
            KeyValueNodeImpl bkvn = new KeyValueNodeImpl(new StringNodeImpl("body"), valueNode);
            node.getValue().addChild(bkvn);

            for (PayloadBuilder body : bodies) {
                valueNode.addChild(body.buildNode());
            }
        }

        if ( ! annotations.isEmpty() ) {

            for (AnnotationBuilder annotation : annotations) {
                node.getValue().addChild(annotation.buildNode());
            }
        }

        return node;

    }

    public ResponseBuilder description(String description) {
        this.description = description;
        return this;
    }
}
