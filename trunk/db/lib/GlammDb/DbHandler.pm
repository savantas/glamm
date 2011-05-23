package GlammDb::DbHandler;

use strict;
# use GlammDb;
# use GlammDb::Entity;

################################################################################

#constructor
sub new {
	my $class 	= shift;
	my $self 	= {@_};
	bless ( $self, $class );
	$self->init();

	$self->parse()
		if ( defined $self->dataSource() && defined $self->dataSource()->path() );
	
	return $self;
}

################################################################################

sub init {
	# override in subclasss, if necessary
}

################################################################################

#get/setters
sub dataSource		{ $_[0]->{dataSource} = $_[1]			if defined $_[1]; $_[0]->{dataSource} }
sub entities 		{ push (@{$_[0]->{entities}}, $_[1]) 	if defined $_[1]; $_[0]->{entities} }

################################################################################

# this should be a unique mapping - emphasis on *should*
sub entityForXref { 
	
	my ( $self, $xrefName, $xrefId, $entity ) = @_;
		
	$self->{entityForXref}->{$xrefName}->{$xrefId} = $entity
		if defined $entity;
		
	return $self->{entityForXref}->{$xrefName}->{$xrefId};
	
}

################################################################################

sub parse {

	my $self 			= shift;
	my $dataSource		= $self->dataSource();
	
	if ( defined $dataSource ) {
		my $path 			= $dataSource->path()->[0];
		my $dbDescription	= $dataSource->description()->[0];
		
		print "Parsing $dbDescription database at $path...\n";
		$self->_parse();
		$self->_hashXrefs();
		print "Done\n";
	}
	
}

################################################################################

sub print {
	my $self = shift;
	
	map {
		$_->print();
	} @{$self->entities()} if defined $self->entities();
}


################################################################################

sub resolve {
	
	my ( $dbh0, $dbh1, $resolvedDataSource ) = @_;
	my $resolved = GlammDb::CompoundDbHandler->new( dataSource => $resolvedDataSource );
	
	printf ( "\tResolving %s and %s into %s.\n",	$dbh0->dataSource()->description()->[0], 
													$dbh1->dataSource()->description()->[0], 
													$resolved->dataSource()->description()->[0] );
	
	# for each entity in $dbh1...
	map {
		
		
		my $entity1 = $_;
		my $entity0 = $entity1->resolveAgainstDb( $dbh0 );

		# add the merged result to $resolved if we find an $entity0
		$resolved->entities( $entity0->merge( $entity1) )
			if defined $entity0;
		
	} @{ $dbh1->entities() };

	# hash xrefs of $resolved
	$resolved->_hashXrefs();
	
	# for each entity in $dbh0...
	map {
		
		my $entity = $_;
		my $resolvedEntity = $entity->resolveAgainstDb( $resolved );
		
		# add the entity to $resolved if we do NOT find a $resolvedEntity
		$resolved->entities( $entity )
			unless defined $resolvedEntity;
		
	} @{ $dbh0->entities() };
	
	$resolved->_hashXrefs();

	# for each entity in $dbh1...
	map {
		
		my $entity = $_;
		my $resolvedEntity = $entity->resolveAgainstDb( $resolved );
	
		# add the entity to $resolved if we do NOT find a $resolvedEntity
		$resolved->entities( $entity )
			unless defined $resolvedEntity;
		
	} @{ $dbh1->entities() };
	
	# rehash xrefs
	$resolved->_hashXrefs();

	return $resolved;
}

################################################################################

sub resolveMultiple {
	
	my $resolvedDbDescription = shift;
	my $resolved = ();
	my $resolvedDataSource = GlammDb::DataSource->new(	id				=>	[ "resolved" ],
														description		=>	[ $resolvedDbDescription ],
														type			=>	[ "cpd"	] );
	
	if ( scalar @_ == 1 ) {
		$resolved = shift;
	}
	elsif ( scalar @_ > 1 ) {
		my $a = shift;
		my $b = shift;
		$resolved = $a->resolve($b, $resolvedDataSource );
		map {
			$resolved = $resolved->resolve($_, $resolvedDataSource );
		} @_;
	}
	
	return $resolved;
}

################################################################################

sub serialize {
	
	my $self 		= shift;
	my $dataSource	= $self->dataSource();
	my $dbDescription = "undescribed database";
	
	$dbDescription = $dataSource->description()->[0]
		if defined $dataSource->description();
	
	print "Serializing $dbDescription...\n";
	
	map {
		$_->serialize( $self );
	} @{$self->entities()};
	
	print "Done\n";
}

################################################################################

sub _hashXrefs {
	
	my $self = shift;
	
	# nuke the hash first!
	$self->{entityForXref} = ();
	
	map {
		my $entity = $_;
		map {
			my ( $xrefName, $xrefId ) = $_ =~ /(\S+):\s+(\S+)/;
			$self->entityForXref( $xrefName, $xrefId, $entity );
		} @{ $entity->xrefs() } if defined $entity->xrefs();
	} @{ $self->entities() } if defined $self->entities();
	
}

################################################################################

1;