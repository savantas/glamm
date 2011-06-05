package GlammDb::PathwayDbHandler;

use strict;

our @ISA = qw(GlammDb::DbHandler);

################################################################################

sub cpdDb			{ $_[0]->{cpdDb} = $_[1] 			if defined $_[1]; $_[0]->{cpdDb} } 				# the compound db
sub cpdDataSource 	{ $_[0]->{cpdDataSource} = $_[1]	if defined $_[1]; $_[0]->{cpdDataSource} }
sub rxnDb			{ $_[0]->{rxnDb} = $_[1] 			if defined $_[1]; $_[0]->{rxnDb} } 				# the reaction db
sub rxnDataSource 	{ $_[0]->{rxnDataSource} = $_[1]	if defined $_[1]; $_[0]->{rxnDataSource} }

################################################################################

sub init {
	
	my $self = shift;
	
	die ( "dataSource not defined" )
		unless ( defined $self->dataSource() );
		
	die ( "cpdDb not defined" )
		unless ( defined $self->cpdDb() );
	
	die ( "cpdDataSource not defined" )
		unless ( defined $self->cpdDataSource() );
		
	die ( "rxnDb not defined" )
		unless ( defined $self->cpdDb() );

	die ( "rxnDataSource not defined" )
		unless ( defined $self->rxnDataSource() );
}

################################################################################

1;