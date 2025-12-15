// The module 'vscode' contains the VS Code extensibility API
// Import the module and reference it with the alias vscode in your code below
const vscode = require('vscode');
const path = require('path');
const cp = require('child_process');
const { LanguageClient, TransportKind } = require('vscode-languageclient/node');

// This method is called when your extension is activated
// Your extension is activated the very first time the command is executed

/**
 * @param {vscode.ExtensionContext} context
 */
function activate(context) {

	// Use the console to output diagnostic information (console.log) and errors (console.error)
	// This line of code will only be executed once when your extension is activated
	// console.log('Congratulations, your extension "badlang" is now active!');

	const output = vscode.window.createOutputChannel("Badlang Interpreter");

	// The command has been defined in the package.json file
	// Now provide the implementation of the command with  registerCommand
	// The commandId parameter must match the command field in package.json
	const disposable = vscode.commands.registerCommand('badlang.run', function () {
		// The code you place here will be executed every time your command is executed
		const editor = vscode.window.activeTextEditor;
		if (!editor) {
			vscode.window.showErrorMessage("No active file to run!");
			return;
		}

		const filePath = editor.document.fileName;
		
		output.show(true);

		const serverModule = context.asAbsolutePath('langServ.jar');

		const cmd = `java -jar ${serverModule} -interp ${filePath}`;

		output.clear();

		cp.exec(cmd, (error, stdout, stderr) => {
			if (stdout) output.append(stdout);
			if (stderr) output.append(stderr);

			if (error) {
				vscode.window.showErrorMessage("Interpreter error!");
			}
		});

		// Display a message box to the user
		// vscode.window.showInformationMessage("Server path: " + context.asAbsolutePath('langServ.jar'));
		vscode.window.showInformationMessage("Interpreter running.");
	});

	context.subscriptions.push(disposable);

	// vscode.window.showInformationMessage("Server path: " + context.asAbsolutePath('langServ.jar'));
	vscode.window.showInformationMessage("Badlang extension running.");
	
	const serverModule = context.asAbsolutePath('langServ.jar');
    const serverOptions = {
		command: 'java',
		args: ['-jar', serverModule, '-lsp'],
		options: {}
    };
    const clientOptions = {
		documentSelector: [{ scheme: 'file', language: 'badlang' }],
		outputChannelName: 'Badlang LSP',
    };
    const client = new LanguageClient(
        'badlang',
        'BadLang Language Server',
        serverOptions,
        clientOptions
	);
	client.start()
	context.subscriptions.push(client);
}

// This method is called when your extension is deactivated
function deactivate() {}

module.exports = {
	activate,
	deactivate
}
