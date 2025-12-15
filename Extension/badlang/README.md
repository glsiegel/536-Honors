# Badlang Language Server
CS 536 Honors Project - Luke Siegel with professor Aws Albarghouthi - UW Madison - Dec 12, 2025

# Overview

For this honors project, I implemented the language server protocol (LSP) for Badlang, allowing for its use in a Visual Studio Code extension. Some features of this project:

1. When the LSP encounters errors, it attempts recovery mechanisms to continue running the pipeline in the hopes to catch additional errors. This can allow users to see a list of many LSP errors in the file simultaneously, instead of just the first error.

2. The extension provides syntax highlighting, giving different colors to global vs local variables.

3. This project extends Badlang to include import statements. Files can declare that they are part of a package and then import other files within the same package. The LSP tracks which files reference other files for global identifier resolution. This component of the project took the greatest amount of effort and forethought.

4. This project includes an interpreter for Badlang, extended to function with import statements.

5. The extension will re-check the file after updates, though will wait a second in order to avoid continuously running the pipeline after every character submitted by the user.