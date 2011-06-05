package GlammDb::CompoundDbHandler;

use strict;

our @ISA = qw(GlammDb::DbHandler);

################################################################################

sub init {
	my $self = shift;
	die ( "dataSource invalid or not defined" )
		unless ( defined $self->dataSource() );
}

################################################################################

1;