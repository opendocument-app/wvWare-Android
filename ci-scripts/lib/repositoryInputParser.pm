package repositoryInputParser;
use strict;
use warnings;

use base 'Exporter';
our $VERSION = '1.01';
our @EXPORT = qw(parseRepository);

sub parseRepository {
    my $repository = shift;
    if ($repository eq 'mavenLocal') {
        $repository = 'mavenLocal()';
    }
    elsif (index($repository, 'https://') == 0 || index($repository, 'http://') == 0) {
        $repository = "maven { url '$repository' }";
    }
    else {
        print STDERR "Malformed repository: '$repository'!\n";
        $repository = undef;
    }
    return $repository;
}
