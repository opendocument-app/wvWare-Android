package pathResolver;
use strict;
use warnings;

use base 'Exporter';
our $VERSION = '1.01';
our @EXPORT = qw(getAbsolutePath getAbsolutePathOfBasedir);

sub getAbsolutePath {
    use Cwd qw/abs_path getcwd/;

    my $path = shift;
    my $options = shift if @_;
    if (!defined($options->{pathRelativeTo})) {
        $options->{pathRelativeTo} = Cwd::getcwd;
    }
    if (!defined($options->{doResolvePathAndCheckIfExists})) {
        $options->{doResolvePathAndCheckIfExists} = 1;
    }

    # Implementation taken and heavily modified from
    # https://stackoverflow.com/questions/39275327/check-if-a-directory-exists-as-an-absolute-path/39275654#39275654
    if ($path =~ /^~/) {
        $path =~ s/^~/$ENV{HOME}/;
    }
    if ($path !~ m#^/#) {
        $path = $options->{pathRelativeTo} . '/' . $path;
    }

    if ($options->{doResolvePathAndCheckIfExists}) {
        # Cwd::abs_path() does not work on non-existent paths
        my $pathResolved = Cwd::abs_path($path);
        if (!$pathResolved || !-e $path) {
            die "Failed to resolve path: $path!\n";
        }
        $path = $pathResolved;
    }

    return $path;
}

sub getAbsolutePathOfBasedir {
    my $path = shift;
    my $basedirCount = shift;

    $path = getAbsolutePath($path);
    foreach (1..$basedirCount) {
        $path = File::Basename::dirname($path);
    }
    $path = getAbsolutePath($path);
    return $path;
}
