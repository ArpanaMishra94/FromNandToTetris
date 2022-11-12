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
D=M//25

