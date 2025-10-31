package no.sirktek.taxonomy;

import no.sirktek.taxonomy.LogisticsTaxonomyService;
import no.sirktek.taxonomy.model.CategoryInfo;
import no.sirktek.taxonomy.model.TaxonomyTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaxonomyServiceTest {

    private LogisticsTaxonomyService taxonomyService;

    @BeforeEach
    void setUp() {
        taxonomyService = new LogisticsTaxonomyService();
    }

    @Test
    void shouldLoadBaseTaxonomy() {
        TaxonomyTree taxonomy = taxonomyService.loadBaseTaxonomy();

        assertNotNull(taxonomy);
        assertNotNull(taxonomy.rootCategories());
        assertFalse(taxonomy.rootCategories().isEmpty());
    }

    @Test
    void shouldFindLocationRootCategory() {
        Optional<CategoryInfo> location = taxonomyService.getCategoryByClassName("Location");

        assertTrue(location.isPresent());
        assertEquals("Location", location.get().className());
        assertEquals("Location", location.get().englishName());
        assertTrue(location.get().isRoot());
    }

    @Test
    void shouldFindLocationTypeCategory() {
        Optional<CategoryInfo> locationType = taxonomyService.getCategoryByClassName("LocationType");

        assertTrue(locationType.isPresent());
        assertEquals("LocationType", locationType.get().className());
    }

    @Test
    void shouldReturnEmptyForNonExistentCategory() {
        Optional<CategoryInfo> nonExistent = taxonomyService.getCategoryByClassName("NonExistent");

        assertFalse(nonExistent.isPresent());
    }

    @Test
    void shouldProvideStats() {
        TaxonomyService.TaxonomyStats stats = taxonomyService.getStats();

        assertTrue(stats.totalCategories() > 0);
        assertTrue(stats.rootCategories() > 0);

        // We expect at least 2 root categories (Location, LocationType)
        assertTrue(stats.rootCategories() >= 2);
    }

    @Test
    void shouldCacheTaxonomyAfterFirstLoad() {
        TaxonomyTree first = taxonomyService.loadBaseTaxonomy();
        TaxonomyTree second = taxonomyService.loadBaseTaxonomy();

        // Should be the same instance due to caching
        assertSame(first, second);
    }

    @Test
    void shouldReloadBaseTaxonomy() {
        // Load taxonomy first time
        TaxonomyTree first = taxonomyService.loadBaseTaxonomy();

        // Reload should give us a fresh instance
        TaxonomyTree reloaded = taxonomyService.reloadBaseTaxonomy();

        assertNotNull(reloaded);
        assertNotSame(first, reloaded);

        // But subsequent loads should cache the reloaded version
        TaxonomyTree cached = taxonomyService.loadBaseTaxonomy();
        assertSame(reloaded, cached);
    }

    @Test
    void shouldDetectBaseTaxonomyClasses() {
        assertTrue(taxonomyService.isBaseTaxonomyClass("Location"));
        assertTrue(taxonomyService.isBaseTaxonomyClass("LocationType"));
        assertFalse(taxonomyService.isBaseTaxonomyClass("CustomClass"));
        assertFalse(taxonomyService.isBaseTaxonomyClass("NonExistent"));
    }

}
