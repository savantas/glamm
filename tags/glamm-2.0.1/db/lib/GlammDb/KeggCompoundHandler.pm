package GlammDb::KeggCompoundHandler;

use strict;

#use GlammDb::DbHandler;

our @ISA = qw(GlammDb::CompoundDbHandler);

my $kKeggCompoundId 			= "ENTRY";
my $kKeggCompoundNames			= "NAME";
my $kKeggCompoundFormula		= "FORMULA";
my $kKeggCompoundMass			= "MASS";
my $kKeggCompoundReactions		= "REACTION";
my $kKeggCompoundDbLinks		= "DBLINKS";
my $kKeggCompoundEnzymes		= "ENZYME";
my $kKeggCompoundTerminator		= "///";

sub _parse () {

	my $self = shift;
	my $path = $self->dataSource()->path()->[0];
	
	open ( FILE, $path ) or
		die "Could not open $path for reading";
		
	my $fieldId 			= "";
	my $compound			= ();

	while ( my $line = <FILE> ) {

		if ( $line =~ /^(\S+)(.*)/ ) {
			# we have encountered a new field identifier
			# set fieldId to new identifier
			$fieldId = $1;

			# parse the rest of the line
			$line = $2;
		}

		if ( $fieldId eq $kKeggCompoundId ) {

			# new compound entry, so clear compound hash and grab compound ID
			$compound = GlammDb::Compound->new();
			my ( $id ) = $line =~ /(\S+)/;	# the ID should be the first field in the line
			$compound->id( $id );
			$compound->xrefs( "LIGAND-CPD: $id" ); # special case this for ease of hashing
			$compound->dataSource( $self->dataSource() );

		}

		elsif ( $fieldId eq $kKeggCompoundNames ) {

			$line = GlammDb::trim ( $line );
			$line =~ s/;//g;	# trim semicolons
			if( !defined $compound->commonName() ) {
				$compound->commonName( $line );
			}
			else { 
				$compound->synonyms( $line );
			}
		}

		# KEGG formulae and masses are the preferred formulae and masses
		elsif ( $fieldId eq $kKeggCompoundFormula ) {

			$line = GlammDb::trim ( $line );
			$compound->prefFormula( $line );

		}

		elsif ( $fieldId eq $kKeggCompoundMass ) {
			$line = GlammDb::trim ( $line );
			$compound->prefMass( $line );	
		}

		elsif ( $fieldId eq $kKeggCompoundReactions ) {

			my @buf = ();
			$line = GlammDb::trim ( $line );
			@buf = split ( / /, $line );
			map { $compound->reactions( $_ ); } @buf;

		}

		elsif ( $fieldId eq $kKeggCompoundDbLinks ) {

			$line = GlammDb::trim ( $line );
			$compound->xrefs( $line );
		}

		elsif ( $fieldId eq $kKeggCompoundEnzymes ) {

			my @buf = ();
			$line = GlammDb::trim ( $line );
			@buf = split ( /\t/, $line );
			map { $compound->enzymes( $_ ); } @buf;

		}

		elsif ( $fieldId eq $kKeggCompoundTerminator ) {
			$self->entities( $compound );
		}
	}
		
	close ( FILE );
	
}
1;