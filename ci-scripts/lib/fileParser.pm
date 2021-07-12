package fileParser;
use strict;
use warnings;
use base 'Exporter';
our $VERSION = '1.01';
our @EXPORT = qw(grepSingleLine);

sub grepSingleLine {
    my $inputFile = shift;
    my $searchExpression = shift;

    open(FH, '<', $inputFile) or die ("${!}: $inputFile!\n");
    while(<FH>) {
        my @matches = $_ =~ $searchExpression;
        if (@matches) {
            close(FH);
            return @matches;
        }
    }
    die("Failed to parse file $inputFile");
}
