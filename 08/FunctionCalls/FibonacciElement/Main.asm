@256
D=A
@SP
M=D

@RETURN_0
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@0
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
goto Sys.init
0;JMP
(RETURN_0)

//// This file is part of www.nand2tetris.org
//// and the book "The Elements of Computing Systems"
//// by Nisan and Schocken, MIT Press.
//// File name: projects/08/FunctionCalls/FibonacciElement/Main.vm
//
//// Computes the n'th element of the Fibonacci series, recursively.
//// n is given in argument[0].  Called by the Sys.init function 
//// (part of the Sys.vm file), which also pushes the argument[0] 
//// parameter before this code starts running.
//
//function Main.fibonacci 0
(Main.fibonacci)

//push argument 0
@ARG
D=M
@0
D=D+A
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1

//push constant 2
@2
D=A
@SP
A=M
M=D
@SP
M=M+1

//lt                     // checks if n<2
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
@87
D;JLT//->true
@SP
A=M-1
M=0
@90
0;JMP//->true
@SP
A=M-1
M=-1


//if-goto IF_TRUE
@SP
M=M-1
A=M
D=M
@IF_TRUE
D;JNE

//goto IF_FALSE
@IF_FALSE
0;JMP

//label IF_TRUE          // if n<2, return n
(IF_TRUE)

//push argument 0        
@ARG
D=M
@0
D=D+A
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1

//return
@LCL
D=M
@FRAME
M=D
@5
A=D-A
D=M
@RET
M=D
@SP
A=M-1
D=M
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D+1
@1
D=A
@FRAME
A=M-D
D=M
@THAT
M=D
@2
D=A
@FRAME
A=M-D
D=M
@THIS
M=D
@3
D=A
@FRAME
A=M-D
D=M
@ARG
M=D
@4
D=A
@FRAME
A=M-D
D=M
@LCL
M=D
@RET
A=M
0;JMP

//label IF_FALSE         // if n>=2, returns fib(n-2)+fib(n-1)
(IF_FALSE)

//push argument 0
@ARG
D=M
@0
D=D+A
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1

//push constant 2
@2
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

//call Main.fibonacci 1  // computes fib(n-2)
@RETURN_1
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@1
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
goto Main.fibonacci
0;JMP
(RETURN_1)

//push argument 0
@ARG
D=M
@0
D=D+A
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1

//push constant 1
@1
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

//call Main.fibonacci 1  // computes fib(n-1)
@RETURN_2
D=A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@SP
A=M
M=D
@SP
M=M+1
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
@THAT
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
D=M
@1
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
goto Main.fibonacci
0;JMP
(RETURN_2)

//add                    // returns fib(n-1) + fib(n-2)
//->add
@SP
M=M-1
A=M
D=M
@SP
A=M-1
M=M+D

//return
@LCL
D=M
@FRAME
M=D
@5
A=D-A
D=M
@RET
M=D
@SP
A=M-1
D=M
@ARG
A=M
M=D
@ARG
D=M
@SP
M=D+1
@1
D=A
@FRAME
A=M-D
D=M
@THAT
M=D
@2
D=A
@FRAME
A=M-D
D=M
@THIS
M=D
@3
D=A
@FRAME
A=M-D
D=M
@ARG
M=D
@4
D=A
@FRAME
A=M-D
D=M
@LCL
M=D
@RET
A=M
0;JMP

