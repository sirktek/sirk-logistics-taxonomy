# Design — checklists (logistics-taxonomy slice)

## Precedent

Complex values in this repo already range over commons markers and are typed by
`CommonPropertyDefinition`, not by this repo's mapper (logistics-base.ttl: `rdfs:range
common:EmissionEntry`, `common:AllocationEntry`, `common:EnergySourceEntry`). The checklist
follows this exactly. No departure.

## Inventory of everything new

Nothing new in this repo: one changed `rdfs:range`, one dependency version bump, one updated
test. No Java production code changes. The logistics mapper's fallback already returns `STRING` for
the new range (its name contains none of the substrings the fallback matches), and orgadmin asks
`CommonPropertyDefinition` first.

## Decision

```turtle
logistics:controlChecklist a rdf:Property ;
    ...labels and comment unchanged...
    rdfs:domain logistics:ControlPoint ;
    rdfs:range common:ChecklistTemplateEntry .
```


## Risks

- **Release order:** taxonomy-commons first, then this repo, then sirk-orgadmin-backend bumps
  both. Shipping this range before orgadmin's Group 3 is harmless: an older orgadmin maps the
  unknown range to `STRING`, as today.
