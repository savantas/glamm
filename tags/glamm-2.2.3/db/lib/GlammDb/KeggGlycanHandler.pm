package GlammDb::KeggGlycanHandler;

use strict;

our @ISA = qw(GlammDb::CompoundDbHandler);

my $kKeggGlycanId 			= "ENTRY";
my $kKeggGlycanNames		= "NAME";
my $kKeggGlycanComposition	= "COMPOSITION";
my $kKeggGlycanMass			= "MASS";
my $kKeggGlycanRemark		= "REMARK";
my $kKeggGlycanReactions	= "REACTION";
my $kKeggGlycanDbLinks		= "DBLINKS";
my $kKeggGlycanEnzymes		= "ENZYME";
my $kKeggGlycanTerminator	= "///";

sub _parse () {

	my $self = shift;
	my $path = $self->dataSource()->path()->[0];
	
	open ( FILE, $path ) or
		die "Could not open $path for reading";
		
	my $fieldId 			= "";
	my $glycan			= ();

	while ( my $line = <FILE> ) {

		if ( $line =~ /^(\S+)(.*)/ ) {
			# we have encountered a new field identifier
			# set fieldId to new identifier
			$fieldId = $1;

			# parse the rest of the line
			$line = $2;
		}

		if ( $fieldId eq $kKeggGlycanId ) {

			# new glycan entry, so clear glycan hash and grab glycan ID
			$glycan = GlammDb::Compound->new();
			my ( $id ) = $line =~ /(\S+)/;	# the ID should be the first field in the line
			$glycan->id( $id );
			$glycan->xrefs( "LIGAND-CPD: $id" ); # special case this for ease of hashing
			$glycan->dataSource( $self->dataSource() );

		}

		elsif ( $fieldId eq $kKeggGlycanNames ) {
			
			$line = GlammDb::trim ( $line );
			$line =~ s/;//g;	# trim semicolons
			$glycan->synonyms( $line )
			
		}
		
		elsif ( $fieldId eq $kKeggGlycanComposition ) {
			$line = GlammDb::trim ( $line );
			$glycan->formula( $line );
		}
		
		elsif ( $fieldId eq $kKeggGlycanRemark ) {
			if ( my ($cpdIdString) = $line =~ /Same as:\s+(.*)/ ) {
				my $cpdIdString = GlammDb::trim( $cpdIdString );
				my @cpdIds = split ( / /, $cpdIdString );
				map {
					$glycan->xrefs( "LIGAND-CPD: $_" );
				} @cpdIds;
			}
		}

		elsif ( $fieldId eq $kKeggGlycanMass ) {

			$line = GlammDb::trim ( $line );
			my ( $mass ) = $line =~ /^(\S+)/;
			$glycan->mass( $mass );	
		}

		elsif ( $fieldId eq $kKeggGlycanReactions ) {

			my @buf = ();
			$line = GlammDb::trim ( $line );
			@buf = split ( / /, $line );
			map { $glycan->reactions( $_ ); } @buf;

		}

		elsif ( $fieldId eq $kKeggGlycanEnzymes ) {

			my @buf = ();
			$line = GlammDb::trim ( $line );
			@buf = split ( /\t/, $line );
			map { $glycan->enzymes( $_ ); } @buf;

		}

		elsif ( $fieldId eq $kKeggGlycanTerminator ) {
			$self->entities( $glycan );
		}
	}
		
	close ( FILE );
	
}

1;