# How to prepare an SVG document for importation into GLAMM #
v1.0 10/27/2011
John Bates
jtbates@lbl.gov


## Introduction ##

In order for GLAMM to interact with an SVG document describing a metabolic network, the document must be annotated.  These annotations include:

  * Mappings from SVG elements to network element ids
  * CSS classes and attributes
  * Reaction graph connectivity

At the time of this writing, there is no graphical editor for GLAMM format documents and this annotation must be done by hand.

The format used by GLAMM is an adaptation of KEGG's KGML format, described here:

http://www.genome.jp/kegg/xml/

Please note that KGML+ and GLAMM format files are NOT mutually interoperable.

### File Format Overview ###

The KGML+ file format is an XML format consisting of a specially annotated SVG document and a description of the reaction graph wrapped as follows:

```
<kgml>
	<svg>
		…
	</svg>
	<reactions>
		…
	</reactions>
</kgml>
```

It may seem redundant to have a separate reaction graph description on top of the SVG document, but SVG only describes the geometry and formatting of the displayed pathway and not its connectivity.

GLAMM extracts the svg element from the KGML+ document and annotates such that the relationship between compounds, reactions, and pathway ids and svg elements is made explicit.

## SVG Annotations ##

### “Viewport” group ###

All SVG elements must be wrapped in a top level “viewport” group so that they may be panned and zoomed as a unit:

```
<svg>
	<g id=”viewport”>
		(Your SVG data goes here)
	</g>
</svg>
```

### Compound Annotation ###

Compounds are represented by a group enclosing one SVG ellipse element, as in the example below:

```
<g class="cpd" compound="C00110">
	<ellipse class="cpd" cx="728.50" cy="99.50" fill="#99CCFF" rx="7.000000" ry="7.000000" stroke="#99CCFF" stroke-width="1"/>
</g>
```

|Element|Attribute|Value|Notes|
|:------|:--------|:----|:----|
|g      |class    |cpd  |Required|
|g      |compound |Compound id|It is not necessary to supply the source database to which this id belongs - this is inferred automatically.|
|ellipse|class    |cpd  |Required - CSS class attribute.|

### Reaction Annotation ###

Reactions are represented by a group enclosing one or more SVG pathelements, as in the example below:

```
<g class="rxn" enzyme="3.5.1.78+6.3.1.8+6.3.1.9+3.5.1.-" reaction="R01917+R01918">
	<path class="rxn" d="M3225.50,2068.50L2533.50,2068.50C2523.50,2068.50,2513.50,2058.50,2513.50,2048.50L2513.50,1978.50" fill="none" stroke="#FF9900" stroke-linecap="square" stroke-miterlimit="10" stroke-width="3"/>
	<path class="rxn" d="M3224.50,2068.50L3207.50,2068.50C3199.50,2068.50,3191.50,2060.50,3191.50,2052.50L3191.50,1942.50" fill="none" stroke="#FF9900" stroke-linecap="square" stroke-miterlimit="10" stroke-width="3"/>
	<path class="rxn" d="M3225.50,2068.50L2533.50,2068.50C2523.50,2068.50,2513.50,2058.50,2513.50,2048.50L2513.50,1978.50" fill="none" stroke="#FF9900" stroke-linecap="square" stroke-miterlimit="10" stroke-width="3"/>
</g>
```

|Element|Attribute|Value|Notes|
|:------|:--------|:----|:----|
|g      |class    |rxn  |Required|
|g      |enzyme   |EC numbers for reaction|If multiple values apply, separate them with the "+" character.|
|g      |reaction |Reaction id|If multiple values apply, separate them with the "+" character.|
|path   |class    |rxn  |Required - CSS class attribute.|

### Map Annotation ###

External, linked pathway maps are represented by a group enclosing one or more SVG tspan elements.  The group may also contain other elements, including other groups, as in the example below.  As before, GLAMM-specific annotations are in italics:

```
<g class="map" map="map00624">
	<rect fill="none" height="40.50" rx="0" ry="0" stroke="none" stroke-width="1" width="211.50" x="237.50" y="2103.50"/>
   	<g>
  		<text kerning="1.00">
     			<tspan class="map" fill="#FFFFFF" state="default" style=" font-family: Arial; font-weight: normal; font-style: normal; font-size: 16px;" x="242.50" y="2122.50">Polycyclic aromatic </tspan>
      			<tspan class="map" fill="#FFFFFF" state="default" style=" font-family: Arial; font-weight: normal; font-style: normal; font-size: 16px;" x="242.50" y="2137.50">hydrocarbon degradation</tspan>
        	</text>
    	</g>
</g>
```

|Element|Attribute|Value|Notes|
|:------|:--------|:----|:----|
|g      |class    |map  |Required|
|g      |map      |Map id|Currently, only KEGG maps are supported by GLAMM.|
|tspan  |class    |map  |Required - CSS class attribute.|

## Reaction Graph Connectivity Annotations ##

The reaction graph connectivity is encoded in a manner identical to that of the KGML+ specification.  The only exception is the fact that the colon-delimited prefixes in the name attribute (e.g. "rn:", "cpd:", etc) are optional.  They are absent from the following example:

```
<reaction name="R02215+R02219" type="reversible">
	<substrate name="C00410"/>
     	<product name="C05479"/>
</reaction>
```

Reactions may be either reversible or irreversible.  In the case of irreversible reactions, the direction of the reaction is implied by the naming of substrate and product elements (i.e. reactions flow from substrate to product.)  In practice, the direction of the reaction is best left specified as "reversible" in the GLAMM document, as the GLAMM rendering algorithms ignore this tag and the retrosynthesis algorithms are not sophisticated enough to make proper use of it at the time of this writing.