package no.sirktek.taxonomy;

import no.sirktek.taxonomy.loader.LogisticsRdfsTaxonomyLoader;

/**
 * Main service for accessing logistics taxonomy data
 */
public class LogisticsTaxonomyService extends TaxonomyService {

    /**
     * Default constructor using logistics taxonomy loader
     */
    public LogisticsTaxonomyService() {
        super(new LogisticsRdfsTaxonomyLoader());
    }
}
