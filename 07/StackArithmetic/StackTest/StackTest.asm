//// This file is part of www.nand2tetris.org
//// and the book "The Elements of Computing Systems"
//// by Nisan and Schocken, MIT Press.
//// File name: projects/07/StackArithmetic/StackTest/StackTest.vm
//
//// Executes a sequence of arithmetic and logical operations
//// on the stack. 
//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1

//eq
//->equl
//->sub
@SP
M=M-1
@SP
A=M
D=M
@SP
A=M-1
M=M-D
@SP
A=M-1
D=M
@32
D;JEQ//->true
@SP
A=M-1
M=0
@35
0;JMP//->true
@SP
A=M-1
M=-1


//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 16
@16
D=A
@SP
A=M
M=D
@SP
M=M+1

//eq
//->equl
//->sub
@SP
M=M-1
@SP
A=M
D=M
@SP
A=M-1
M=M-D
@SP
A=M-1
D=M
@67
D;JEQ//->true
@SP
A=M-1
M=0
@70
0;JMP//->true
@SP
A=M-1
M=-1


//push constant 16
@16
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 17
@17
D=A
@SP
A=M
M=D
@SP
M=M+1

//eq
//->equl
//->sub
@SP
M=M-1
@SP
A=M
D=M
@SP
A=M-1
M=M-D
@SP
A=M-1
D=M
@102
D;JEQ//->true
@SP
A=M-1
M=0
@105
0;JMP//->true
@SP
A=M-1
M=-1


//push constant 892
@892
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1

//lt
//->lessThan
//->sub
@SP
M=M-1
@SP
A=M
D=M
@SP
A=M-1
M=M-D
@SP
A=M-1
D=M
@137
D;JLT//->true
@SP
A=M-1
M=0
@140
0;JMP//->true
@SP
A=M-1
M=-1


//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 892
@892
D=A
@SP
A=M
M=D
@SP
M=M+1

//lt
//->lessThan
//->sub
@SP
M=M-1
@SP
A=M
D=M
@SP
A=M-1
M=M-D
@SP
A=M-1
D=M
@172
D;JLT//->true
@SP
A=M-1
M=0
@175
0;JMP//->true
@SP
A=M-1
M=-1


//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 891
@891
D=A
@SP
A=M
M=D
@SP
M=M+1

//lt
//->lessThan
//->sub
@SP
M=M-1
@SP
A=M
D=M
@SP
A=M-1
M=M-D
@SP
A=M-1
D=M
@207
D;JLT//->true
@SP
A=M-1
M=0
@210
0;JMP//->true
@SP
A=M-1
M=-1


//push constant 32767
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1

//gt
//->greaterThan
//->sub
@SP
M=M-1
@SP
A=M
D=M
@SP
A=M-1
M=M-D
@SP
A=M-1
D=M
@242
D;JGT//->true
@SP
A=M-1
M=0
@245
0;JMP//->true
@SP
A=M-1
M=-1


//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 32767
@32767
D=A
@SP
A=M
M=D
@SP
M=M+1

//gt
//->greaterThan
//->sub
@SP
M=M-1
@SP
A=M
D=M
@SP
A=M-1
M=M-D
@SP
A=M-1
D=M
@277
D;JGT//->true
@SP
A=M-1
M=0
@280
0;JMP//->true
@SP
A=M-1
M=-1


//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 32766
@32766
D=A
@SP
A=M
M=D
@SP
M=M+1

//gt
//->greaterThan
//->sub
@SP
M=M-1
@SP
A=M
D=M
@SP
A=M-1
M=M-D
@SP
A=M-1
D=M
@312
D;JGT//->true
@SP
A=M-1
M=0
@315
0;JMP//->true
@SP
A=M-1
M=-1


//push constant 57
@57
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 31
@31
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 53
@53
D=A
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

//push constant 112
@112
D=A
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

//neg
//->neg
@SP
A=M-1
M=-M

//and
//->or
@SP
M=M-1
A=M
D=M
@SP
A=M-1
M=D&M

//push constant 82
@82
D=A
@SP
A=M
M=D
@SP
M=M+1

//or
//->or
@SP
M=M-1
A=M
D=M
@SP
A=M-1
M=D|M

//not
//->not
@SP
A=M-1
M=!M

