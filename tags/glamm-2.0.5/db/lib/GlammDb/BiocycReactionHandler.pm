package GlammDb::BiocycReactionHandler;

use strict;

#use GlammDb::DbHandler;

our @ISA = qw(GlammDb::ReactionDbHandler);

my $kBiocycReactionId			= "UNIQUE-ID";
my $kBiocycReactionDbLinks		= "DBLINKS";
my $kBiocycReactionLeft			= "LEFT";
my $kBiocycReactionRight		= "RIGHT";
my $kBiocycReactionDirection	= "REACTION-DIRECTION";
my $kBiocycReactionCoefficient	= "^COEFFICIENT";

my $kBiocycReactionTerminator	= "//";

sub _genNormalizedEquation {
	my ( $operator, $left, $right ) = @_;
	my $equation = "";
	my @leftTerms = ();
	my @rightTerms = ();
	
	while ( my ( $cpdId, $coefficient ) = each %$left ) {
		push ( @leftTerms, "($coefficient) $cpdId" );
	}
	
	while ( my ( $cpdId, $coefficient ) = each %$right ) {
		push ( @rightTerms, "($coefficient) $cpdId" );
	}
	
	$equation = join ( " + ", @leftTerms ) . " " . $operator . " " . join ( " + ", @rightTerms );
	
	return $equation;
}

sub _parse () {

	my $self = shift;
	my $path = $self->dataSource()->path()->[0];
	
	open ( FILE, $path ) or
		die "Could not open $path for reading";
		
	my $fieldId 			= "";
	my $formula				= "";
	my $reaction			= ();

	my %left				= ();
	my %right			 	= ();
	my $operator			= "<=>";
	
	my $lastCpdId			= "";
	my $lastRxnSide			= ();
	
	while ( my $line = <FILE> ) {
		
		# skip comments
		if ( $line =~ /^#/ ) {
			next;
		}
		
		# read fieldId and rest of line
		if ( $line =~ /^(\S+)\s*-*\s*(.*)/ ) {
			
			# set fieldId to new identifier
			$fieldId = $1;
				
			# parse the rest of the line
			$line = $2;
		}
		
		if ( $fieldId eq $kBiocycReactionId ) {
			
			# new reaction entry, so clear reaction hash and grab reaction ID
			$reaction = GlammDb::Reaction->new();
			my ($id) = $line =~ /(\S+)/;	# the ID should be the first field in the line
			$reaction->id( $id );
			$reaction->xrefs( "BIOCYC-RXN: $id" );
			$reaction->dataSource( $self->dataSource() );
			
			%left = ();
			%right = ();
			$operator = "<=>";
			
			$lastCpdId = "";
			$lastRxnSide = ();
	
		}
			
		elsif ( $fieldId eq $kBiocycReactionDbLinks ) {
			
			my ( $db, $dbId ) = $line =~ /\((\S+)\s+\"(\S+)\"/;
			if ( $db ne "" && $dbId ne "" && $dbId ne "NIL" ) {
				
				# for some reason, biocyc's KEGG ids aren't consistent in case - fix that!
				if ( $db eq "LIGAND-CPD" ) {
					$reaction->xrefs( "$db: " . uc $dbId );
				}
				else {
					$reaction->xrefs( "$db: $dbId" );
				}
			}
			
		}
		
		elsif ( $fieldId eq $kBiocycReactionLeft ) {
			# print "left: $line\n";
			my $cpdId = GlammDb::trim($line);
			$left{$cpdId} = 1;
			$lastCpdId = $cpdId;
			$lastRxnSide = \%left;
		}
		
		elsif ( $fieldId eq $kBiocycReactionRight ) {
			# print "right: $line\n";
			my $cpdId = GlammDb::trim($line);
			$right{$cpdId} = 1;
			$lastCpdId = $cpdId;
			$lastRxnSide = \%right;
		}
		
		elsif ( $fieldId eq $kBiocycReactionCoefficient ) {
			# print "coefficient: $line\n";
			my $coefficient = GlammDb::trim($line);
			$lastRxnSide->{$lastCpdId} = $coefficient;
		}
		
		elsif ( $fieldId eq $kBiocycReactionDirection ) {
			
			# operators a la palsson
			if ( $line =~ /REVERSIBLE/ ) {
				$operator = "<=>";
			}
			elsif ( $line =~ /LEFT-TO-RIGHT/ ) {
				$operator = "-->";
			}
		}
		
		elsif ( $fieldId eq $kBiocycReactionTerminator ) {
	
			my $equation = _genNormalizedEquation( $operator, \%left, \%right );
			
			$reaction->equation( $equation );
			$reaction->definition( $equation );
			$reaction->normalizedEquation( $equation );
			
			# $reaction->print();
		
			# end of entry, push reaction onto reactions array	
			$self->entities( $reaction )
				if $reaction->validateParticipants( $self );
			
			# print "-------------------------------------------------------------------\n";
			
		}
		
	}
		
	close ( FILE );

}
1;