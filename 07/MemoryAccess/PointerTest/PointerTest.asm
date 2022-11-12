//// This file is part of www.nand2tetris.org
//// and the book "The Elements of Computing Systems"
//// by Nisan and Schocken, MIT Press.
//// File name: projects/07/MemoryAccess/PointerTest/PointerTest.vm
//
//// Executes pop and push commands using the 
//// pointer, this, and that segments.
//push constant 3030
@3030
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop pointer 0
@SP
M=M-1
A=M
D=M
@3
M=D
//push constant 3040
@3040
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop pointer 1
@SP
M=M-1
A=M
D=M
@4
M=D
//push constant 32
@32
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop this 2
@THIS
D=M
@R13
M=D
@2
D=A
@R13
M=D+M
@SP
M=M-1
A=M
D=M
@R13
A=M
M=D

//push constant 46
@46
D=A
@SP
A=M
M=D
@SP
M=M+1

//pop that 6
@THAT
D=M
@R13
M=D
@6
D=A
@R13
M=D+M
@SP
M=M-1
A=M
D=M
@R13
A=M
M=D

//push pointer 0
@3
D=M
@SP
A=M
M=D
@SP
M=M+1
//push pointer 1
@4
D=M
@SP
A=M
M=D
@SP
M=M+1
//add
//->add
@SP
M=M-1
A=M
D=M
@SP
A=M-1
M=M+D

//push this 2
@THIS
D=M
@2
D=D+A
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1

//sub
//->sub
@SP
M=M-1
@SP
A=M
D=M
@SP
A=M-1
M=M-D

//push that 6
@THAT
D=M
@6
D=D+A
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1

//add
//->add
@SP
M=M-1
A=M
D=M
@SP
A=M-1
M=M+D

