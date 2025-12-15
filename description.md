# Badlang Language Server
CS 536 Honors Project - Luke Siegel with professor Aws Albarghouthi - Dec 12, 2025

# Overview

For this Honors project, I implemented the Language Server Protocol (LSP) for our in-class mock language Badlang, allowing for its use in a Visual Studio Code extension. Some features of this project:

1. When the checker encounters errors, it attempts recovery mechanisms to continue running the pipeline in the hopes to catch additional errors. This can allow users to see a list of many checker errors in the file simultaneously, instead of just the first error.

2. The extension provides syntax highlighting, giving different colors to global vs local variables.

3. This project extends Badlang to include import statements. Files can declare that they are part of a package and then import other files within the same package. The LSP tracks which files reference other files for global identifier resolution. This component of the project took the greatest amount of effort and forethought.

4. This project includes an interpreter for Badlang, extended to function with import statements.

5. The extension will re-check the file after updates, though will wait a second in order to avoid continuously running the pipeline after every character submitted by the user.

# File Breakdown
In this repository there are the following files/directories:
## Eclipse
This folder contains the LSP code along with the interpreter code. This all compiles into a single jar file, which the extension uses for the LSP and interpreter. Almost all methods contain detailed descriptions.
## Extension
This folder contains the code for the extension itself. It tells VSCode what to expect from Badlang and also calls the jar file appropriately. If one wants to run the extension, they can open the directory "Extension/badlang" in VSCode and press F5. This runs the extension in debug mode.
## TestPrograms
This folder contains test Badlang programs, designed to show off some features of the LSP, by either demonstrating syntax highlighting or error handling.
## badlang-0.0.1.vsix
This file allows users to install the extension on their own VSCode. It should ONLY show itself when dealing with .bl files. Additionally, since VSCode keeps things "locked down", it should leave no trace upon being uninstalled. Run this command to install it:

```code --install-extension badlang-0.0.1.vsix```

This can be uninstalled within the VSCode extensions panel, or with:

```code --uninstall-extension luke-siegel.badlang```

# What I Learned
The opportunity to complete this Honors project provided me with a great deal of insight into the compiler pipeline and LSP structure.

The syntax checker we built in class only had to panic at the first error, which was relatively simple. However, most every real-world compiler has a syntax checker that identifies a multitude of errors simultaneously. Meaning, a syntax checker needs to find recovery mechanisms to continue processing the files even after encountering bizarre input. 

In making my syntax checker for the LSP, I had to look into different recovery mechanisms used in real-world compilers. The parser required the bulk of the modification in order for my LSP to be able to capture multiple errors. Depending on the erroneous token and the anticipated statement/expression, I had to determine whether to ignore the token, pretend the token was the correct token, skip to the next line, skip to the next parenthesis, or skip to the next bracket. Each of these has its own advantages and disadvantages, and this project made it clear to me that there's no "supreme" heuristic. 

For example, suppose that a user writes ```int x = 1 call(true);```. There are many recovery strategies we could take. Did the user mean to write a binary operator between ```1``` and ```call(true)```? If so, which one? Or, did they mean to write these as two separate statements and are missing a semicolon? Or, was it something else entirely? Whichever meaning we pick, it then begs the question of how the LSP should recover. If the LSP skips to the next line once encountering the first problem, it will not be able to check that the function ```call``` accepts one argument of type ```bool```. Yet, if it does not skip to the next line, then it needs to somehow turn these tokens into a well-formed set of statements. As such, designing a compiler with error recovery mechanisms is extremely non-trivial and requires many design decisions.

This project also helped me understand different caching strategies for the LSP. The LSP is responsible for remembering the state of various files, which opens up some design decisions. Whenever a user modifies the contents a file, instead of re-sending the entire file contents to the LSP, I only send the changed characters and store the current file state internally. Additionally, whenever the user changes the file, I do not run the syntax checker immediately, and instead wait until the user does not type for a full second. This reduces wasted CPU cycles where the extension might repeatedly try to run the syntax checker pipeline on something that the user was actively working on. There were also unique challenges when working with multiple files. If one file referenced another, then I would need to run the pipeline on both files in order to calculate semantic tokens and syntax errors. However, if one file was left unchanged since its last run through, I could avoid redundant work and cache its results from the previous pass.

Speaking of files referencing each other, extending the language with imports posed a unique challenge. I had to choose some system to allow users to link files together, and I'm unsure which one makes the most sense. It seems like each language takes a different strategy towards imports, and through this project I was better able to understand why: there's not really a clear supreme method. C allows users to reference specific files which means there's no strict notion of a "project" hierarchy, but in return it includes the horrors that are header files. Rust requires users to identify all the "modules" in a main file, then import them in all relevant files by referencing crate. This does then require the notion of a "main" file. I ended up going with a strategy more similar to Java, where files declare their project path at the top, then import other files using these declared path names. I also allowed users to specify names for the imports, much like Python's ```import pandas as pd``` structure. This has the advantage of allowing users to change the locations and names of files and only needing to modify the few statements at the tops of all affected files. However, this has the downside of creating a notion of a "project root" and also means that each file can be a member of at most one project.

The implementation of the syntax checker for multi-file projects proved very difficult. Since the LSP needs to cache information about the files for efficiency, then it also needed a system to remember how the files related to each other. This resulted in the FileManager class and Rootpath object which work together to define and remember these relationships. Going through this development process and experiencing the high level of complexity gave me a newfound appreciation for other existing LSPs. I feel I have taken them for granted whenever I've been writing code, and for that, I'm truly sorry. If it was difficult for a simple language like Badlang, then I can't imagine what would be going on in a more complex language with many more moving parts. Just thinking about generics and protected methods and obj.val.call().get() makes my head hurt.

Most importantly, this project inspired me to start learning Rust. It is impressive how detailed and powerful its compiler is. In Rust, it feels like it's impossible to write incorrect code, because if it was incorrect, the compiler would catch it. So many times throughout this project (written in Java) I felt the pains of OOP more than any other project I've worked on. Since finishing this Honors project, I've started to work on reimplementing an extended Badlang compiler in Rust, which has been a lot of fun so far!

Overall, I felt like this experience was very valuable to my learning and future as a programmer. I am very grateful to have had guidance from Aws through this semester. I sincerely hope I can do something as captivating as this project when I'm working in the real world.