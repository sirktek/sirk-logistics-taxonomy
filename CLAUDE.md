# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview
This is a Java library that provides access to a standardized logistics taxonomy defined in RDF-S. It extends the `sirk-taxonomy-commons` library and provides location and logistics concepts with English class names and bilingual labels stored in the RDF-S file. The project follows the same architectural patterns as the sibling `sirk-furniture-taxonomy` project.

## Common Commands

### Build and Test
```bash
# Using system Maven
mvn compile          # Compile source code
mvn test             # Run unit tests
mvn package          # Build JAR artifact
mvn install          # Install to local Maven repository
mvn clean            # Clean build artifacts

# Using Maven wrapper (recommended, when available)
./mvnw compile       # Compile source code
./mvnw test          # Run unit tests
./mvnw package       # Build JAR artifact
./mvnw install       # Install to local Maven repository
./mvnw clean         # Clean build artifacts
```

### Running Single Tests
```bash
# Run a specific test class
mvn test -Dtest=TaxonomyServiceTest

# Run a specific test method
mvn test -Dtest=TaxonomyServiceTest#testLoadBaseTaxonomy
```

## Architecture

### Layered Structure
Following the furniture-taxonomy architecture:
- **Commons Dependency**: Extends `sirk-taxonomy-commons` for shared model, loader, and service
- **Loader Layer** (`loader/`): `LogisticsRdfsTaxonomyLoader` extends abstract base loader
- **Service Layer**: `LogisticsTaxonomyService` extends `TaxonomyService`
- **Property Types** (`model/`): `LogisticsPropertyDefinition` with logistics-specific type detection

### Data Flow
1. RDF-S Turtle file (`logistics-base.ttl`) defines logistics hierarchy with bilingual labels
2. `LogisticsRdfsTaxonomyLoader` extends base loader and specifies namespace + resource path
3. Base loader (from commons) parses RDF-S using Apache Jena
4. `LogisticsTaxonomyService` provides caching and high-level API
5. Clients consume via simple Java API (English class names)

### Key Files
- `/src/main/resources/taxonomy/logistics-base.ttl` - RDF-S taxonomy definition
  - Defines `Location` class with properties: name, description, address, position, type
  - Includes 15 location types: Site, Property, Complex, Zone, Building, Entrance, Section, Floor, Room, Rack, Aisle, Row, Shelf, Bin
  - Integrates W3C WGS84 geo vocabulary for geographic coordinates
  - Uses QB4ST for spatial dimension metadata and CRS support
- `LogisticsTaxonomyService.java` - Extends base service with logistics loader
- `LogisticsRdfsTaxonomyLoader.java` - Specifies logistics namespace and resource path
- `LogisticsPropertyDefinition.java` - Logistics-specific property type detection

## Development Notes

### Technology Stack
- Java 17
- Apache Maven 3.9+
- **sirk-taxonomy-commons** for shared base classes
- Apache Jena 5.5.0 (via commons dependency)
- Lombok 1.18.36 for code generation
- JUnit Jupiter 5.11.3 for testing

### RDF-S Schema Design
The logistics taxonomy uses:
- **Classes**: Location types with `rdfs:Class` definitions
- **Enumeration Pattern**: LocationType instances representing valid type values
- **Geo-spatial Support**:
  - `geo:Point` from W3C WGS84 vocabulary for coordinates
  - `qb4st:SpatialDimension` for CRS metadata (EPSG:4326/WGS84)
  - Two coordinate patterns supported: standard `geo:lat/long` and custom `logistics:lat/long` sub-properties
- **Properties**: Standard RDF properties with proper domain/range definitions
- **Labels**: Bilingual labels in English (`@en`) and Norwegian (`@no`)

### Language Support
- English URIs and class names for API compatibility (`http://taxonomy.sirktek.no/logistics#`)
- Bilingual labels (English/Norwegian) stored in RDF-S file for frontend consumption
- Frontend handles translation - Java library provides English class names

### Related Projects
This project is part of the Sirktek taxonomy suite:
- **sirk-furniture-taxonomy**: Furniture classification (sibling project)
- **sirk-logistics-taxonomy**: Logistics and location taxonomy (this project)

Both projects share the same architectural patterns, technology stack, and development practices.
