## ADDED Requirements

### Requirement: RealEstate carries the accounting and commercial commons properties

Loaded through `LogisticsTaxonomyService`, `RealEstate` SHALL have exactly the properties
`propertyId`, `landArea`, `builtYear`, `floorArea`, `ledgerAccount`, `depreciationGroup`,
`valuations`, `marketValue`, `marketValueDate`, `monthlyRent` and `monthlyServiceCharge`, each
name once. `ledgerAccount`, `depreciationGroup` and `valuations` SHALL have `privateSet`
`accounting`; the four commercial terms SHALL have `privateSet` `commercial`; the own properties
SHALL have no `privateSet`.

#### Scenario: Accounting opt-ins attach with their private set
- **WHEN** the logistics taxonomy is loaded
- **THEN** `RealEstate` has `ledgerAccount`, `depreciationGroup` and `valuations`, each with `privateSet` `accounting`, and `depreciationGroup` has ten `enumValues`

#### Scenario: Deleted accounting properties are gone
- **WHEN** the logistics taxonomy is loaded
- **THEN** `RealEstate` has no property `bookValue`, `taxValue` or `wealthTaxValue`

#### Scenario: Commercial terms attach with their private set
- **WHEN** the logistics taxonomy is loaded
- **THEN** `RealEstate` has `marketValue`, `marketValueDate`, `monthlyRent` and `monthlyServiceCharge`, each with `privateSet` `commercial`

### Requirement: marketValue is the commons property

The logistics taxonomy SHALL NOT declare its own `marketValue`. `RealEstate` SHALL have exactly one
property named `marketValue`, with `uri` `http://taxonomy.sirktek.no/common#marketValue`.

#### Scenario: One marketValue, from commons
- **WHEN** the logistics taxonomy is loaded
- **THEN** `RealEstate` has one property named `marketValue` and its `uri` is `http://taxonomy.sirktek.no/common#marketValue`

### Requirement: OperatedProperty carries the commercial terms

Loaded through `LogisticsTaxonomyService`, `OperatedProperty` SHALL have exactly
`managedPropertyId`, `marketValue`, `marketValueDate`, `monthlyRent` and `monthlyServiceCharge`;
the four commercial terms SHALL have `privateSet` `commercial`.

#### Scenario: Commercial terms on a driftsobjekt
- **WHEN** the logistics taxonomy is loaded
- **THEN** `OperatedProperty` has `managedPropertyId` and the four commercial terms, the latter with `privateSet` `commercial`, and no accounting property

### Requirement: Maven Central publishing is manual

A merge to `main` SHALL tag `3.<run number>` and publish to GitHub Packages only. Publishing a
tag to Maven Central SHALL be a `workflow_dispatch` workflow taking the tag as input.

#### Scenario: Merge to main
- **WHEN** a commit is pushed to `main`
- **THEN** the release workflow tags and publishes to GitHub Packages and runs no Maven Central step

#### Scenario: Manual Central release
- **WHEN** a maintainer runs "Publish release to Maven Central" with tag `3.<n>`
- **THEN** the workflow checks out that tag, sets the version to `3.<n>` and deploys it with the `deployment` profile
