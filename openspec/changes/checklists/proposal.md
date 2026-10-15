# Proposal — checklists (logistics-taxonomy slice)

## Why

`logistics:controlChecklist` holds a JSON-encoded ChecklistTemplate v1, but is declared with range
`xsd:string`. sirk-orgadmin-backend therefore validates a ControlPoint category's default checklist as a
generic string (at most 2000 characters, a "suspicious substring" screen), which limits a default
to about 15 short checks instead of the contract's 100 and refuses ordinary check text containing
e.g. `window.` or `data:`.

The product owner chose (2026-10-15, "option B") to validate a property by the type its
definition declares. Which property is a checklist is taxonomy data, so this repo declares it:
the range becomes the commons marker `common:ChecklistTemplateEntry` (sirk-taxonomy-commons
`checklists`).

Canonical cross-repo design: sirk-service-backend `openspec/changes/checklists/design.md` (rule
R7). No GitHub issue yet; open one before branching.

## What changes

- `logistics-base.ttl`: `logistics:controlChecklist` gets `rdfs:range common:ChecklistTemplateEntry` instead of
  `xsd:string`. URI, local name, domain, labels and comment are unchanged.
- `pom.xml`: `taxonomy-commons` bumped to the release that declares the marker.
- The test pinning `controlChecklist` asserts the new range and type.

## Impact

- **Stored values:** none change. Values are keyed by the local name `controlChecklist`, and an
  existing value is still a JSON string.
- **sirk-orgadmin-backend** reports the property as type `CHECKLIST` once it bumps this
  taxonomy and has its `checklists` Group 3; until then nothing changes there.
- **Breaking:** no for stored data. The property's reported type changes from `STRING` to
  `CHECKLIST` for clients of orgadmin (handled in the orgadmin-backend and orgadmin-frontend
  `checklists` changes; vardly-frontend echoes the type it reads).
- sirk-app-backend's `openspec/specs/control-point-checklists/spec.md` still states the range
  `xsd:string` and type `STRING`; a follow-up there (listed in the canonical per-owner table)
  updates it.

## Non-goals

- Moving the property to a shared `common:controlChecklist` (an open question in the
  taxonomy-commons `checklists` proposal).
