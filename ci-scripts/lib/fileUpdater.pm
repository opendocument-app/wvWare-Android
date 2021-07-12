package fileUpdater;
use warnings;
use strict;

use base 'Exporter';
our $VERSION = '1.01';

use File::Find 'find';
use File::Path 'make_path';

use File::Basename 'dirname';
use lib dirname(__FILE__);
use pathResolver 'getAbsolutePath';

sub new {
    my ($class, $args) = @_;
    my $self = {
        rootDirectory          => $args->{rootDirectory},
        outputDirectory        => $args->{outputDirectory}
    };
    bless $self, $class;

    $self->{rootDirectory} = getAbsolutePath($self->{rootDirectory});
    if ($self->{outputDirectory}) {
        # 1: Check if dir does not exist
        # 2: Get absolute path without resolving (because resolving requires dir to actually exist)
        # 3: mkdir
        # 4: Resolve path
        $self->{outputDirectory} = getAbsolutePath($self->{outputDirectory}, { doResolvePathAndCheckIfExists => 0 });
        -e $self->{outputDirectory} && die("Output directory $self->{outputDirectory} already exists!\n");
        File::Path::make_path($self->{outputDirectory}, { chmod => 0755 });
        $self->{outputDirectory} = getAbsolutePath($self->{outputDirectory}, { doResolvePathAndCheckIfExists => 1 });
    }

    return $self;
}

sub update {
    my $self = shift;
    my $input = shift;
    my $lineUpdateExpressionsRef = shift if (@_);
    my $filenameUpdateExpressionsRef = shift if (@_);

    my $recursion = sub {
        $self->update(shift, $lineUpdateExpressionsRef, $filenameUpdateExpressionsRef);
    };

    if ('ARRAY' eq ref($input)) {
        foreach my $file (@$input) {
            &$recursion($file);
        }
        return;
    }
    elsif ('' ne ref($input)) {
        use Data::Dumper;
        die('Unrecognized input: ' . Dumper($input));
    }

    my $inputFile = getAbsolutePath($input, {
        pathRelativeTo                => $self->{rootDirectory},
        doResolvePathAndCheckIfExists => 1
    });

    -e $inputFile or die("Input $inputFile does not exist!\n");
    if (-d _) {
        File::Find::find(sub {
            &$recursion($File::Find::name) if (-f $File::Find::name);
        }, $inputFile);
    }
    elsif (-f _) {
        $self->__updateSingleFile($inputFile, $lineUpdateExpressionsRef, $filenameUpdateExpressionsRef);
    }
}

sub __updateSingleFile {
    my $self = shift;
    my $inputFile = shift;
    my $lineUpdateExpressionsRef = shift if (@_);
    my $filenameUpdateExpressionsRef = shift if (@_);

    my $outputFile = $inputFile;
    my $inPlaceEdit = !$self->{outputDirectory};
    if ($inPlaceEdit != 1) {
        $outputFile = $self->{outputDirectory} . substr($inputFile, length($self->{rootDirectory}));
    }

    if (defined $filenameUpdateExpressionsRef) {
        $outputFile = &$filenameUpdateExpressionsRef($outputFile, $inputFile);
        if (!defined $outputFile) {
            return;
        }
    }

    open(my $FH_INPUT, '<', $inputFile) or die "$! : $inputFile\n";
    if ($inPlaceEdit == 1) {
        rename($inputFile, $inputFile . '.orig');
        $inputFile = $inputFile . '.orig';
    }
    else {
        File::Path::make_path(File::Basename::dirname($outputFile), { chmod => 0755 });
    }

    open(my $FH_OUTPUT, '>', $outputFile) or die "$! : $outputFile\n";
    while (<$FH_INPUT>) {
        if (defined $lineUpdateExpressionsRef) {
            $_ = &$lineUpdateExpressionsRef($_);
        }
        print $FH_OUTPUT $_;
    }

    my $mode = (stat($FH_INPUT))[2] & 07777;
    close($FH_INPUT);
    chmod($mode, $FH_OUTPUT);
    close($FH_OUTPUT);

    if ($inPlaceEdit == 1) {
        unlink($inputFile);
    }
}
