package no.sirktek.taxonomy.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static no.sirktek.taxonomy.model.LogisticsPropertyDefinition.PropertyType;
import static no.sirktek.taxonomy.model.LogisticsPropertyDefinition.getPropertyType;

class PropertyDefinitionTest {

    @Test
    void shouldBuildPropertyDefinitionWithAllFields() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("address")
                .englishLabel("Address")
                .norwegianLabel("Adresse")
                .uri("http://taxonomy.sirktek.no/logistics#address")
                .rangeType("http://www.w3.org/2001/XMLSchema#string")
                .domainClass("Location")
                .description("Physical address of the location")
                .build();

        assertEquals("address", property.name());
        assertEquals("Address", property.englishLabel());
        assertEquals("Adresse", property.norwegianLabel());
        assertEquals("http://taxonomy.sirktek.no/logistics#address", property.uri());
        assertEquals("http://www.w3.org/2001/XMLSchema#string", property.rangeType());
        assertEquals("Location", property.domainClass());
        assertEquals("Physical address of the location", property.description());
    }

    @Test
    void shouldDetectStringPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("name")
                .rangeType("http://www.w3.org/2001/XMLSchema#string")
                .build();

        assertEquals(PropertyType.STRING, getPropertyType(property));
    }

    @Test
    void shouldDetectAddressPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("address")
                .rangeType("http://www.w3.org/2001/XMLSchema#string")
                .build();

        assertEquals(PropertyType.ADDRESS, getPropertyType(property));
    }

    @Test
    void shouldDetectGeoPointPropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("position")
                .rangeType("http://www.w3.org/2003/01/geo/wgs84_pos#Point")
                .build();

        assertEquals(PropertyType.GEO_POINT, getPropertyType(property));
    }

    @Test
    void shouldDetectLocationTypePropertyType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("type")
                .rangeType("http://taxonomy.sirktek.no/logistics#LocationType")
                .build();

        assertEquals(PropertyType.LOCATION_TYPE, getPropertyType(property));
    }

    @Test
    void shouldDefaultToStringForUnknownTypes() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("unknownProperty")
                .rangeType("http://unknown.com/type")
                .build();

        assertEquals(PropertyType.STRING, getPropertyType(property));
    }

    @Test
    void shouldHandleNullRangeType() {
        PropertyDefinition property = PropertyDefinition.builder()
                .name("nullRangeProperty")
                .rangeType(null)
                .build();

        assertEquals(PropertyType.STRING, getPropertyType(property));
    }

    @Test
    void shouldTestAllPropertyTypeEnumValues() {
        // Test that all enum values are defined for logistics
        PropertyType[] allTypes = PropertyType.values();

        assertEquals(9, allTypes.length);

        // Check specific enum values exist
        assertNotNull(PropertyType.valueOf("STRING"));
        assertNotNull(PropertyType.valueOf("DECIMAL"));
        assertNotNull(PropertyType.valueOf("INTEGER"));
        assertNotNull(PropertyType.valueOf("DATE"));
        assertNotNull(PropertyType.valueOf("BOOLEAN"));
        assertNotNull(PropertyType.valueOf("URL"));
        assertNotNull(PropertyType.valueOf("ADDRESS"));
        assertNotNull(PropertyType.valueOf("GEO_POINT"));
        assertNotNull(PropertyType.valueOf("LOCATION_TYPE"));
    }
}
