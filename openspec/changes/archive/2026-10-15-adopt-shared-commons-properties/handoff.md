
## Handoff — Group 1

Changed: `pom.xml` (taxonomy-commons 3.14), `logistics-base.ttl` (deleted `logistics:marketValue`;
cross-cutting section rewritten per D1), `CommonPropertyOptInTest` (rewritten through
`LogisticsTaxonomyService`, 5 tests), `README.md` (accounting/commercial table, Releases),
`build_tag_and_release.yml` (Central steps removed, SNAPSHOT message), new
`publish_maven_central.yml` (commons' file plus GitHub Packages warm-up, `packages: read`).

Verified: commons 3.14 resolved; bump alone left the old test green; new test failed before the TTL
edit and passes after; full `mvnq test` 24 tests green; both workflow YAMLs parse. Grep: only hit is
the README's `valuations` shape text `{year, bookValue, taxValue, wealthValue}` (design D3 wording).

Unverified: the workflows have not run on GitHub; `TaxonomyServiceTest`/`PropertyDefinitionTest`
needed no change. No design deviations.
