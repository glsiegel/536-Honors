package interpreter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data.Outcome;
import data.Stmt;
import data.Token;
import data.VarType;
import env.GlobalEnvironment;
import error.IError;
import lexer.Lexer;
import parser.Parser;
import pipeline.FileInfo;
import pipeline.FileManager;
import pipeline.Pipeline;
import pipeline.Rootpath;
import pipeline.StatementData;

public class InterpreterPipeline {
	
	public static void runOnFileName(String filename) {
		File f = new File(filename);
		
		if (!f.exists()) {
			System.err.println(String.format("Interpreter immediate failure. Could not resolve file with name '%s'.", filename));
			return;
		}
		
		// this pretty much does everything with checking.
		// statically checks all the relevant files.
		Outcome outcome = Pipeline.runStaticCheck(f);
		
		List<Stmt> statements = null;
		
		
		if (!outcome.hasManager) {
			// check for errors
			StatementData data = outcome.getStatementData(); // we know this is safe
			if (!data.errors.isEmpty()) {
				System.err.println(String.format("Interpreter had errors checking the file '%s'. Details:", filename));
				for (IError error : data.errors) {
					System.err.println(error);
				}
				
				return; // don't interpret
			}
			
			// otherwise, fall through to interpret!
			statements = data.statements;
			
		} else {
			FileManager<VarType> manager = outcome.getManager();
			
			boolean going_to_run = true;
			
			for (Map.Entry<Rootpath, FileInfo<VarType>> entry : manager) {
				Rootpath rp = entry.getKey();
				FileInfo<VarType> info = entry.getValue();
				if (info.file.equals(f)) {
					statements = info.getStatements();
				}
				if (info.getErrors() != null && !info.getErrors().isEmpty()) {
					if (going_to_run) {
						going_to_run = false;
						System.err.println("Interpreter found just some of these errors.");
					}
					System.err.println(String.format("In file %s:", rp));
					for (IError error : info.getErrors()) {
						System.err.println(error);
					}
				}
				
			}
			
			if (!going_to_run) return;
		}
	
		
		// GREAT! Now, interpret!
		
		Interpreter interpreter = new Interpreter(statements, f);
		interpreter.run();
	}
	
	public static GlobalEnvironment<Object> findGlobals(File f) throws RuntimeException {
		Lexer lexer = new Lexer();
		
		lexer.runOnFile(f.getAbsolutePath()); // bubble errors up

		List<Token> token_list = lexer.tokenList;
		Parser parser = new Parser(token_list);


		parser.run(); // bubble errors up
		List<Stmt> statements = parser.statements;
		
		// Checker is ASSUMED to work, since the checker needs to run before interpreter can run.
		
		GlobalValueBuilder gbuilder = new GlobalValueBuilder();
		gbuilder.run(statements);
		if (!gbuilder.visitorErrors.isEmpty()) throw new RuntimeException("Had global construction errors.");
		return gbuilder.globals;
	}
}
