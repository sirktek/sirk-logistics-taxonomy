# Design — adopt-shared-commons-properties

## Precedent

- **Opt-in:** a commons property applies to a logistics class through
  `common:<p> schema:domainIncludes logistics:<Class> .` in `logistics-base.ttl`, section
  "Cross-cutting property domain extensions" (logistics-base.ttl:217-239, README "Accounting and
  Tax Properties"). This change only edits those triples; no new mechanism.
- **Commons ranges:** logistics already references commons marker classes
  (`common:EnergySourceEntry`, `EmissionEntry`, `AllocationEntry`) without redeclaring them.
- **Opt-in test:** `CommonPropertyOptInTest` pins the opt-ins. It checks raw Jena triples because
  the loader ignored them before commons 3.14 (its javadoc says so). Now the loader honours them, so
  the test asserts what consumers see: `LogisticsTaxonomyService` properties. Same test class, same
  purpose, stronger check.
- **CI:** sirk-taxonomy-commons PR #2 (commit `7edfcc7`): Central steps removed from
  `build_tag_and_release.yml`, a `workflow_dispatch` `publish_maven_central.yml` taking a tag,
  README "Releases". Copied with one adaptation (D4).

No departures.

## Inventory of everything new or removed

| Kind | Item | Reader in this change |
| --- | --- | --- |
| pom | `taxonomy-commons` 3.13 → 3.14 | build |
| TTL | `common:valuations schema:domainIncludes logistics:RealEstate` | loader, `CommonPropertyOptInTest` |
| TTL | 4 commercial opt-ins on `RealEstate`, 4 on `OperatedProperty` | loader, `CommonPropertyOptInTest` |
| Removed TTL | `logistics:marketValue`; opt-ins to `common:bookValue`, `taxValue`, `wealthTaxValue` | — |
| Workflow | `.github/workflows/publish_maven_central.yml` (manual) | maintainers |
| Removed workflow steps | Central setup + deploy in `build_tag_and_release.yml` | — |
| Docs | README accounting/commercial section, new "Releases" section | — |

No new Java class, dependency, config, Java constant, enum value or endpoint. No new test class.

## What each class gains or loses (through `LogisticsTaxonomyService`, commons 3.14)

| Class | Before (commons 3.13) | After |
| --- | --- | --- |
| RealEstate | `propertyId`, `marketValue` (logistics, public), `landArea`, `builtYear`, `floorArea` | own `propertyId`, `landArea`, `builtYear`, `floorArea`; commons `ledgerAccount`, `depreciationGroup`, `valuations` (`privateSet accounting`), `marketValue`, `marketValueDate`, `monthlyRent`, `monthlyServiceCharge` (`privateSet commercial`) |
| OperatedProperty | `managedPropertyId` | + `marketValue`, `marketValueDate`, `monthlyRent`, `monthlyServiceCharge` (`commercial`) |
| every other class | unchanged | unchanged |

Checked: the only `common:` triples in `logistics-base.ttl` besides the opt-ins are ranges
(`EnergySourceEntry`, `EmissionEntry`, `AllocationEntry` on Location properties). No other
logistics class opts into anything. Commons properties whose domain is a commons class
(`common:address` on `Manufacturer`, `manufacturer`/`epd`/`emissionPerUnit` on `Model`, …) attach
only to commons classes, which the 3.14 loader keeps out of the logistics tree; so the local-name
overlap `logistics:address` / `common:address` causes no duplicate. After the deletion, no logistics
class declares a key it also opts into.

`marketValue` differences visible to consumers: `uri` `…/common#marketValue`, English label
"Market value" (was "Market Value"), Norwegian "Markedsverdi" (same), description "Estimated
market value of the asset in NOK", `domainClass` `null` (was `RealEstate`; commons properties have
no `rdfs:domain`), `privateSet` `commercial`. Range `xsd:decimal`, unchanged.

## Effect of the loader change on this repo's Java code

- `LogisticsRdfsTaxonomyLoader`, `LogisticsTaxonomyService`: no change (namespace + resource path).
- `LogisticsPropertyDefinition.getPropertyType`: `marketValue`, `monthlyRent`,
  `monthlyServiceCharge` → `DECIMAL`; `marketValueDate` → `DATE`; `ledgerAccount` → `STRING`;
  `depreciationGroup` (range `common:DepreciationGroup`) and `valuations` (range
  `common:AssetValuationEntry`) fall to the `STRING` default, as `yearlyEmission`
  (`common:EmissionEntry`) does today. Consumers resolve those through
  `CommonPropertyDefinition` (`ASSET_VALUATIONS`; enum options from `enumValues`). No change.
- `PropertyDefinition` gained `privateSet` in 3.14; `PropertyDefinitionTest` uses the builder, so
  it compiles unchanged.
- `TaxonomyServiceTest`: root-category count and class lookups are unaffected (classes stay
  namespace-filtered); `floorArea` / `landArea` assertions unaffected.
- Grep of `src/` for `bookValue`, `taxValue`, `wealthTaxValue`, `AssetValueEntry`, `ASSET_VALUE`:
  only the TTL lines, `CommonPropertyOptInTest` and the README. No enum lookup touches them.

## Decisions

### D1 — TTL edits (`logistics-base.ttl`)

- Delete the `logistics:marketValue` block (lines 185-189).
- Rewrite the "Cross-cutting property domain extensions" section (lines 217-239):

```turtle
# Accounting (privateSet "accounting", ledger17 names): ledger account,
# tax depreciation group and the yearly valuations (book value, tax value,
# wealth value per fiscal year). Typical group for real estate is h
# (buildings and installations); i for office buildings, j for fixed
# technical installations. Land is not depreciable. The actual group is
# chosen per category/asset by the consumer.
common:ledgerAccount schema:domainIncludes logistics:RealEstate .
common:depreciationGroup schema:domainIncludes logistics:RealEstate .
common:valuations schema:domainIncludes logistics:RealEstate .

# Commercial terms (privateSet "commercial").
common:marketValue schema:domainIncludes logistics:RealEstate, logistics:OperatedProperty .
common:marketValueDate schema:domainIncludes logistics:RealEstate, logistics:OperatedProperty .
common:monthlyRent schema:domainIncludes logistics:RealEstate, logistics:OperatedProperty .
common:monthlyServiceCharge schema:domainIncludes logistics:RealEstate, logistics:OperatedProperty .
```

  The `wealthTaxValue` comment goes: the wealth value is now a field of each valuation.
- Update the header comment of that section to say the loader merges commons at load time
  (it no longer says "only the merged graph in the consumer sees").

### D2 — `CommonPropertyOptInTest` through the loader

Replace the Jena model with a `LogisticsTaxonomyService` (as `TaxonomyServiceTest`). Assertions:

- `RealEstate` property names are exactly `propertyId, landArea, builtYear, floorArea,
  ledgerAccount, depreciationGroup, valuations, marketValue, marketValueDate, monthlyRent,
  monthlyServiceCharge` (set equality: catches the deleted three and any duplicate `marketValue`;
  also assert the list has no duplicate names).
- `privateSet`: `accounting` on `ledgerAccount`, `depreciationGroup`, `valuations`;
  `commercial` on the four commercial terms; `null` on the four own properties.
- `OperatedProperty` names are exactly `managedPropertyId` + the four commercial terms, the four
  with `privateSet` `commercial`.
- `marketValue` on `RealEstate` has `uri` `http://taxonomy.sirktek.no/common#marketValue`.
- `depreciationGroup` on `RealEstate` has ten `enumValues` (smoke check that the commons
  individuals reach the logistics model; commons tests the details).

Update the class javadoc to match. `TaxonomyServiceTest` and `PropertyDefinitionTest` stay as they
are unless the bump breaks them.

### D3 — README

"Accounting and Tax Properties" becomes "Accounting and commercial properties": a table of the
RealEstate opt-ins (`ledgerAccount`, `depreciationGroup`, `valuations` — a list of
`{year, bookValue, taxValue, wealthValue}` per fiscal year, ledger17 shape) and the commercial
terms on RealEstate and OperatedProperty, with their private set. Drop the `wealthTaxValue`
paragraph; keep the pointer to the commons README and `CommonPropertyOptInTest`. Add "Releases"
at the end, worded as commons':

> Every merge to `main` tags `3.<run number>` and publishes it to GitHub Packages. Maven Central
> (monthly publishing limit) is manual: run "Publish release to Maven Central" with the tag when a
> Central consumer needs that version. Publish the taxonomy-commons version this release depends on
> to Central first.

### D4 — CI

The logistics workflows differ from commons' only where logistics resolves `taxonomy-commons`
from GitHub Packages (setup-java `server-id: github` before the test step, `GITHUB_ACTOR` /
`GITHUB_TOKEN` env, `-Drevision=3.…`). `build_only.yml` is identical to commons'.

- `build_tag_and_release.yml`: delete the two Central steps (setup-java `server-id: central`,
  "Publish to Maven Central") and change the SNAPSHOT error message to "Cannot release a SNAPSHOT
  version", exactly as commons `7edfcc7`. Everything else stays.
- `publish_maven_central.yml`: commons' file (header comment, `workflow_dispatch` input `tag`,
  example `3.<n>`, checkout `ref: ${{ inputs.tag }}`, `versions:set -DnewVersion=${{ inputs.tag }}`,
  `clean deploy -P deployment -Dmaven.test.skip`) with one adaptation: `taxonomy-commons` is not
  on the runner's empty Maven cache and the `central` server settings carry no GitHub
  credentials. So before the Central setup, a setup-java step with `server-id: github` (as in the
  release workflow) and `./mvnw -B dependency:resolve` with `GITHUB_ACTOR` / `GITHUB_TOKEN` env
  fill the cache; `permissions` adds `packages: read`. The second setup-java (`central`) then
  rewrites `settings.xml` and the deploy resolves from the cache.

## Risks

- A consumer bumping logistics to this release without commons ≥ 3.14 on its classpath gets
  inert opt-ins and no `marketValue` at all. Maven nearest-wins: orgadmin-backend pins commons
  directly and must be on ≥ 3.14 (its `private-property-definitions` change).
- Publishing this release to Central before commons 3.14 is there gives Central consumers a POM
  whose dependency does not resolve; README "Releases" states the order.
