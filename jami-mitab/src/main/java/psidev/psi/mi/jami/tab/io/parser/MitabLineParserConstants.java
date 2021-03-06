/* Generated By:JavaCC: Do not edit this line. MitabLineParserConstants.java */
package psidev.psi.mi.jami.tab.io.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface MitabLineParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int RANGE_SEPARATOR = 3;
  /** RegularExpression Id. */
  int COMMENT = 4;
  /** RegularExpression Id. */
  int FIELD_SEPARATOR = 5;
  /** RegularExpression Id. */
  int COLUMN_SEPARATOR = 6;
  /** RegularExpression Id. */
  int LINE_SEPARATOR = 7;
  /** RegularExpression Id. */
  int OPEN_PAREN = 8;
  /** RegularExpression Id. */
  int CLOSE_PAREN = 9;
  /** RegularExpression Id. */
  int COLON = 10;
  /** RegularExpression Id. */
  int DASH = 11;
  /** RegularExpression Id. */
  int QUOTED_STRING = 12;
  /** RegularExpression Id. */
  int UNRESERVED_STRING = 13;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"\\r\"",
    "\"\\f\"",
    "\",\"",
    "\"#\"",
    "\"|\"",
    "\"\\t\"",
    "\"\\n\"",
    "\"(\"",
    "\")\"",
    "\":\"",
    "\"-\"",
    "<QUOTED_STRING>",
    "<UNRESERVED_STRING>",
  };

}
