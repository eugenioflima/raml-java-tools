package org.raml.builder;

import amf.client.model.domain.NodeShape;
import amf.client.model.domain.PropertyShape;
import amf.client.model.domain.ScalarShape;
import amf.client.model.domain.UnionShape;
import org.junit.Test;
import webapi.Raml10;
import webapi.WebApiDocument;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.raml.builder.RamlDocumentBuilder.document;

/**
 * Created. There, you have it.
 */
public class TypeBuilderTest {

    @Test
    public void goddamit() throws ExecutionException, InterruptedException {


        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun").buildNode();

        NodeShape parent = new NodeShape();
        PropertyShape property = new PropertyShape();
        property.withName("prop1");
        property.withRange(new ScalarShape().withDataType("http://www.w3.org/2001/XMLSchema#string"));
        parent.withName("parent");
        parent.withProperties(Collections.singletonList(property));
        api.withDeclares(Arrays.asList( new NodeShape().withInherits(Collections.singletonList(parent)).withName("foo"), parent));

//
//        ValidationReport s = Raml10.validate(api).get();
//        if (!s.conforms()) {
//            throw new ModelBuilderException(s);
//        }

        System.err.println(Raml10.generateString(api).get());
    }

    @Test
    public void justATypeMam() {

        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun")
                .withTypes(
                        DeclaredShapeBuilder.typeDeclaration("Mom").ofType(ScalarShapeBuilder.stringScalar())
                )
                .buildModel();

        assertEquals("Mom", ((ScalarShape)api.declares().get(0)).name().value());
        assertTrue(((ScalarShape)api.declares().get(0)).dataType().value().contains("string"));
    }

    @Test
    public void simpleType() {
        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun")
                .withTypes(
                        DeclaredShapeBuilder.typeDeclaration("Mom")
                                .ofType(ScalarShapeBuilder.booleanScalar())
                )
                .buildModel();

        assertEquals("Mom", ((ScalarShape)api.declares().get(0)).name().value());
        assertTrue(((ScalarShape)api.declares().get(0)).dataType().value().contains("boolean"));
    }

    // the absolutely stupidest type ever.
    @Test
    public void enumeratedBoolean() {

        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun")
                .withTypes(
                        DeclaredShapeBuilder.typeDeclaration("Mom").ofType(EnumShapeBuilder.enumeratedType().enumValues(true, false))
                )
                .buildModel();

        assertEquals("Mom", ((ScalarShape)api.declares().get(0)).name().value());
        assertTrue(((ScalarShape)api.declares().get(0)).dataType().value().contains("boolean"));
        assertEquals(2, ((ScalarShape) api.declares().get(0)).values().size());
    }

    @Test
    public void enumeratedInteger() {

        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun")
                .withTypes(
                        DeclaredShapeBuilder.typeDeclaration("Mom").ofType(EnumShapeBuilder.enumeratedType().enumValues(1, 2, 3))
                )
                .buildModel();

        assertEquals("Mom", ((ScalarShape)api.declares().get(0)).name().value());
        assertTrue(((ScalarShape)api.declares().get(0)).dataType().value().contains("integer"));
        assertEquals(3, ((ScalarShape) api.declares().get(0)).values().size());

    }

    @Test
    public void enumeratedString() {

        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun")
                .withTypes(
                        DeclaredShapeBuilder.typeDeclaration("Mom").ofType(EnumShapeBuilder.enumeratedType().enumValues("1", "2", "3"))
                )
                .buildModel();

        assertEquals("Mom", ((ScalarShape)api.declares().get(0)).name().value());
        assertTrue(((ScalarShape)api.declares().get(0)).dataType().value().contains("string"));
        assertEquals(3, ((ScalarShape) api.declares().get(0)).values().size());

    }

    @Test
    public void complexType() {

        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun")
                .withTypes(
                        DeclaredShapeBuilder.typeDeclaration("Mom")
                                .ofType(NodeShapeBuilder.inheritingObjectFromShapes()
                                        .withProperty(
                                                PropertyShapeBuilder.property("name", TypeShapeBuilder.simpleType("string")))
                                )
                )
                .buildModel();

        assertEquals("Mom", ((NodeShape)api.declares().get(0)).name().value());
        assertEquals(0, (((NodeShape) api.declares().get(0)).inherits().size()));
        assertEquals("name", ((NodeShape)api.declares().get(0)).properties().get(0).name().value());
        assertTrue(((NodeShape)api.declares().get(0)).properties().get(0).range().name().value().contains("anonymous"));
    }

    @Test
    public void complexInheritance() throws ExecutionException, InterruptedException {

        DeclaredShapeBuilder parent = DeclaredShapeBuilder.typeDeclaration("Parent")
                .ofType(NodeShapeBuilder.inheritingObjectFromShapes()
                        .withProperty(
                                PropertyShapeBuilder.property("subName", TypeShapeBuilder.stringScalar()))
                );

        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun")
                .withTypes(
                        DeclaredShapeBuilder.typeDeclaration("Mom")
                                .ofType(NodeShapeBuilder.inheritingObjectFromShapes(parent.buildNode()).withProperty(PropertyShapeBuilder.property("name", TypeShapeBuilder.stringScalar()))
                                ),
                        parent
                        )
                .buildModel();


        assertEquals("Mom", ((NodeShape)api.declares().get(0)).name().value());
        assertEquals(1, (((NodeShape) api.declares().get(0)).inherits().size()));
        assertEquals("name", ((NodeShape)api.declares().get(0)).properties().get(0).name().value());
        assertTrue(((NodeShape)api.declares().get(0)).properties().get(0).range().name().value().contains("string"));
    }


    @Test
    public void multipleInheritance() {

        DeclaredShapeBuilder parent1 = DeclaredShapeBuilder.typeDeclaration("Parent1")
                .ofType(NodeShapeBuilder.inheritingObjectFromShapes()
                        .withProperty(
                                PropertyShapeBuilder.property("subName", TypeShapeBuilder.stringScalar()))
                );

        DeclaredShapeBuilder parent2 = DeclaredShapeBuilder.typeDeclaration("Parent2")
                .ofType(NodeShapeBuilder.inheritingObjectFromShapes()
                        .withProperty(
                                PropertyShapeBuilder.property("subName2", TypeShapeBuilder.stringScalar()))
                );

        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun")
                .withTypes(
                        DeclaredShapeBuilder.typeDeclaration("Mom")
                                .ofType(NodeShapeBuilder.inheritingObjectFromShapes(parent1.buildNode(), parent2.buildNode()).withProperty(PropertyShapeBuilder.property("name", TypeShapeBuilder.stringScalar()))
                                ),
                        parent1, parent2
                )
                .buildModel();


        assertEquals("Mom", ((NodeShape) api.declares().get(0)).name().value());
        assertEquals(2, (((NodeShape) api.declares().get(0)).inherits().size()));
        assertTrue(((NodeShape) api.declares().get(0)).properties().get(0).range().name().value().contains("string"));
        assertEquals("name", ((NodeShape) api.declares().get(0)).properties().get(0).name().value());
        assertEquals("subName", ((NodeShape) ((NodeShape) api.declares().get(0)).inherits().get(0)).properties().get(0).name().value());
        assertEquals("subName2", ((NodeShape) ((NodeShape) api.declares().get(0)).inherits().get(1)).properties().get(0).name().value());

    }

    // Should you try the same test with a text file, you get the same result.  'sweird.
    @Test
    public void unionType() {

        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun")
                .withTypes(
                        DeclaredShapeBuilder.typeDeclaration("Mom")
                                .ofType(TypeShapeBuilder.unionShapeOf(TypeShapeBuilder.stringScalar().buildNode(), TypeShapeBuilder.longScalar().buildNode()).withProperty(PropertyShapeBuilder.property("name", TypeShapeBuilder.stringScalar()))
                                )
                )
                .buildModel();


        assertEquals("Mom", ((UnionShape) api.declares().get(0)).name().value());
        assertTrue(((ScalarShape)((UnionShape)api.declares().get(0)).inherits().get(0)).dataType().value().contains("string"));
        assertTrue(((ScalarShape)((UnionShape)api.declares().get(0)).inherits().get(1)).dataType().value().contains("long"));
    }

    // unions of declared types, they work ok.
    @Test
    public void complexUnions() {


        DeclaredShapeBuilder parent1 = DeclaredShapeBuilder.typeDeclaration("Parent1")
                .ofType(NodeShapeBuilder.inheritingObjectFromShapes()
                        .withProperty(
                                PropertyShapeBuilder.property("subName", TypeShapeBuilder.stringScalar()))
                );

        DeclaredShapeBuilder parent2 = DeclaredShapeBuilder.typeDeclaration("Parent2")
                .ofType(NodeShapeBuilder.inheritingObjectFromShapes()
                        .withProperty(
                                PropertyShapeBuilder.property("subName2", TypeShapeBuilder.stringScalar()))
                );

        WebApiDocument api = document()
                .baseUri("http://google.com")
                .title("doc")
                .version("one")
                .mediaType("foo/fun")
                .withTypes(
                        DeclaredShapeBuilder.typeDeclaration("Mom")
                                .ofType(TypeShapeBuilder.unionShapeOf(parent1.buildNode(), parent2.buildNode()).withProperty(PropertyShapeBuilder.property("name", TypeShapeBuilder.stringScalar()))
                                )
                )
                .buildModel();

        assertEquals("Mom", ((UnionShape) api.declares().get(0)).name().value());
        assertEquals("Parent1", ((UnionShape)api.declares().get(0)).inherits().get(0).name().value());
        assertEquals("Parent2", ((UnionShape)api.declares().get(0)).inherits().get(1).name().value());
    }


}