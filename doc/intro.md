# Introduction to discobolus

Simple D64 image maker.

Notes

Image configuration hash-map

{:diskname <text>
 :file-seq [file-spec]}

file-spec:

{:track <int> :sector <int> (defaults to 0)
 :file <file> :file-name <text> (ignored if :raw true)
 :interleave <int> (ooptional, 10 by default)
 :raw <true|false> (optional, false by default)}

Process files that have track and sectors explicitly set, throw error
if unable to allocate.

sector-allocation function:

Usage:

(let [[sec-seq alloc-set] (sector-seq free-set file)])

Should return a sec-seq of [<track> <sector>] for that file, and the
remaining free sectors of the whole disk.

If a file is not raw, need to add +2 bytes per sector that will be
used for <track, sector> header at start of sector.

(Size of file / 254) round up to whole number is number of sectors
used.
