package no.sirktek.taxonomy.model;

/**
 * Logistics-specific property definition with type detection
 */
public class LogisticsPropertyDefinition {

    /**
     * Convert RDF range type to PropertyType enum equivalent for logistics taxonomy
     * @param propertyDef the property definition
     * @return the corresponding PropertyType enum value
     */
    public static PropertyType getPropertyType(PropertyDefinition propertyDef) {
        String rangeType = propertyDef.rangeType();
        String name = propertyDef.name();

        if (rangeType == null) {
            return PropertyType.STRING;
        }

        return switch (rangeType) {
            case "http://www.w3.org/2001/XMLSchema#string" -> {
                if (name != null) {
                    if (name.equals("address")) yield PropertyType.ADDRESS;
                }
                yield PropertyType.STRING;
            }
            case "http://www.w3.org/2001/XMLSchema#decimal" -> PropertyType.DECIMAL;
            case "http://www.w3.org/2001/XMLSchema#date" -> PropertyType.DATE;
            case "http://www.w3.org/2001/XMLSchema#boolean" -> PropertyType.BOOLEAN;
            case "http://www.w3.org/2001/XMLSchema#anyURI" -> PropertyType.URL;
            case "http://www.w3.org/2001/XMLSchema#integer" -> PropertyType.INTEGER;
            default -> {
                // Handle custom types for logistics
                if (rangeType.contains("Point")) {
                    yield PropertyType.GEO_POINT;
                }
                if (rangeType.contains("LocationType")) {
                    yield PropertyType.LOCATION_TYPE;
                }
                yield PropertyType.STRING; // Default fallback
            }
        };
    }

    /**
     * Property types for logistics taxonomy
     */
    public enum PropertyType {
        /** String property type */
        STRING,
        /** Decimal property type */
        DECIMAL,
        /** Integer property type */
        INTEGER,
        /** Date property type */
        DATE,
        /** Boolean property type */
        BOOLEAN,
        /** URL property type */
        URL,
        /** Address property type */
        ADDRESS,
        /** Geographic point property type */
        GEO_POINT,
        /** Location type enumeration property type */
        LOCATION_TYPE
    }
}
