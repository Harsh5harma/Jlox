# Jlox 
This is my implementation of the Jlox language featured in crafting interpreters by Robert Nystrom  

## Table of Contents:

1. [ Installation ](installation)

2. [ Features and Usage ](features-and-usage)

3. [ Missing Features ](#missing-features)

4. [ Conclusion ](##conclusion)


## Installation:  

<strong>$ gh repo clone Harsh5harma/craftinginterpreters  </strong>

<strong>$ make all</strong>

## Features and Usage
* Jlox is a dynamically typed scripting language.
* Jlox is Turing complete.
* Jlox is built using Java
* It uses a simple Tree Walk interpreter that employs recursive descent to walk through the Syntax Trees.
* Jlox implements OOP with the classic single parent inheritance structure.
* Everything in Lox is an expression, which means each expression must be evaluated.
### Usage  

* Running a REPL  

  <strong>$  make run </strong>

  ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/52e3a0a5-2f72-4d0e-8021-9fc541dba097)


* Interpreting source file  

  <strong>$ make run ARGS="[filename].lox"</strong>

  ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/53e5aa30-98e8-4ef6-a9dd-3d20d1438ec0)

  ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/3971027c-93d4-48fd-8761-4cd6fbc747c7)



### Syntax 
1. <strong>Variable declaration  </strong>  

   Variables are dynamically typed and they look similar to those in JS. End of Story.
   
   ![image](https://github.com/Harsh5harma/craftinginterpreters/assets/77851315/764a6392-0c89-4a62-8c61-df8a57d49047)

   
1. #### Flow Control

   Our flow control looks similar to that of C/C++/Java/Javascript.

   #### Example:    

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/38f1d6b6-76b8-4330-8d5f-47d4de2ae7bb)

   #### Output:

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/376c6ecd-9b2d-442b-870f-07def724e400)

   
1. #### Loops

   Jlox supports both while and for loops. It does not support do-while loops.

   #### Example of While loop:

   Simple program to countdown from 10 to 1.

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/aa8b1488-68fe-4dd7-a188-636616519e57)

   #### Output:

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/678de2dd-48ec-40cb-81e4-546385b956dc)


   #### Example of For loop:

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/86a0de78-e8c5-45ec-a1c2-52eb86ee74c0)

   
   #### Output:

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/dd38633b-2ca1-4011-b65d-63241e8e4a1f)

1. <strong>Function declaration  </strong>
   
   Function declarations look like those from Kotlin but fairly easy to get used to. The argument limit is 255.
   
   ![image](https://github.com/Harsh5harma/craftinginterpreters/assets/77851315/dc8da30c-b512-4411-82a1-e83323117e27)

1. <strong>Currying of functions  </strong>
   
   Currying is nothing but a play on higher order functions which allows the programmer to chain up any arbritary long expression  
   as long as it evaluates to a Callable object. Yeah kinda cool that we've got higher order functions here.
  
   ![image](https://github.com/Harsh5harma/craftinginterpreters/assets/77851315/dcd7282c-382c-4f8d-a878-e136565cc58b)

1. <strong>Closures  </strong>

   Closures are simply put, snapshots of your scope-chain or "environment parent-pointer tree",
   taken at the time of function declaration. They're a famous feature of Javascript and we've implemented them here as well.
   The image below demonstrates an example of closure usage in Jlox.

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/3534bb27-dabc-4590-a5b7-cbe66aa51a74)

   This prints "global" both times as the variable a was assigned "global" when the inner function was declared and subsequent
   changes to the variable aren't reflected in the closure (closures would be easier to get if they were called "static scope-snapshots").  

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/42d06507-3943-4e7b-b16b-e82283e4e2a6)

3. #### This  
   Jlox gets an implementation of <strong>this</strong> too. <strong>this</strong> gets binded to the object it was called on.

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/58ebc6f5-6f22-433d-a64f-a1435e8d43af)

   #### Output:

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/4b968ee3-fefa-4b68-a66e-be4304b5bb94)


5. #### Classes
   You thought there'd be no classes? Well you thought wrong, Christmas came a bit early (not really). Jlox implements constructors via the classic
   <i>init()</i> way. Also you'll see soon that we decided not to go the <i>new</i> keyword route when creating a class instance, that would've been
   an extra keyword to deal with. Instead, we create new classes in a way that looks very similar to how factory functions are created in JS.

   #### Example Class:

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/806f2f6d-7ce0-4e47-8032-f9996b941a6c)

   #### Output:
   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/ff540500-5803-42bc-bb5c-5a48e54227e9)


   
7. #### Inheritance
   Jlox supports single-parent inheritance, the classic OOP way. We don't hang with those ugly prototypes here. Again, we try to minimize the number of
   keywords we use, so no <i>extends</i> or <i>:</i> were touched here. We use the humble <i> < </i> operator here. Kind of simple right, it mimics mathematic comparison.

   #### Example:
   
   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/581f3be5-ecb9-4cd1-946a-c5a0292f12f3)

   #### Output:

   ![image](https://github.com/Harsh5harma/Jlox/assets/77851315/fcee78bb-8d39-44b3-9534-d0f1e8c60dbb)


## Missing Features: 

  If you looked at the examples above, you would've felt that something was missing. I'd like to answer that by saying that <strong>a lot</strong>  
  is missing. There are many arithmetic operations that Jlox simply can't handle if you compare it to a language like Python, there's no support for many 
  operators we take for granted. I can't list out <i>all</i> the features its missing but I'll try to mention all those that spring to my mind over time.  
  I don't consider them failures of the language but more like a challenge list that I need to tackle eventually. 

  * break statements.

  * switch-case expressions.

  * Native functions for file management or OS usage.

  * Complex String manipulation.

  * Explicit Type conversion.

  * Conditional/Ternary operator.

  * User Input functions.

  * ...

## Conclusion:  

  Jlox seems like a language out of the 80s or like a very early draft of python and that's a valid assessment to make but even working through a language
  that basic taught me a lot. You wanna learn closures? There's no better way than to code the damn thing for your own language. And this is what I observed
  throughout the project. I had a lot of gaps in my understanding of programming concepts that I was aware of, but there were also many that I was clueless about.
  Working through Lox filled up a fair bit of them.  

  Now my note to you. Go ahead, build your own Jlox. It's gonna be frustrating at times but you'll come out having learned a lot of stuff you probably consider
  programming black magic right now. Good luck.



