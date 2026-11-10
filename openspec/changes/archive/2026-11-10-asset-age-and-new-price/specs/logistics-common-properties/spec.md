## MODIFIED Requirements

### Requirement: RealEstate carries the accounting and commercial commons properties

Loaded through `LogisticsTaxonomyService`, `RealEstate` SHALL have exactly the properties
`propertyId`, `landArea`, `floorArea`, `yearOfManufacture`, `installationYear`, `ledgerAccount`,
`depreciationGroup`, `valuations`, `marketValue`, `marketValueDate`, `monthlyRentExVAT` and
`monthlyServiceChargeExVAT`, each name once. `ledgerAccount`, `depreciationGroup` and `valuations`
SHALL have `privateSet` `accounting`; the four commercial terms SHALL have `privateSet`
`commercial`; the own properties and the two age properties SHALL have no `privateSet`.

#### Scenario: Accounting opt-ins attach with their private set
- **WHEN** the logistics taxonomy is loaded
- **THEN** `RealEstate` has `ledgerAccount`, `depreciationGroup` and `valuations`, each with `privateSet` `accounting`, and `depreciationGroup` has ten `enumValues`

#### Scenario: Deleted accounting properties are gone
- **WHEN** the logistics taxonomy is loaded
- **THEN** `RealEstate` has no property `bookValue`, `taxValue` or `wealthTaxValue`

#### Scenario: Commercial terms attach with their private set
- **WHEN** the logistics taxonomy is loaded
- **THEN** `RealEstate` has `marketValue`, `marketValueDate`, `monthlyRentExVAT` and `monthlyServiceChargeExVAT`, each with `privateSet` `commercial`, and no `monthlyRent` or `monthlyServiceCharge`

#### Scenario: Age properties come from commons
- **WHEN** the logistics taxonomy is loaded
- **THEN** `RealEstate` has `yearOfManufacture` with `uri` `http://taxonomy.sirktek.no/common#yearOfManufacture` and `installationYear` with `uri` `http://taxonomy.sirktek.no/common#installationYear`, both with no `privateSet`, and no property `builtYear`

### Requirement: OperatedProperty carries the commercial terms

Loaded through `LogisticsTaxonomyService`, `OperatedProperty` SHALL have exactly
`managedPropertyId`, `marketValue`, `marketValueDate`, `monthlyRentExVAT` and
`monthlyServiceChargeExVAT`; the four commercial terms SHALL have `privateSet` `commercial`.

#### Scenario: Commercial terms on a driftsobjekt
- **WHEN** the logistics taxonomy is loaded
- **THEN** `OperatedProperty` has `managedPropertyId` and the four commercial terms, the latter with `privateSet` `commercial`, and no accounting or age property
