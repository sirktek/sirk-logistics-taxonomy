# Proposal — adopt-shared-commons-properties

## Why

taxonomy-commons 3.14 (sirktek/sirk-taxonomy-commons PR #2, change `shared-commons-properties`)
makes `schema:domainIncludes` opt-ins work: every domain loader now merges `common-base.ttl` into
its model and attaches commons properties to the classes that opt in. It also reshaped the
accounting vocabulary to ledger17's (`common:valuations` replaces `bookValue`, `taxValue`,
`wealthTaxValue`), added the commercial terms (`marketValue`, `marketValueDate`, `monthlyRent`,
`monthlyServiceCharge`) and marks both sets with `common:privateSet`. Its proposal names this
change as the logistics follow-up.

Today on commons 3.13, `RealEstate` loads with `builtYear, propertyId, marketValue, floorArea,
landArea`; its five accounting opt-ins are dead, three of them point at properties 3.14 deleted,
and `logistics:marketValue` has the same key as the new `common:marketValue`.

Also, Maven Central's monthly publishing limit is reached: as in commons PR #2, Central becomes a
manual workflow.

## What changes

- **Bump** `no.sirktek:taxonomy-commons` 3.13 → 3.14 (tag `3.14`, commit `9ed7141`, the release of
  commons PR #2).
- **`logistics:RealEstate`** opts into `common:valuations` and the four commercial terms; keeps
  `common:ledgerAccount` and `common:depreciationGroup`; drops the opt-ins to the deleted
  `common:bookValue`, `common:taxValue`, `common:wealthTaxValue`.
- **`logistics:OperatedProperty`** opts into the four commercial terms.
- **`logistics:marketValue` is deleted**; `RealEstate` gets `common:marketValue` instead (same
  key `marketValue`, URI `…/common#marketValue`, `privateSet "commercial"`, label "Market value").
- **`CommonPropertyOptInTest`** asserts through `LogisticsTaxonomyService` instead of raw Jena.
- **README:** the accounting section describes `valuations` and the commercial terms; new
  "Releases" section.
- **CI:** Maven Central steps leave `build_tag_and_release.yml`; new `publish_maven_central.yml`
  (`workflow_dispatch`, input `tag`).

## Impact

- Consumers that bump logistics (sirk-orgadmin-backend, sirk-mcp-server) see on `RealEstate`:
  `ledgerAccount`, `depreciationGroup` (10 saldogruppe options), `valuations` (`privateSet
  accounting`) and four commercial terms (`privateSet commercial`); on `OperatedProperty` the four
  commercial terms. `marketValue` keeps its key but changes URI, English label ("Market value") and
  becomes private.
- No Java change in `src/main`. Release: next `3.<run number>` (breaking for the data model, as
  3.13's `feat!`).

## Not in scope

- **vardly-homepage:** remove "Markedsverdi" from the logistics page (PO round 3, decision 5).
- **sirk-machine-, sirk-furniture-, sirk-ict-taxonomy:** their own follow-ups of
  `shared-commons-properties`.
- **sirk-orgadmin-backend / frontends:** bumping and showing `privateSet`
  (`private-property-definitions`, `edit-private-properties`).
- **sirk-mcp-server:** unpacks the logistics and commons TTLs (pins logistics 3.18, commons 3.12);
  it picks this up when it bumps.

## Decided with the product owner

- Stored `marketValue` values (2026-10-13): left as they are, no data move (green-field). Old public
  values stay in `properties` without a definition; users enter the value in the commercial set.
