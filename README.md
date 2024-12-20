# Joy of Postfix Calculator App for Android

![joyapppng](https://github.com/Joy-of-Postfix/calculator/blob/main/pictures/JoyOfPostfixGreen.png)

[APK-File](https://github.com/Joy-of-Postfix/calculator/blob/main/apk/debug/joyapp-debug.apk) can be downloaded

**Subset** of the programming language **Joy** for the ***postfix experience***.

[Tutorial of Joy](https://www.kevinalbrecht.com/code/joy-mirror/j01tut.html)
but there are some **modifications** to Joy-of-Postfix

## They are:

- ***' identifier*** is not a char, but a quotation of the identifier

- there are ***no sets*** and ***no integers*** and ***no chars***  (for ***filter*** of strings use ***unpack*** and ***pack***)

- it is only defined for one line with **==** at the second position and an identifier at the first position
- there is no keyword **DEFINE** and no terminator at the end of the line

- ***get*** and ***put*** are for dictionary use

- ***split2*** is used for filtering two aggregates as a result
- ***split*** and ***join*** are for splitted strings in a list

- type the **CALC**-Button to execute a line of functions or a definition
- type **.** for one output taken from the top of the stack
- type **.s** for the output of the reversed **stack**
- for side-effects there is a Monad to use at the end of a line, like: [*program*] [*program*] **!**

**IMPORTANT:** with ***dump*** and ***words*** and ***help*** you get an overview of the words or definitions that are used.

## Data Types

- **[ ]** is the empty list (null)
- **[a b c]** is the non-empty list (cons)
- **-123.456E-33** are double numbers (float)
- **abc** is an identifier (ident)
- **"abc"** is an unicode-string (string)
- **true** and **false** are booleans (bool)
- **(x y z)** is a comment
- **# x y z** is an end-of-line comment

