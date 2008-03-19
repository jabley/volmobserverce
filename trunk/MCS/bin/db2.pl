#!/usr/bin/perl -w
#
# Converts Volantis sql92 database scripts into a suitable format for db2.
# All sql scripts in the source directory are converted and written out to the
# destination directory with the same filenames.
#
# Usage: db2.pl <srcdir> <destdir>

use strict;
use File::Find;
use File::Basename;

die "Usage: db2.pl <srcdir> <destdir>\n" if @ARGV != 2;

my $srcDir = $ARGV[0];
my $destDir = $ARGV[1];

my $vmIndex = 0;
my $vsIndex = 0;

# Find all the sql files in the src dir and call the process subroutine.
# We remain in the current directory.
find({wanted => \&process, preprocess => \&sqlonly, no_chdir => 1}, $srcDir);

# Given a list of files, filter out all files that don't end in .sql
sub sqlonly()
{
    my @files = ();

    foreach my $filename (@_)
    {
        if ($filename =~ /\.sql/)
        {
            push(@files, $filename);
        }
    }
    return @files;
}

# Processes each sql file
sub process()
{
    my $path = $_;

    # Skip past the src directory. Why is this present?
    # The src directory . is not returned by sqlonly()'s list of files.
    if ($path eq $srcDir)
    {
        return 0;
    }

    # Extract the filename from the path passed in
    my $file = basename($path);

    # Open the src and destination sql files
    open SRC, "<$path" or die ("Cannot read $path: $!");
    open DEST, ">$destDir/$file" or die ("Cannot create $destDir/$file: $!");

    while (my $line = <SRC>)
    {
        chomp;

        # Truncate all INDEX names down to 18 characters
        # Index names start with PK_VM or PK_VS
        if ($line =~ /(PK_V\w+)/)
        {
            # Get the first 15 characters and append on
            # a unique digit.
            my $newIndex = substr($1, 0, 15);
            if ($newIndex =~ /PK_VM/)
            {
                $line =~ s/PK_V\w+/$newIndex$vmIndex/;
                $vmIndex++;
            }
            else
            {
                $line =~ s/PK_V\w+/$newIndex$vsIndex/;
                $vsIndex++;
            }
        }

        # DROP TABLE statements don't need CASCADE CONSTRAINTs in order to
        # remove the table and any indices associated with it.
        $line =~ s/CASCADE.*CONSTRAINTS//;

        print DEST "$line";
    }

    close SRC;
    close DEST;
}



#
# ===========================================================================
# Change History
# ===========================================================================
# $Log$

# 30-Jun-03	440/6	chrisw	VBM:2003061802 Add line to write out destination file

# 27-Jun-03	440/4	chrisw	VBM:2003061802 Stop reusing java.sql.Statement objects

# 23-Jun-03	440/1	chrisw	VBM:2003061802 perl script to convert our sql92 scripts to db2 format

# ===========================================================================
#
