package GlammDb::KeggReactionHandler;

use strict;

our @ISA = qw(GlammDb::ReactionDbHandler);

my $kKeggReactionId				= "ENTRY";
my $kKeggReactionNames			= "NAME";
my $kKeggReactionDefinition		= "DEFINITION";
my $kKeggReactionEquation		= "EQUATION";
my $kKeggReactionPathway		= "PATHWAY";
my $kKeggReactionEnzymes		= "ENZYME";
my $kKeggReactionRpair			= "RPAIR";
my $kKeggReactionComment		= "COMMENT";
my $kKeggReactionTerminator		= "///";

################################################################################

sub _parse {
	
	my $self 		= shift;
	my $path 		= $self->dataSource()->path()->[0];
	
	my $fieldId		= "";
	my $reaction 	= ();
	
	my $equation 	= "";
	my $definition 	= "";
	
	open ( FILE, $path ) or
		die "Could not open $path for reading";
		
	while ( my $line = <FILE> ) {
		
		if ( $line =~ /^(\S+)(.*)/ ) {

			# we have encountered a new field identifier

			# set fieldId to new identifier
			$fieldId = $1;

			# parse the rest of the line
			$line = $2;
			
			# save equation
			if ( $equation ne "" ) {
				my $normalizedEquation = $self->_normalizeEquation( $equation );
				$reaction->normalizedEquation( $normalizedEquation );
				$reaction->equation( $equation );
				$equation = "";
			}
			
			# save definition
			if ( $definition ne "" ) {
				$reaction->definition( $definition );
				$definition = "";
			}

		}

		if ( $fieldId eq $kKeggReactionId ) {

			# new reaction entry, so clear reaction hash and grab reaction ID
			$reaction = GlammDb::Reaction->new();
			
			my ($id) = $line =~ /(\S+)/;	# the ID should be the first field in the line
			$reaction->id( $id );
			$reaction->xrefs( "LIGAND-RXN: $id" );
			$reaction->dataSource( $self->dataSource() );

		}

		elsif ( $fieldId eq $kKeggReactionNames ) {

			$line = GlammDb::trim ( $line );
			$line =~ s/;//g;	# GlammDb::trim semicolons
			$reaction->synonyms( $line )
		}

		elsif ( $fieldId eq $kKeggReactionEquation ) {
			$line = GlammDb::trim ( $line );
			if ( $equation eq "" ) {
				$equation = $line;
			}
			else {
				$equation .= " " . $line;
			}
		}

		elsif ( $fieldId eq $kKeggReactionDefinition ) {
			$line = GlammDb::trim ( $line );
			if ( $definition eq "" ) {
				$definition = $line;
			}
			else {
				$definition .= " " . $line;
			}
		}

		elsif ( $fieldId eq $kKeggReactionEnzymes ) {

			my @buf = ();
			$line = GlammDb::trim ( $line );
			@buf = split ( /\s+/, $line );
			map { $reaction->enzymes( $_ ); } @buf;

		}

		elsif ( $fieldId eq $kKeggReactionRpair ) {
			$line = GlammDb::trim ( $line );
			$reaction->rpair( $line );
		}

		elsif ( $fieldId eq $kKeggReactionTerminator ) {
			
			# validate reaction participants against their compound dbs
			# push reaction onto reactions array
			# push ( @{$self->{entities}}, $reaction )
			$self->entities( $reaction )
				if $reaction->validateParticipants( $self );
		}

	}
	
	close ( FILE );
	
}

################################################################################

sub _normalizeEquation {
	my ( $self, $equation ) = @_;
	my $normalizedEquation = "";
	
	my @terms 		= split ( /\s+/, $equation );

	map {
		my $term = $_;
		
		if ( $term =~ /(\+|\d+)*([CG]\d{5})(\S*)/ ) {
			if ( defined $1 ) {

				# annoying KEGG detritus - can't believe I even found this
				my $detritus = $1;
				
				if ( $detritus eq "+" ) {
					$normalizedEquation .= "+ ";
				}
				elsif ( $detritus =~ /\d+/ ) {
					$normalizedEquation .= "($detritus) ";
				}
				else {
					die "Unable to parse equation: $equation"
				}
			}
			$normalizedEquation .= "$2 ";
		}

		elsif ( $term =~ /(<=>|\+)/ ) {
			$normalizedEquation .= "$1 ";
		}

		elsif ( $term =~ /(\d+)/ ) {
			$normalizedEquation .= "($1) ";
		}
		
	} @terms;
	
	# chop off trailing whitespace
	chop $normalizedEquation;
	
	return $normalizedEquation;
}

1;