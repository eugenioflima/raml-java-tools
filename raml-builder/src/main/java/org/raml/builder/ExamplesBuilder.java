package org.raml.builder;

import amf.client.model.domain.DomainElement;
import org.raml.yagi.framework.nodes.KeyValueNode;
import org.raml.yagi.framework.nodes.KeyValueNodeImpl;
import org.raml.yagi.framework.nodes.ObjectNodeImpl;
import org.raml.yagi.framework.nodes.StringNodeImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created. There, you have it.
 */
public class ExamplesBuilder extends KeyValueNodeBuilder<ExamplesBuilder> implements SupportsProperties<ExamplesBuilder> {

    private boolean strict = true;
    private List<PropertyValueBuilder> propertyValues = new ArrayList<>();

    private ExamplesBuilder(String name) {
        super(name);
    }

    static public ExamplesBuilder example(String name) {

        return new ExamplesBuilder(name);
    }

    public static ExamplesBuilder singleExample() {
        return new ExamplesBuilder(null);
    }

    public ExamplesBuilder strict(boolean strict) {

        this.strict = strict;
        return this;
    }

    public ExamplesBuilder withNoProperties() {

        this.propertyValues.clear();
        return this;
    }

    public ExamplesBuilder withPropertyValues(PropertyValueBuilder... values) {

        this.propertyValues.addAll(Arrays.asList(values));
        return this;
    }

    public ExamplesBuilder withPropertyValue(PropertyValueBuilder value) {

        this.propertyValues.add(value);
        return this;
    }

    @Override
    public DomainElement buildNode() {

        KeyValueNode node = super.buildNode();
        node.getValue().addChild(new KeyValueNodeImpl(new StringNodeImpl("strict"), new BooleanNode(strict)));

        if ( ! propertyValues.isEmpty() ) {

            KeyValueNodeImpl kvn = new KeyValueNodeImpl(new StringNodeImpl("value"), new ObjectNodeImpl());
            for (PropertyValueBuilder example : propertyValues) {
                kvn.getValue().addChild(example.buildNode());
            }

            node.getValue().addChild(kvn);
        } else {

            KeyValueNodeImpl kvn = new KeyValueNodeImpl(new StringNodeImpl("value"), new ObjectNodeImpl());
            node.getValue().addChild(kvn);
        }

        return node;
    }
}
