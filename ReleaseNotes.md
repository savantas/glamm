# Introduction #

Better late than never, the official GLAMM release notes wiki.


# Releases #

**Current: v2.2.4**

**GLAMM v2.2.4 - 8/20/2013**
  * Fixed an issue where experiment uploads by gene name were not working.

**GLAMM v2.2.3 - 06/05/2012**
  * Fixed a bug where (sometimes) using the retrosynthesis tool to select from one of several compounds with ambiguous names (e.g. D-glucose leads to alpha and beta versions) would cause a crash.
  * Fixed a bug preventing download of calculated retrosynthesis pathways.
  * Fixed a bug where database handles would time-out and require a server restart if they were not used for more than 20 hours.
  * Added a "Clear routes" box to the Retrosynthesis tool.
  * The search box now ignores common non-alphanumeric characters when building suggestions. For example, the search phrase "D glucose" will now find "D-glucose" along with "adenosine Diphosphate glucose"
  * The above has been applied to searching for metabolites, reactions, and organisms
  * The popup that appears when clicking on a canonical KEGG pathway name has been redesigned. The map it displays is larger and (hopefully) more useful. Clicking the "Show Reaction Table" button will toggle over to a list of all reactions in that pathway with some extra information (EC number, reaction name, and formula). If a species is selected, a clickable list of genes will also be displayed.

**GLAMM v2.2.1 - 12/09/2011**
  * Ensured that TW-DFS is first in list of algorithm choices when an organism is selected.
  * Fixed bug where non-native reactions were not being specified as such for DFS.

**GLAMM v2.2.0 - 12/08/2011**
  * User may now upload experiment data in tab-delimited files targeting genes, compounds, and reactions.
  * Project now has complete javadoc documentation.
  * Minor refactoring and bug fixing.