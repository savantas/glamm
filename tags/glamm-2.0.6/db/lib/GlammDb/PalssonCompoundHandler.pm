package GlammDb::PalssonCompoundHandler;

use strict;

our @ISA = qw(GlammDb::CompoundDbHandler);

sub _parse {
	
	my $self = shift;
	my $path = $self->dataSource->path()->[0];
	
	# $self->xrefName( "BIGG-CPD" );
	
	open ( FILE, $path ) or
		die "Could not open $path for reading";
		
	while ( my $line = <FILE> ) {
		
		my $compound	= GlammDb::Compound->new();
		my @fields 		= split ( '\t', $line );
		
		$compound->id( $fields[0] );
		$compound->synonyms( $fields[1] );
		$compound->formula( $fields[2] );
		$compound->charge( $fields[3] );
		$compound->dataSource( $self->dataSource() );
		
		# compartments are split by the sequence ', '
		{
			my @compartments = split ( ', ', $fields[4] );
			map {
				$compound->compartments( $_ );
			} @compartments;
		}
		
		if ( defined $fields[5] && $fields[5] ne "" && lc $fields[5] ne "none") {
			$compound->xrefs( "LIGAND-CPD: " . $fields[5] );
		}
		
		if ( defined $fields[6] && $fields[6] ne "" && lc $fields[6] ne "none") {
			$compound->xrefs( "CAS: " . $fields[6] );
		}
		
		$compound->xrefs( "BIGG-CPD: " . $compound->id()->[0] );
		
		# push compound onto compounds array
		$self->entities( $compound );
		# push ( @{$self->{entities}}, $compound );
		
		
	}
	
	close ( FILE );
	
}

1;