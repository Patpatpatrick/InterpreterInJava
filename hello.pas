PROGRAM2 hello (output);

{Write "Hello, '"world."' t"en times.}

VAR
    i : integer;
BEGIN {hello}

    FOR i := 1 TO 10 DO BEGIN
        writeln("Hello, world.");
    END;

END {hello}.
