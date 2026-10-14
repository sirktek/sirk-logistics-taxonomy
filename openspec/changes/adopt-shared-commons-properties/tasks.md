# Tasks — adopt-shared-commons-properties

One group, one implementer in a fresh context with this file, design.md and
`specs/logistics-common-properties/spec.md`. Touch only the listed files, run only the listed
tests, tick the tasks, append a short `## Handoff — Group 1` note (what changed, what is
unverified) to `handoff.md` in this directory, and stop when the tests pass. Maven through `mvnq`
(`~/.local/bin/mvnq`, same arguments as `./mvnw`); never `clean`. No Java code in `src/main`.

Suggested model: sonnet (design pins every triple, name and assertion).

### Group 0: Issue and branch (main loop)

- [x] 0.1 Open a GitHub issue in sirktek/sirk-logistics-taxonomy ("Adopt shared commons
      properties: commons 3.14, valuations and commercial terms, manual Central publishing"),
      linking sirktek/sirk-taxonomy-commons#1.
- [x] 0.2 Branch `feature/#<id>-adopt-shared-commons-properties` from `main`.

### Group 1: Bump commons, opt-ins, test, README, CI

Design D1–D4.

Files: `pom.xml` (taxonomy-commons version only),
`src/main/resources/taxonomy/logistics-base.ttl`,
`src/test/java/no/sirktek/taxonomy/CommonPropertyOptInTest.java`,
`src/test/java/no/sirktek/taxonomy/TaxonomyServiceTest.java` and
`src/test/java/no/sirktek/taxonomy/model/PropertyDefinitionTest.java` (only if the bump breaks
them), `README.md`, `.github/workflows/build_tag_and_release.yml`,
new `.github/workflows/publish_maven_central.yml`.
Tests: `mvnq test -Dtest=CommonPropertyOptInTest`, then the full suite once: `mvnq test`.

- [ ] 1.1 `pom.xml`: `taxonomy-commons` 3.13 → 3.14. Run `mvnq test`; note anything that breaks
      before the TTL edit (expected: `CommonPropertyOptInTest`, which asserts the deleted opt-ins).
- [ ] 1.2 Test first: rewrite `CommonPropertyOptInTest` per D2 (through `LogisticsTaxonomyService`;
      exact name sets for `RealEstate` and `OperatedProperty`, no duplicate names, `privateSet`
      values, `marketValue` uri, ten `depreciationGroup` options; javadoc updated). See it fail.
- [ ] 1.3 `logistics-base.ttl` per D1: delete `logistics:marketValue`; rewrite the cross-cutting
      section (drop `bookValue`, `taxValue`, `wealthTaxValue` and the wealthTaxValue comment; add
      `valuations` and the four commercial terms on `RealEstate` and `OperatedProperty`).
      `mvnq test -Dtest=CommonPropertyOptInTest` passes.
- [ ] 1.4 README per D3: accounting and commercial section; "Releases" section.
- [ ] 1.5 CI per D4: drop the two Central steps and fix the SNAPSHOT message in
      `build_tag_and_release.yml`; add `publish_maven_central.yml` (commons' file at tag 3.14 plus
      the GitHub Packages warm-up step and `packages: read`). Check both with
      `python3 -c 'import yaml,sys;[yaml.safe_load(open(f)) for f in sys.argv[1:]]' .github/workflows/*.yml`.
- [ ] 1.6 Full suite: `mvnq test`. Grep `src/` and README for `bookValue`, `taxValue`,
      `wealthTaxValue`, `AssetValueEntry`, `logistics:marketValue`: no hits outside the test's
      absence assertions.
