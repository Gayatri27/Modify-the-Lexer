package lexer;

/**
 *  The Lexer class is responsible for scanning the source file
 *  which is a stream of characters and returning a stream of
 *  tokens; each token object will contain the string (or access
 *  to the string) that describes the token along with an
 *  indication of its location in the source program to be used
 *  for error reporting; we are tracking line numbers; white spaces
 *  are space, tab, newlines
 */
public class Lexer {
  private boolean atEOF = false;
  // next character to process
  private char ch;
  private SourceReader source;

  // positions in line of current token
  private int startPosition, endPosition, lineNumber;

  /**
   *  Lexer constructor
   * @param sourceFile is the name of the File to read the program source from
   */
  public Lexer(String sourceFile, boolean read) throws Exception {
    // init token table
    new TokenType();
    source = new SourceReader(sourceFile);
    if(read) {
      ch = source.read();
    }
  }

  /**
   *  newIdTokens are either ids or reserved words; new id's will be inserted
   *  in the symbol table with an indication that they are id's
   *  @param id is the String just scanned - it's either an id or reserved word
   *  @param startPosition is the column in the source file where the token begins
   *  @param endPosition is the column in the source file where the token ends
   *  @param lineNumber is the line in the source file where the token exists
   *  @param kind is the identified Token kind
   *  @return the Token; either an id or one for the reserved words
   */
  public Token newIdToken(String id, int startPosition, int endPosition, int lineNumber, Tokens kind) {
    return new Token(
      startPosition,
      endPosition,
      lineNumber,
      Symbol.symbol(id, kind)
    );
  }

  /**
   *  number tokens are inserted in the symbol table; we don't convert the
   *  numeric strings to numbers until we load the bytecodes for interpreting;
   *  this ensures that any machine numeric dependencies are deferred
   *  until we actually run the program; i.e. the numeric constraints of the
   *  hardware used to compile the source program are not used
   *  @param number is the int String just scanned
   *  @param startPosition is the column in the source file where the int begins
   *  @param endPosition is the column in the source file where the int ends
   *  @param lineNumber is the line in the source file where the token exists
   *  @param kind is the identified Token kind
   *  @return the int Token
   */
  public Token newToken(String number, int startPosition, int endPosition, int lineNumber, Tokens kind) {
    return new Token(
      startPosition,
      endPosition,
      lineNumber,
      Symbol.symbol(number, kind)
    );
  }

  /**
   *  build the token for operators (+ -) or separators (parens, braces)
   *  filter out comments which begin with two slashes
   *  @param s is the String representing the token
   *  @param startPosition is the column in the source file where the token begins
   *  @param endPosition is the column in the source file where the token ends
   *  @param lineNumber is the line in the source file where the token exists
   *  @return the Token just found
   */
  public Token makeToken(String s, int startPosition, int endPosition, int lineNumber) {
    // filter comments
    if(s.equals("//")) {
      try {
        int oldLine = source.getLineno();

        do {
          ch = source.read();
        } while(oldLine == source.getLineno());
      } catch (Exception e) {
        atEOF = true;
      }

      return nextToken();
    }

    // ensure it's a valid token
    Symbol sym = Symbol.symbol(s, Tokens.BogusToken);

    if( sym == null ) {
      System.out.println("******** illegal character: " + s);
      atEOF = true;
      return nextToken();
    }

    return new Token(startPosition, endPosition, lineNumber, sym);
  }

  /**
   *  @return the next Token found in the source file
   */
  public Token nextToken() {
    // ch is always the next char to process
    if(atEOF) {
      if(source != null) {
        source.close();
        source = null;
      }

      return null;
    }

    try {
      // scan past whitespace
      while(Character.isWhitespace(ch)) {
        ch = source.read();
      }
    } catch(Exception e) {
      atEOF = true;
      return nextToken();
    }

    startPosition = source.getPosition();
    endPosition = startPosition - 1;
    lineNumber = source.getLineno();

    if(Character.isJavaIdentifierStart(ch)) {
      // return tokens for ids and reserved words
      String id = "";

      try {
        do {
          endPosition++;
          id += ch;
          ch = source.read();
        } while(Character.isJavaIdentifierPart(ch));
      } catch(Exception e) {
        atEOF = true;
      }

      Tokens kind = Tokens.Identifier;
      if(id.equals("number")) {
        kind = Tokens.Number;
      } else if(id.equals("scientific")) {
        kind = Tokens.Scientific;
      }

      return newIdToken(id, startPosition, endPosition, lineNumber, Tokens.Identifier);
    }

    if(Character.isDigit(ch)) {
      // return number tokens

      String number = "";
      Tokens token = Tokens.INTeger;

      try {
        do {
          endPosition++;
          number += ch;
          ch = source.read();
        } while(Character.isDigit(ch));
      } catch(Exception e) {
        atEOF = true;
      }

      try {
        if('.' == ch) {
          endPosition++;
          number += ch;
          ch = source.read();
          do {
            endPosition++;
            number += ch;
            ch = source.read();
          } while(Character.isDigit(ch));

          if(isNumberLit(number)) {
            token = Tokens.NumberLit;
          }
        }
      } catch(Exception e) {
        atEOF = true;
      }

      try {
        if('e' == ch) {
          endPosition++;
          number += ch;
          ch = source.read();

          if('+' == ch || '-' == ch) {
            endPosition++;
            number += ch;
            ch = source.read();
          }

          do {
            endPosition++;
            number += ch;
            ch = source.read();
          } while(Character.isDigit(ch));

          if(isScientificLit(number)) {
            token = Tokens.ScientificLit;
          }
        }
      } catch(Exception e) {
        atEOF = true;
      }

      return newToken(number, startPosition, endPosition, lineNumber, token);
    }

    // At this point the only tokens to check for are one or two
    // characters; we must also check for comments that begin with
    // 2 slashes
    String charOld = "" + ch;
    String op = charOld;
    Symbol sym;
    try {
      endPosition++;
      ch = source.read();
      op += ch;

      // check if valid 2 char operator; if it's not in the symbol
      // table then don't insert it since we really have a one char
      // token
      sym = Symbol.symbol(op, Tokens.BogusToken);
      if (sym == null) {
        // it must be a one char token
        return makeToken(charOld, startPosition, endPosition, lineNumber);
      }

      endPosition++;
      ch = source.read();

      return makeToken(op, startPosition, endPosition, lineNumber);
    } catch( Exception e ) { /* no-op */ }

    atEOF = true;
    if(startPosition == endPosition) {
      op = charOld;
    }

    return makeToken(op, startPosition, endPosition, lineNumber);
  }

  private boolean isNumberLit(String number) {
    return number.matches("^\\d*\\.\\d+|\\d+\\.\\d*$");
  }

  private boolean isScientificLit(String number) {
    return number.matches("^[+-]?(\\d+\\.\\d+|\\d+\\.|\\.\\d+|\\d+)([eE][+-]?\\d+)?$");
  }

  public static void main(String args[]) {
    Token token;
    String filename;
    Lexer lex = null;

    if(args.length != 1) {
      System.out.println("usage: java lexer.Lexer filename.x");
    } else {
      filename = args[0];

      try {
        lex = new Lexer(filename, true);

        while(!lex.atEOF) {
          token = lex.nextToken();
          token.print();
        }
      } catch (Exception e) {

      }

      try {
        lex = new Lexer(filename, false);
        lex.source.printFile();
      } catch (Exception e) {

      }
    }
  }
}
