#!/usr/bin/perl

use strict;

use lib "../lib";

use GlammDb;

################################################################################

sub main {

	my @paths = ();
	
	GlammDb::init( "../data", "../data/output" );

	my $rootDataSource = GlammDb::DataSource->new( path => [ "../etc/db.xml" ], description => [ "data sources" ] );	
	my $dataSourceHandler = GlammDb::DataSourceHandler->new( dataSource => $rootDataSource );
	
	my $keggCpdDataSource		= $dataSourceHandler->entityForXref( "DATASOURCE", "keggCpd" );
	my $keggGlycanDataSource	= $dataSourceHandler->entityForXref( "DATASOURCE", "keggGlycan" );
	my $biocycCpdDataSource 	= $dataSourceHandler->entityForXref( "DATASOURCE", "biocycCpd" );
	my $palssonCpdDataSource 	= $dataSourceHandler->entityForXref( "DATASOURCE", "iJR904Cpd" );
	my $keggRxnDataSource 		= $dataSourceHandler->entityForXref( "DATASOURCE", "keggRxn" );
	my $biocycRxnDataSource		= $dataSourceHandler->entityForXref( "DATASOURCE", "biocycRxn" );
	my $palssonRxnDataSource 	= $dataSourceHandler->entityForXref( "DATASOURCE", "iJR904Rxn" );
	my $keggPathwayDataSource 	= $dataSourceHandler->entityForXref( "DATASOURCE", "keggPathway" );
	
	my $keggCpdHandler 		= GlammDb::KeggCompoundHandler->new( dataSource => $keggCpdDataSource );
	my $keggGlycanHandler	= GlammDb::KeggGlycanHandler->new( dataSource => $keggGlycanDataSource );
	my $biocycCpdHandler	= GlammDb::BiocycCompoundHandler->new( 	dataSource => $biocycCpdDataSource );																
	my $palssonCpdHandler	= GlammDb::PalssonCompoundHandler->new( dataSource => $palssonCpdDataSource );
	
	# resolve compound dbs
	print "\n";
	print "Resolving compound databases...\n";
	my $resolvedCpdHandler 	= GlammDb::DbHandler::resolveMultiple(	"resolved compounds", 
																		$keggCpdHandler,
																		$keggGlycanHandler,
																		$biocycCpdHandler, 
																		$palssonCpdHandler );	
																		
	print "Done!\n\n";
	
	my $keggRxnHandler		= GlammDb::KeggReactionHandler->new(	dataSource		=>	$keggRxnDataSource,
	 																cpdDb			=>	$resolvedCpdHandler,
	 																cpdDataSource	=>	$keggCpdDataSource	);
	
	my $biocycRxnHandler	= GlammDb::BiocycReactionHandler->new(	dataSource		=>	$biocycRxnDataSource,
																	cpdDb			=>	$resolvedCpdHandler,
																	cpdDataSource	=>	$biocycCpdDataSource	);	
																	
	my $palssonRxnHandler	= GlammDb::PalssonReactionHandler->new(	dataSource		=> 	$palssonRxnDataSource,
																	cpdDb			=>	$resolvedCpdHandler,
																	cpdDataSource	=>	$palssonCpdDataSource );
																	
	my $keggPathwayHandler	= GlammDb::KeggPathwayHandler->new(	dataSource		=>	$keggPathwayDataSource,
																cpdDb			=>	$resolvedCpdHandler,
																cpdDataSource	=>	$keggCpdDataSource,
																rxnDb			=>	$keggRxnHandler,
																rxnDataSource	=>	$keggRxnDataSource );
																
																	
	# summarize parsing results
	print "Summary:\n";
	print "\tkegg compounds: " . scalar @{$keggCpdHandler->entities()} . "\n";
	print "\tkegg glycans: " . scalar @{$keggGlycanHandler->entities()} . "\n";
	print "\tbiocyc compounds: " . scalar @{$biocycCpdHandler->entities()} . "\n";
	print "\tpalsson compounds: " . scalar @{$palssonCpdHandler->entities()} . "\n";
	print "\tresolved compounds: ". scalar @{$resolvedCpdHandler->entities()} . "\n";
	print "\n";
	print "\tkegg reactions: " . scalar @{$keggRxnHandler->entities()} . "\n";
	print "\tbiocyc reactions: " . scalar @{$biocycRxnHandler->entities()} . "\n";
	print "\tpalsson reactions: " . scalar @{$palssonRxnHandler->entities()} . "\n";
	print "\n";
	print "\tkegg pathways: " . scalar @{$keggPathwayHandler->entities()} . "\n";
	print "\n\n";
	
	# open csv file handles
	GlammDb::openFileHandles();
	
	# serialize in order - data sources, compounds, reactions, pathways
	$dataSourceHandler->serialize();
	$resolvedCpdHandler->serialize();
	$keggRxnHandler->serialize();
	$biocycRxnHandler->serialize();
	$palssonRxnHandler->serialize();
	$keggPathwayHandler->serialize();
	
	# serialize last GUID
	GlammDb::serializeLastGuid();
	
	# close csv file handles
	GlammDb::closeFileHandles();
}


################################################################################

main;

################################################################################

