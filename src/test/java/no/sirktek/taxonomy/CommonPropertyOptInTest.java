package no.sirktek.taxonomy;

import no.sirktek.taxonomy.model.PropertyDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import no.sirktek.taxonomy.model.CommonPropertyDefinition;

/**
 * Verifies that the logistics taxonomy opts its classes into the shared commons
 * properties. The {@code schema:domainIncludes} opt-ins are honoured by the commons
 * loader (taxonomy-commons 3.19+), so this checks what consumers see: the properties
 * of {@code RealEstate} and {@code OperatedProperty} loaded through
 * {@link LogisticsTaxonomyService}, with their private set.
 */
class CommonPropertyOptInTest {

    private static final String COMMON = "http://taxonomy.sirktek.no/common#";
    private static final Set<String> ACCOUNTING = Set.of("ledgerAccount", "depreciationGroup", "valuations");
    private static final Set<String> COMMERCIAL =
            Set.of("marketValue", "marketValueDate", "monthlyRent", "monthlyServiceCharge");
    private static final Set<String> REAL_ESTATE_OWN = Set.of("propertyId", "landArea", "builtYear", "floorArea");

    private LogisticsTaxonomyService service;

    @BeforeEach
    void setUp() {
        service = new LogisticsTaxonomyService();
    }

    private List<PropertyDefinition> properties(String className) {
        return service.getCategoryByClassName(className)
                .orElseThrow(() -> new AssertionError("No such class: " + className))
                .properties();
    }

    private Map<String, PropertyDefinition> byName(String className) {
        return properties(className).stream().collect(Collectors.toMap(PropertyDefinition::name, p -> p));
    }

    @Test
    void realEstateHasExactlyOwnAndCommonPropertiesOnce() {
        List<PropertyDefinition> props = properties("RealEstate");
        Set<String> expected = new java.util.HashSet<>(REAL_ESTATE_OWN);
        expected.addAll(ACCOUNTING);
        expected.addAll(COMMERCIAL);

        assertEquals(expected, props.stream().map(PropertyDefinition::name).collect(Collectors.toSet()));
        assertEquals(expected.size(), props.size(), "property names must not repeat");
    }

    @Test
    void realEstatePropertiesCarryTheirPrivateSet() {
        Map<String, PropertyDefinition> props = byName("RealEstate");
        ACCOUNTING.forEach(n -> assertEquals("accounting", props.get(n).privateSet(), n));
        COMMERCIAL.forEach(n -> assertEquals("commercial", props.get(n).privateSet(), n));
        REAL_ESTATE_OWN.forEach(n -> assertNull(props.get(n).privateSet(), n));
    }

    @Test
    void marketValueIsTheCommonsProperty() {
        assertEquals(COMMON + "marketValue", byName("RealEstate").get("marketValue").uri());
    }

    @Test
    void depreciationGroupHasTenOptions() {
        assertEquals(10, byName("RealEstate").get("depreciationGroup").enumValues().size());
    }

    @Test
    void operatedPropertyHasOnlyTheCommercialTerms() {
        List<PropertyDefinition> props = properties("OperatedProperty");
        Set<String> expected = new java.util.HashSet<>(COMMERCIAL);
        expected.add("managedPropertyId");

        assertEquals(expected, props.stream().map(PropertyDefinition::name).collect(Collectors.toSet()));
        assertEquals(expected.size(), props.size(), "property names must not repeat");
        props.stream().filter(p -> COMMERCIAL.contains(p.name()))
                .forEach(p -> assertEquals("commercial", p.privateSet(), p.name()));
    }

    @Test
    void freightCarriesWeightAndVolumeOnceFromCommons() {
        List<PropertyDefinition> props = properties("Freight");
        for (String[] e : new String[][] {
                {"weight", "Weight (kg)", "Vekt (kg)"}, {"volume", "Volume (m\u00b3)", "Volum (m\u00b3)"}}) {
            List<PropertyDefinition> found = props.stream().filter(p -> e[0].equals(p.name())).toList();
            assertEquals(1, found.size(), e[0]);
            assertEquals(COMMON + e[0], found.get(0).uri());
            assertEquals(e[1], found.get(0).englishLabel());
            assertEquals(e[2], found.get(0).norwegianLabel());
        }
    }

    @Test
    void locationAddressStaysInLogistics() {
        assertEquals("http://taxonomy.sirktek.no/logistics#address", byName("Location").get("address").uri());
    }

    @Test
    void controlPointCarriesCommonControlChecklistOnce() {
        List<PropertyDefinition> found = properties("ControlPoint").stream()
                .filter(p -> "controlChecklist".equals(p.name())).toList();
        assertEquals(1, found.size());
        PropertyDefinition p = found.get(0);
        assertEquals(COMMON + "controlChecklist", p.uri());
        assertEquals(COMMON + "ChecklistTemplateEntry", p.rangeType());
        assertEquals("Checklist", p.englishLabel());
        assertEquals("Sjekkliste", p.norwegianLabel());
        assertEquals(CommonPropertyDefinition.PropertyType.CHECKLIST, CommonPropertyDefinition.getPropertyType(p));
    }
}
