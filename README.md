# Sirktek Logistics Taxonomy Library

A Java library that provides access to a standardized logistics [taxonomy defined in RDF-S](src/main/resources/taxonomy/logistics-base.ttl) (Resource Description Framework Schema) with Turtle serialization format.

## Features

- **RDF-S based taxonomy**: Formal ontology structure using W3C standards
- **English URIs**: Standardized English class names for international compatibility
- **Bilingual labels**: Norwegian and English labels stored in RDF-S for frontend use
- **Java API**: Easy-to-use service for loading and querying taxonomy
- **Caching**: Efficient in-memory caching of parsed taxonomy
- **Apache Jena**: Robust RDF processing using industry-standard library
- **Geo-spatial Support**: Integration with W3C WGS84 geo vocabulary and QB4ST for coordinates

## Quick Start

### Maven Dependency

```xml
<dependency>
    <groupId>no.sirktek</groupId>
    <artifactId>logistics-taxonomy</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Usage

```java
// Create service instance
TaxonomyService taxonomyService = new TaxonomyService();

// Load the complete taxonomy tree
TaxonomyTree taxonomy = taxonomyService.loadBaseTaxonomy();

// Find category by English class name
Optional<CategoryInfo> location = taxonomyService.getCategoryByClassName("Location");
Optional<CategoryInfo> building = taxonomyService.getCategoryByClassName("Building");

// Check if English class name exists in taxonomy
boolean exists = taxonomyService.isBaseTaxonomyClass("Warehouse"); // -> false
boolean exists2 = taxonomyService.isBaseTaxonomyClass("Location"); // -> true

// Get taxonomy statistics
TaxonomyService.TaxonomyStats stats = taxonomyService.getStats();
System.out.println("Total categories: " + stats.totalCategories());
System.out.println("Root categories: " + stats.rootCategories());

// Access bilingual labels from CategoryInfo
if (location.isPresent()) {
    CategoryInfo locationInfo = location.get();
    String englishName = locationInfo.englishName(); // "Location"
    String norwegianName = locationInfo.norwegianName(); // "Lokasjon"
}
```

## Taxonomy Structure

The logistics taxonomy defines:

### Core Classes

- **Location**: A physical location in the logistics network
  - Properties: name, description, address, position (geo:Point), type (LocationType)

- **LocationType**: Enumeration of location types:
  - Site, Property, Complex, Zone, Building, Entrance
  - Section, Floor, Room, Rack, Aisle, Row, Shelf, Bin

### Geo-spatial Support

The taxonomy integrates W3C standards for geographic coordinates:
- **geo:Point**: W3C WGS84 geographic point
- **qb4st:SpatialDimension**: Coordinate reference system (CRS) metadata
- Supports EPSG:4326 (WGS84) coordinate reference system

Example location with coordinates:
```turtle
logistics:ExampleWarehouse a logistics:Location ;
    logistics:name "Main Warehouse" ;
    logistics:type logistics:Building ;
    logistics:position [
        a geo:Point ;
        qb4st:crs <http://epsg.io/4326> ;
        geo:lat "59.9139" ;
        geo:long "10.7522"
    ] .
```

## RDF-S Schema

The taxonomy is defined using RDF-S in Turtle format with:

- **Classes**: Location and LocationType with proper definitions
- **Properties**: Standard RDF properties with domain/range definitions
- **Labels**: Bilingual labels in English (`@en`) and Norwegian (`@no`)
- **Geo-spatial**: Integration with W3C WGS84 and QB4ST vocabularies

## Architecture

- **Model Layer**: `CategoryInfo`, `TaxonomyTree`, `PropertyDefinition` POJOs
- **Loader Layer**: `RdfsTaxonomyLoader` using Apache Jena for RDF parsing
- **Service Layer**: `TaxonomyService` providing high-level API with caching

## Testing

Run tests with:
```bash
mvn test
```

The test suite verifies:
- RDF-S parsing and taxonomy loading
- Category lookup by English class names
- Property definition type detection
- Taxonomy statistics and caching
- All PropertyDefinition functionality

## Related Projects

This library is part of the Sirktek taxonomy suite:
- **sirk-furniture-taxonomy**: Furniture classification taxonomy
- **sirk-logistics-taxonomy**: Logistics and location taxonomy (this project)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
