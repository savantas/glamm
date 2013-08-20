package GlammDb::ReactionDbHandler;

use strict;

our @ISA = qw(GlammDb::DbHandler);

################################################################################

sub cpdDb			{ $_[0]->{cpdDb} = $_[1] 			if defined $_[1]; $_[0]->{cpdDb} } 				# the compoundDb
sub cpdDataSource 	{ $_[0]->{cpdDataSource} = $_[1]	if defined $_[1]; $_[0]->{cpdDataSource} } 	

################################################################################

sub init {
	
	my $self = shift;

	die ( "dataSource not defined" )
		unless ( defined $self->dataSource() );
		
	die ( "cpdDb not defined" )
		unless ( defined $self->cpdDb() );
	
	die ( "cpdDataSource not defined" )
		unless ( defined $self->cpdDataSource() );
		
}

################################################################################

1;