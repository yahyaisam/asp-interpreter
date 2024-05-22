// © 2021 Dag Langmyhr, Institutt for informatikk, Universitetet i Oslo

package no.uio.ifi.asp.scanner;

import java.io.*;
import java.util.*;

import no.uio.ifi.asp.main.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class Scanner {
    private LineNumberReader sourceFile = null;
    private String curFileName;
    private ArrayList<Token> curLineTokens = new ArrayList<>();
    private Stack<Integer> indents = new Stack<>();
    private final int TABDIST = 4;
	private boolean curStringLitBeingProcessed = false;

    public Scanner(String fileName) {
		// Test
		curFileName = fileName;
		indents.push(0);

	try {
	    sourceFile = new LineNumberReader(
			    new InputStreamReader(
				new FileInputStream(fileName),
				"UTF-8"));
	} catch (IOException e) {
	    scannerError("Cannot read " + fileName + "!");
	}
    }


    private void scannerError(String message) {
		String m = "Asp scanner error";
		if (curLineNum() > 0)
	    m += " on line " + curLineNum();
		m += ": " + message;

		Main.error(m);
    }


    public Token curToken() {
		while (curLineTokens.isEmpty()) {
	    	readNextLine();
	}
		return curLineTokens.get(0);
    }


    public void readNextToken() {
	if (! curLineTokens.isEmpty())
	    curLineTokens.remove(0);
    }

    private void readNextLine() {
	curLineTokens.clear();

	// Read the next line:
	String line = null;
	try {
	    line = sourceFile.readLine();
	    if (line == null) {
		sourceFile.close();
		sourceFile = null;
	    } else {
		Main.log.noteSourceLine(curLineNum(), line);
	    }
	} catch (IOException e) {
	    sourceFile = null;
	    scannerError("Unspecified I/O error!");
	}

	//-- Must be changed in part 1: -------------------------------------------------

	// 1: Sjekk at vi ikke har siste linje
	if (line == null) {

		// 1.1 find remaining indents
		addIndentsOrDedents(0);

		// 1.2
		curLineTokens.add(new Token(eofToken, curLineNum()));
	}

	// 2: Sjekk at vi ikke har en linje med kommenatarer
	else if (!isEmptyOrCommented(line)) {

		// 2.1: Erstatt alle TABs med whitespaces
		line = expandLeadingTabs(line);

		// 2.2: Beregn indenteringen
		int whitespaces = findIndent(line);

		// 2.3: Legg til eventuelle INDENT/DEDENT-tokens
		addIndentsOrDedents(whitespaces);

		// 2.4: Tokenize linje
		tokenize(line);

		// 2.5: Parsere curLineTokens
		// vi maa kunne lese hele linje, uten aa flytte naavaerende tokenPeker og kontrollere
		// om vi har = token eller ikke: assignment ved = og expr stmt ved ikke
		
		// 2.5: Legg til NEWLINE-token
		curLineTokens.add(new Token(newLineToken, curLineNum()));
	}

	// 3: legg til alle Tokens i log for vi begynner paa en ny linje
	for (Token t: curLineTokens) 
	    Main.log.noteToken(t);
    }

	public TokenKind findOperatorTokenKind(String s) {
		
		for (TokenKind tk: EnumSet.range(astToken,semicolonToken)) {
			if (s.equals(tk.image)) {
			return tk;
			}
		}
		return null;
	}

	public boolean isOperator(String s) {

		for (TokenKind tk: EnumSet.range(astToken,semicolonToken)) {
			if (s.equals(tk.image)) {
				return true;
			}
		}
		return false;
	}

	// fredet kode
	public void tokenize(String line) {
		String curTokenString = "";
		
		// vi itererer gjennom hele linjen med Tokens
		for (int i = 0; i < line.length(); i++) {
			
			// Henter ut forste symbol
			char c = line.charAt(i);

			// 1: Kontrollér at vi ikke jobber med en char c som kun er whitespace
			while (c == ' ') {
				if (i == line.length() -1) {	
					break;
					}
				else {
					i ++;
					c = line.charAt(i);
				}	
			}
			if (c == '#') {
				if (curTokenString.isEmpty()) {
					break;
				}
			}

			else if (c == '"' || c == '\'') {
				char l = c;
				curTokenString = curTokenString + l;
				i++;
				c = line.charAt(i);
			
				while (i < line.length() && (c != l)) {
					curTokenString = curTokenString + c;
					i ++;
					c = line.charAt(i);
				}
				curTokenString = curTokenString + l;
				Token tk = new Token(stringToken, curLineNum());
				tk.stringLit = curTokenString;
				curLineTokens.add(tk);
				curTokenString = "";
				continue;
			}

			else if (isLetterAZ(c)) {
				
				while (i < line.length() && (isDigit(c) || isLetterAZ(c))) {
					curTokenString = curTokenString + c;
					i ++;
					if (i != line.length()) c = line.charAt(i);
				}
				Token tk = new Token(nameToken, curLineNum());
				tk.name = curTokenString;
				tk.checkResWords();
				curLineTokens.add(tk);
				curTokenString = "";
				i --;
				continue;
			}

			else if (isDigit(c)) {
				int dot = 0;
				while (i < line.length() && ((isDigit(c) || c == '.'))) {
					curTokenString = curTokenString + c;
					i ++;
					if (c == '.') dot++;
					if (i != line.length()) c = line.charAt(i);
				}
				
				if (curTokenString.contains(".")) {
					
					if (dot > 1) {
						scannerError("Flere enn 1 punktum ('.') i tallet!");
					}

					Token tk = new Token(floatToken, curLineNum());
					tk.floatLit = Float.parseFloat(curTokenString);
					curLineTokens.add(tk);
					
				}
				else {
					Token tk = new Token(integerToken, curLineNum());
					tk.integerLit = Integer.parseInt(curTokenString);
					curLineTokens.add(tk);	
				}
				curTokenString = "";
				i --;
				continue;
			}

			else if (isOperator(Character.toString(c)) || c == '!') {
				
				while (i < line.length() && (isOperator(Character.toString(c)) || c == '!')) {
					curTokenString = curTokenString + c;
					i ++;
					if (i != line.length()) c = line.charAt(i);
				}

				for (int e = 0 ; e < curTokenString.length() ; e++) {
					char op1 = curTokenString.charAt(e);
					
		
					if (e + 1 != curTokenString.length()) {
						char op2 = curTokenString.charAt(e+1);
						String combinedOp = "";
						combinedOp = combinedOp + op1;
						combinedOp = combinedOp + op2;

						if (isOperator(combinedOp)) {
							TokenKind tk = findOperatorTokenKind(combinedOp);
							Token t = new Token(tk, curLineNum());
							curLineTokens.add(t);
							e ++;
						}
						else {
							TokenKind tk = findOperatorTokenKind(Character.toString(op1));
							Token t = new Token(tk, curLineNum());
							curLineTokens.add(t);
						}	
					}
					else {
						TokenKind tk = findOperatorTokenKind(Character.toString(op1));
						Token t = new Token(tk, curLineNum());
						curLineTokens.add(t);
					}
				}
				curTokenString = "";
				i --;
				continue;
			}
		}
	}

	public void addIndentsOrDedents(int n) {
		if (n > indents.peek()) {
			indents.push(n);
			curLineTokens.add(new Token(indentToken, curLineNum()));
		}

		else if (n < indents.peek()) {
			while (n < indents.peek()) {
				indents.pop();
				curLineTokens.add(new Token(dedentToken, curLineNum()));
			}
			
			if (n != indents.peek()) {
				System.out.println("Indenteringsfeil!");
			}
		}
	}

	public Boolean isEmptyOrCommented(String line) {
		
		line = line.strip().trim();

		if (line.equals("")) {
			return true;
	
		}
		if (line.charAt(0) == '#') {
			return true;
		}
		return false;
	}

    public int curLineNum() {
	return sourceFile!=null ? sourceFile.getLineNumber() : 0;
    }

    private int findIndent(String s) {
	int indent = 0;

	while (indent<s.length() && s.charAt(indent)==' ') indent++;
	return indent;
    }

	// //-- Must be changed in part 1:
	// Følgende metode er hentet fra løsningsforslaget for ukesoppgave (uke 36) fra høsten 2021.
    public String expandLeadingTabs(String s) {
        String newS = "";
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                newS += " ";
                n++;
            }

            else if (c == '\t') {
                int nReplace = 4 - (n % 4);
                for (int j = 0; j < nReplace; j++) {
                    newS += " ";
                }
                n += nReplace;
            }

            else {
                newS += s.substring(i);
                    break;
            }
        }
        return newS;
    }  


    private boolean isLetterAZ(char c) {
		return ('A'<=c && c<='Z') || ('a'<=c && c<='z') || (c=='_');
    }


    private boolean isDigit(char c) {
		return '0'<=c && c<='9';
    }


    public boolean isCompOpr() {
		TokenKind k = curToken().kind;
		//-- Must be changed in part 2:
		if (k == TokenKind.lessToken || (k == TokenKind.greaterToken) || (k == TokenKind.doubleEqualToken) 
		|| (k == TokenKind.greaterEqualToken) || (k == TokenKind.lessEqualToken) || (k == TokenKind.notEqualToken)) return true;

		return false;
    }


    public boolean isFactorPrefix() {
		TokenKind k = curToken().kind;
		//-- Must be changed in part 2:
		if ((k == TokenKind.minusToken) || (k == TokenKind.plusToken)) return true;

		return false;
    }


    public boolean isFactorOpr() {
	TokenKind k = curToken().kind;
	//-- Must be changed in part 2:
	if ((k == TokenKind.astToken) || (k == TokenKind.slashToken) || (k == TokenKind.percentToken) || (k == TokenKind.doubleSlashToken)) return true; 
	return false;
    }
	

    public boolean isTermOpr() {
	TokenKind k = curToken().kind;
	//-- Must be changed in part 2:
	if ((k == TokenKind.minusToken) || (k == TokenKind.plusToken)) return true;

	return false;
    }

	public boolean isCompStmt() {
		TokenKind k = curToken().kind;
		//-- Must be changed in part 2:
		if ((k == TokenKind.forToken) || (k == TokenKind.ifToken) || (k == TokenKind.whileToken) || (k == TokenKind.defToken)) return true; 

		return false;
    }



    public boolean anyEqualToken() {
	for (Token t: curLineTokens) {
	    if (t.kind == equalToken) return true;
	    if (t.kind == semicolonToken) return false;
	}
	return false;
    }
}
