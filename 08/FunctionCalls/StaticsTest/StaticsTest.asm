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
@Sys.init
0;JMP
(RETURN_0)

//// This file is part of www.nand2tetris.org
//// and the book "The Elements of Computing Systems"
//// by Nisan and Schocken, MIT Press.
//// File name: projects/08/FunctionCalls/StaticsTest/Class1.vm
//
//// Stores two supplied arguments in static[0] and static[1].
//function Class1.set 0
(Class1.set)

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

//pop static 0
@SP
M=M-1
A=M
D=M
@Class1.vm.0
M=D

//push argument 1
@ARG
D=M
@1
D=D+A
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1

//pop static 1
@SP
M=M-1
A=M
D=M
@Class1.vm.1
M=D

//push constant 0
@0
D=A
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

//
//// Returns static[0] - static[1].
//function Class1.get 0
(Class1.get)

//push static 0
@Class1.vm.0
D=M
@SP
A=M
M=D
@SP
M=M+1

//push static 1
@Class1.vm.1
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

//// This file is part of www.nand2tetris.org
//// and the book "The Elements of Computing Systems"
//// by Nisan and Schocken, MIT Press.
//// File name: projects/08/FunctionCalls/StaticsTest/Class2.vm
//
//// Stores two supplied arguments in static[0] and static[1].
//function Class2.set 0
(Class2.set)

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

//pop static 0
@SP
M=M-1
A=M
D=M
@Class2.vm.0
M=D

//push argument 1
@ARG
D=M
@1
D=D+A
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1

//pop static 1
@SP
M=M-1
A=M
D=M
@Class2.vm.1
M=D

//push constant 0
@0
D=A
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

//
//// Returns static[0] - static[1].
//function Class2.get 0
(Class2.get)

//push static 0
@Class2.vm.0
D=M
@SP
A=M
M=D
@SP
M=M+1

//push static 1
@Class2.vm.1
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

//// This file is part of www.nand2tetris.org
//// and the book "The Elements of Computing Systems"
//// by Nisan and Schocken, MIT Press.
//// File name: projects/08/FunctionCalls/StaticsTest/Sys.vm
//
//// Tests that different functions, stored in two different 
//// class files, manipulate the static segment correctly. 
//function Sys.init 0
(Sys.init)

//push constant 6
@6
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 8
@8
D=A
@SP
A=M
M=D
@SP
M=M+1

//call Class1.set 2
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
@2
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Class1.set
0;JMP
(RETURN_1)

//pop temp 0 // Dumps the return value
@SP
M=M-1
A=M
D=M
@5
M=D

//push constant 23
@23
D=A
@SP
A=M
M=D
@SP
M=M+1

//push constant 15
@15
D=A
@SP
A=M
M=D
@SP
M=M+1

//call Class2.set 2
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
@2
D=D-A
@5
D=D-A
@ARG
M=D
@SP
D=M
@LCL
M=D
@Class2.set
0;JMP
(RETURN_2)

//pop temp 0 // Dumps the return value
@SP
M=M-1
A=M
D=M
@5
M=D

//call Class1.get 0
@RETURN_3
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
@Class1.get
0;JMP
(RETURN_3)

//call Class2.get 0
@RETURN_4
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
@Class2.get
0;JMP
(RETURN_4)

//label WHILE
(WHILE)

//goto WHILE
@WHILE
0;JMP

