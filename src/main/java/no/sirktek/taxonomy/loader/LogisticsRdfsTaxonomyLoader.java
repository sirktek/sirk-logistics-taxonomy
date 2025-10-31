package no.sirktek.taxonomy.loader;

/**
 * Loads the logistics taxonomy from RDF-S Turtle files using Apache Jena
 */
public class LogisticsRdfsTaxonomyLoader extends no.sirktek.taxonomy.loader.RdfsTaxonomyLoader {

    private static final String LOGISTICS_NAMESPACE = "http://taxonomy.sirktek.no/logistics#";
    private static final String RESOURCE_PATH = "/taxonomy/logistics-base.ttl";

    /**
     * Default constructor
     */
    public LogisticsRdfsTaxonomyLoader() {
        super();
    }

    @Override
    protected String getNamespace() {
        return LOGISTICS_NAMESPACE;
    }

    @Override
    protected String getResourcePath() {
        return RESOURCE_PATH;
    }
}
