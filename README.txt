Krysta Knight - 12/13/16
Compilers 
CS 4280
Final Project

This project takes .fs16 code and compiles it into .asm code.

Project was ran and compiled in a unix environment.

-------How to run--------
Will take input from the keyboard if invoked as: comp
Otherwise: comp filename

------------Files------------
comp.java : main executable
ScannerIn.java : Reads in file and creates tokens
Parser.java : takes tokens from scanner and parses them checking for static semantics
codeGenjava : takes tokens from scanner and outputs .asm code
SS.java : stack support
support.java : general supporting functions
Token.java : token class
Node.java : node class

project.fs16 : input file
out.asm : output file
