# lexer-Gayatri27
lexer-Gayatri27 created by GitHub Classroom




Computer Science Department
San Francisco State University
CSC 413  Spring 2018
Assignment 2 - Modify the Lexer


GitHub repository link:
https://github.com/sfsu-csc-413-spring-2018/lexer-Gayatri27



Compiling and Run instructions

javac lexer/setup/TokenSetup.java
java lexer.setup.TokenSetup
javac lexer/Lexer.java
java lexer.Lexer filename.x



BY

Gayatri Pise	917922296	gpise@mail.sfsu.edu









1.Project Introduction

This assignment is to understand the functioning of lexical analysis in the java compilation process. The purpose of lexical analyzer is to read the input characters of the source program and categorize them into sequence of lexical tokens to feed into the parser. If the lexer finds an invalid token, it will report an error. A stream of tokens is generated after lexical analysis and passed on for Tokenizing. Tokenizing is handled by the Parser. Parsers consume the output of the lexical analyzer and operates by analyzing the sequence of tokens. Lexical analyzer interacts with the symbol table as well. In some cases, information regarding the kind of identifier may be read from the symbol table by the lexical analyzer to assist it in determining the proper token it must pass to the parser. 
In this assignment, we extend the Lexer class by adding additional tokens to perform lexical analysis of the x language compiler. The list of tokens available to the Lexer are listed in tokens file.


2. Execution and development environment described 
The lexer is coded in java language. Java is a programming language that is concurrent, class-based, object-oriented, and specifically designed to have as few implementation dependencies as possible. JVM is a computing machine which enables the computer to run a Java program. For compiling and execution, Java source code is compiled into bytecode when we use the javac compiler. The bytecode gets saved on the disk with the file extension .class. When the program is to be run, the bytecode is converted using the compiler. The result is machine code which is then fed to the memory and is executed. 

We use the following java development environment for our application.

 
3. Scope of work
1. The Lexer class reads input from a hard-coded file. This file must be given from the command line. The program returns an error if no input file is given from the command line.
2. We added five additional tokens in the tokens file and by executing TokenSetup class these tokens were added to the Lexer.
"	Greater: >  
"	GreaterEqual: >=  
"	SortaClose: <>  
"	NumberLit: <number> any floating-point literal, which is one or more digits, followed by a decimal point,  followed by one or more digits, as expressed by d+.d+  
"	ScientificLit: <scientific> a number expressed in normalized (one digit before the decimal point) scientific  notation as expressed by d.dd?[Ee][+-]d+  
3. The Token class is updated to include line numbers at which the token was found. This line number is provided by the Lexer while creating the Token object. Lexer gets line number from SourceReader. Token class also takes care of reporting errors.
4. The Lexer output is updated for readability and the Token object is responsible for the formatting and printing the output. Formatting is done by System.out.format().
5. The Lexer output is updated to include a printout, with line number, of each of the lines fed in from the source file. If an error is encountered by the Lexer, then the printout includes lines up to the error line.


4. Command line instructions to compile and execute

javac lexer/setup/TokenSetup.java
java lexer.setup.TokenSetup
javac lexer/Lexer.java
java lexer.Lexer filename.x
5. Assumptions 

"	The project folder structure remains same as compiling and executing instructions.
"	The number of input files given to the Lexer is one. 
"	The input file provided from command line is available in sample_files folder.
"	TokenSetup is always executed before Lexer.





































6. Class diagram with hierarchy


The class diagrams for this assignment is as below:
 




 


7. Implementation decisions

"	The TokenSetup class is responsible for reading tokens from the tokens file and generating TokenType and Tokens classes. TokenType class is a hashmap to map tokens and symbol. Tokens class is the enum of available Tokens.
"	SourceReader class is responsible to read the input file. The file is scanned by a BufferedReader. The position of the Token/Identifier and the line number is saved in the SourceReader. The file printout is also performed by SourceReader.
"	Symbol class is responsible for generating a symbol from the TokenString and Tokens kind. This class also saves the symbol name and Tokens kind information.
"	The Token class contains all information around the token like leftPosition, rightPosition, lineNumber and Symbol. This class also formats the command line output for a Token.
"	The Lexer class contains main() method of our application. We first read the input file name and pass on to the SourceReader for reading. The input file is read character by character and is Token is generated when a valid Symbol or Identifier is found. 

























8. Code Organization

The entire project is organized in the below file structure.
 
9. Conclusion

"	The Lexer successfully reads file name from the command line and outputs usage if no file name is found.
"	Additional Tokens were added and processed successfully.
"	Token class is updated to include line number at which the token was found.
"	Lexer output is formatted as expected in this assignment.
"	The Lexer output also contains the printout of the file with line numbers.
"	This assignment is completed following the object-oriented concepts.
"	This work can be extended and used by the Parser and Compiler modules in the project.
