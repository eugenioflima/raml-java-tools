package org.raml.builder;

import amf.client.model.domain.AnyShape;
import amf.client.model.domain.Shape;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created. There, you have it.
 */
public abstract class TypeShapeBuilder<N extends AnyShape, B extends TypeShapeBuilder<N, B>> extends DomainElementBuilder<N, B> implements AnnotableBuilder<TypeShapeBuilder<N,B>> {

    private List<ExamplesBuilder> examples = new ArrayList<>();
    private List<AnnotationBuilder> annotations = new ArrayList<>();
    private List<FacetBuilder> facets = new ArrayList<>();


    private String description;

    private ExamplesBuilder example;
    private String name;

    @Override
    abstract protected N buildNodeLocally();

    public static ScalarShapeBuilder stringScalar() {
        return new ScalarShapeBuilder("http://www.w3.org/2001/XMLSchema#string");
    }

    public static ScalarShapeBuilder booleanScalar() {
        return new ScalarShapeBuilder("http://www.w3.org/2001/XMLSchema#boolean");
    }

    public static ScalarShapeBuilder longScalar() {
        return new ScalarShapeBuilder("http://www.w3.org/2001/XMLSchema#long");
    }

    public static ScalarShapeBuilder dateOnly() {
        return new ScalarShapeBuilder("http://www.w3.org/2001/XMLSchema#date");
    }

    public static ScalarShapeBuilder dateTime() {
        return new ScalarShapeBuilder("http://www.w3.org/2001/XMLSchema#dateTime");
    }

    public static ScalarShapeBuilder timeOnly() {
        return new ScalarShapeBuilder("http://www.w3.org/2001/XMLSchema#timeOnly");
    }

    public static ScalarShapeBuilder dateTimeOnly() {
        return new ScalarShapeBuilder("http://www.w3.org/2001/XMLSchema#dateTimeOnly");
    }

    public static FileShapeBuilder file() {
        return new FileShapeBuilder();
    }

    public static NilShapeBuilder nil() {
        return new NilShapeBuilder();
    }

//    static public TypeShapeBuilder simpleType(String type) {
//
//        return new NodeShapeBuilder();
//    }

    static public ArrayShapeBuilder arrayOf(TypeShapeBuilder builder) {

        return new ArrayShapeBuilder(builder);
    }

    /* for enums */
    static public EnumShapeBuilder enumeratedType() {

        // todo not hello
        return new EnumShapeBuilder();
    }


    static public NodeShapeBuilder inheritingObjectFromShapes(Shape... types) {

        return new NodeShapeBuilder(types);
    }

    static public UnionShapeBuilder unionShapeOf(Shape... types) {

        return new UnionShapeBuilder(types);
    }

    public static TypeShapeBuilder<AnyShape, AnyShapeBuilder> anyType() {
        return new AnyShapeBuilder("any");
    }

    @Override
    public TypeShapeBuilder withAnnotations(AnnotationBuilder... builders) {

        this.annotations.addAll(Arrays.asList(builders));
        return this;
    }


    public TypeShapeBuilder withExamples(ExamplesBuilder... properties) {

        this.example = null;
        this.examples.addAll(Arrays.asList(properties));
        return this;
    }

    public TypeShapeBuilder withExample(ExamplesBuilder example) {

        this.examples.clear();
        this.example = example;
        return this;
    }

    public TypeShapeBuilder withFacets(FacetBuilder... facetBuilders) {

        this.facets.addAll(Arrays.asList(facetBuilders));
        return this;
    }

    public TypeShapeBuilder description(String description) {

        this.description = description;
        return this;
    }


    protected void commonNodeInfo(N node) {

        super.commonNodeInfo(node);

        Optional.ofNullable(name).ifPresent(node::withName);

        if ( ! facets.isEmpty() ) {


//            KeyValueNodeImpl kvn = new KeyValueNodeImpl(new StringNodeImpl("facets"), new ObjectNodeImpl());
//            for (FacetBuilder facetBuilder : facets) {
//                kvn.getValue().addChild(facetBuilder.buildNode());
//            }
//
//            node.addChild(kvn);
        }

        if ( description != null ) {

            node.withDescription(description);
        }


        if ( ! annotations.isEmpty() ) {

//            for (AnnotationBuilder annotation : annotations) {
//                node.addChild(annotation.buildNode());
//            }
        }


        if ( ! examples.isEmpty() ) {

            node.withExamples(examples.stream().map(ExamplesBuilder::buildNode).collect(Collectors.toList()));
        }

        if ( example != null ) {

            node.withExamples(Collections.singletonList(example.buildNode()));
        }
    }

    public TypeShapeBuilder withFormat(PropertyValueBuilder format) {
        return null;
    }

    public B withName(String name) {
        this.name = name;
        return (B) this;
    }
}
