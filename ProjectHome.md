Original code is taken from:
http://paulhorman.com/wikidoclet/ and ported to work for J2SE6

**Command Line Usage**
```
javadoc -docletpath /path/to/wikidoclet.1.0.4.jar -doclet wikidoclet.WikiDoclet [packagenames] [sourcefiles] ...
```

**ANT Usage**
```
<javadoc doclet="wikidoclet.WikiDoclet" docletpath="path/to/wikidoclet.1.0.4.jar"/ >
        ...
</javadoc>
```

Text Formatting Rules: [TWikiTextFormattingRules.htm](http://paulhorman.com/wikidoclet/TWikiTextFormattingRules.htm)

**License**
```
Copyright (C) 2003  Jason Horman (jason@jhorman.org)
Copyright (C) 2009  Santhosh Kumar T (santhosh.tekuri@gmail.com)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA
```

[Sun Microsystems License](http://paulhorman.com/wikidoclet/original_sun_license.txt)