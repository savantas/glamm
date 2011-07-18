package GlammDb::BiocycCompoundHandler;

use strict;

#use GlammDb::DbHandler;

our @ISA = qw(GlammDb::CompoundDbHandler);

my $kBiocycCompoundId			= "UNIQUE-ID";
my $kBiocycCompoundCommonName	= "COMMON-NAME";
my $kBiocycCompoundFormula		= "CHEMICAL-FORMULA";
my $kBiocycCompoundDbLinks		= "DBLINKS";
my $kBiocycCompoundMass			= "MOLECULAR-WEIGHT";
my $kBiocycCompoundSmiles		= "SMILES";
my $kBiocycCompoundSynonyms		= "SYNONYMS";
my $kBiocycCompoundTypes		= "TYPES";
my $kBiocycCompoundInchi		= "INCHI";
my $kBiocycCompoundTerminator	= "//";

sub _parse () {

	my $self = shift;
	my $path = $self->dataSource()->path()->[0];
	
	open ( FILE, $path ) or
		die "Could not open $path for reading";
		
	my $fieldId 			= "";
	my $formula				= "";
	my $compound			= ();

	while ( my $line = <FILE> ) {
		
		# skip comments
		if ( $line =~ /^#/ ) {
			next;
		}
		
		# read fieldId and rest of line
		if ( $line =~ /^(\S+)\s*-*\s*(.*)/ ) {
			
			if ( $1 ne $fieldId ) {

				# we have encountered a new field identifier
				if ( defined $formula && $formula ne "" ) {
					$compound->formula( $formula );
				}
				$formula = "";
			
				# set fieldId to new identifier
				$fieldId = $1;
				
			}
			
			# parse the rest of the line
			$line = $2;
				
		}
		
		if ( $fieldId eq $kBiocycCompoundId ) {
			
			# new compound entry, so clear compound hash and grab compound ID
			$compound = GlammDb::Compound->new();
			my ($id) = $line =~ /(\S+)/;	# the ID should be the first field in the line
			$compound->id( $id );
			$compound->xrefs( "BIOCYC-CPD: $id" );
			$compound->dataSource( $self->dataSource() );
	
		}
		
		elsif ( $fieldId eq $kBiocycCompoundMass ) { 
			$compound->mass( GlammDb::trim ($line ) );
		}
		
		elsif ( $fieldId eq $kBiocycCompoundInchi ) { 
			$compound->inchi( GlammDb::trim ( $line ) ); 
		}
		
		elsif ( $fieldId eq $kBiocycCompoundCommonName ) { 
			$compound->synonyms( GlammDb::trim ( $line ) );
		}
		
		elsif ( $fieldId eq $kBiocycCompoundSynonyms ) { 
			$compound->synonyms( GlammDb::trim ( $line ) );
		} 
		
		elsif ( $fieldId eq $kBiocycCompoundSmiles ) { 
			$compound->smiles( GlammDb::trim ( $line ) );
		}
		
		elsif ( $fieldId eq $kBiocycCompoundTypes ) {
			$compound->types( GlammDb::trim ( $line ) );
		}
		
		elsif ( $fieldId eq $kBiocycCompoundDbLinks ) {
			
			my ( $db, $dbId ) = $line =~ /\((\S+)\s+\"(\S+)\"/;
			if ( $db ne "" && $dbId ne "" && $dbId ne "NIL" ) {
				
				# for some reason, biocyc's KEGG ids aren't consistent in case - fix that!
				if ( $db eq "LIGAND-CPD" ) {
					$compound->xrefs( "$db: " . uc $dbId );
				}
				else {
					$compound->xrefs( "$db: $dbId" );
				}
			}
			
		}
		
		elsif ( $fieldId eq $kBiocycCompoundFormula ) {
			
			my ( $element, $quantity ) = $line =~ /\((\S+)\s*(\S+)\)/;
			if ( $quantity eq "1" ) {
				$formula = $formula . $element;
			}
			else {
				$formula = $formula . $element . $quantity;
			}
		}
		
		elsif ( $fieldId eq $kBiocycCompoundTerminator ) {
		
			# end of entry, push compound onto compounds array	
			$self->entities( $compound );
			# push ( @{$self->{entities}}, $compound );
			
		}
		
	}
		
	close ( FILE );

}
1;