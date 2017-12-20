/* Generated By:JavaCC: Do not edit this line. MitabLineParserTokenManager.java */
package psidev.psi.mi.jami.tab.io.parser;
import psidev.psi.mi.jami.model.*;
import psidev.psi.mi.jami.datasource.DefaultFileSourceContext;
import psidev.psi.mi.jami.tab.extension.*;
import psidev.psi.mi.jami.tab.listener.MitabParserListener;
import java.util.Collection;
import java.util.Collections;
import psidev.psi.mi.jami.utils.CvTermUtils;
import psidev.psi.mi.jami.utils.PositionUtils;
import java.util.ArrayList;
import java.util.EnumSet;
import java.lang.NumberFormatException;
import psidev.psi.mi.jami.exception.IllegalParameterException;
import psidev.psi.mi.jami.exception.IllegalRangeException;
import psidev.psi.mi.jami.tab.utils.MitabUtils;

/**
 * Token Manager.
 *

 */
public class MitabLineParserTokenManager implements MitabLineParserConstants
{

  /** Debug output. */
  public  java.io.PrintStream debugStream = System.out;
  /**
   * Set debug output.
   *
   * @param ds a {@link java.io.PrintStream} object.
   */
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 9:
         return jjStopAtPos(0, 6);
      case 10:
         return jjStopAtPos(0, 7);
      case 12:
         return jjStartNfaWithStates_0(0, 2, 4);
      case 13:
         return jjStartNfaWithStates_0(0, 1, 4);
      case 35:
         return jjStopAtPos(0, 4);
      case 40:
         return jjStopAtPos(0, 8);
      case 41:
         return jjStopAtPos(0, 9);
      case 44:
         return jjStopAtPos(0, 3);
      case 45:
         return jjStopAtPos(0, 11);
      case 58:
         return jjStopAtPos(0, 10);
      case 124:
         return jjStopAtPos(0, 5);
      default :
         return jjMoveNfa_0(5, 0);
   }
}
private int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
static final long[] jjbitVec0 = {
   0xfffffffffffffffeL, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec2 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private int jjMoveNfa_0(int startState, int curPos)
{
   int startsAt = 0;
   jjnewStateCnt = 5;
   int i = 1;
   jjstateSet[0] = startState;
   int kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         do
         {
            switch(jjstateSet[--i])
            {
               case 5:
                  if ((0xfbffccf3fffff9ffL & l) != 0L)
                  {
                     if (kind > 13)
                        kind = 13;
                     jjCheckNAdd(4);
                  }
                  else if (curChar == 34)
                     jjCheckNAddStates(0, 2);
                  break;
               case 0:
                  if (curChar == 34)
                     jjCheckNAddStates(0, 2);
                  break;
               case 1:
                  if ((0xfffffffbffffffffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 3:
                  if (curChar == 34 && kind > 12)
                     kind = 12;
                  break;
               case 4:
                  if ((0xfbffccf3fffff9ffL & l) == 0L)
                     break;
                  if (kind > 13)
                     kind = 13;
                  jjCheckNAdd(4);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 5:
               case 4:
                  if ((0xefffffffffffffffL & l) == 0L)
                     break;
                  if (kind > 13)
                     kind = 13;
                  jjCheckNAdd(4);
                  break;
               case 1:
                  jjAddStates(0, 2);
                  break;
               case 2:
                  if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 0;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         do
         {
            switch(jjstateSet[--i])
            {
               case 5:
               case 4:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 13)
                     kind = 13;
                  jjCheckNAdd(4);
                  break;
               case 1:
                  if (jjCanMove_0(hiByte, i1, i2, l1, l2))
                     jjAddStates(0, 2);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 5 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   1, 2, 3, 
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec2[i2] & l2) != 0L);
      default :
         if ((jjbitVec0[i1] & l1) != 0L)
            return true;
         return false;
   }
}

/** Token literal values. */
public static final String[] jjstrLiteralImages = {
"", null, null, "\54", "\43", "\174", "\11", "\12", "\50", "\51", "\72", 
"\55", null, null, };

/** Lexer state names. */
public static final String[] lexStateNames = {
   "DEFAULT",
};
static final long[] jjtoToken = {
   0x3ff9L, 
};
static final long[] jjtoSkip = {
   0x6L, 
};
protected JavaCharStream input_stream;
private final int[] jjrounds = new int[5];
private final int[] jjstateSet = new int[10];
protected char curChar;
/**
 * Constructor.
 *
 * @param stream a {@link psidev.psi.mi.jami.tab.io.parser.JavaCharStream} object.
 */
public MitabLineParserTokenManager(JavaCharStream stream){
   if (JavaCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}

/**
 * Constructor.
 *
 * @param stream a {@link psidev.psi.mi.jami.tab.io.parser.JavaCharStream} object.
 * @param lexState a int.
 */
public MitabLineParserTokenManager(JavaCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}

/**
 * Reinitialise parser.
 *
 * @param stream a {@link psidev.psi.mi.jami.tab.io.parser.JavaCharStream} object.
 */
public void ReInit(JavaCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 5; i-- > 0;)
      jjrounds[i] = 0x80000000;
}

/**
 * Reinitialise parser.
 *
 * @param stream a {@link psidev.psi.mi.jami.tab.io.parser.JavaCharStream} object.
 * @param lexState a int.
 */
public void ReInit(JavaCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}

/**
 * Switch to specified lex state.
 *
 * @param lexState a int.
 */
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

/**
 * <p>jjFillToken.</p>
 *
 * @return a {@link psidev.psi.mi.jami.tab.io.parser.Token} object.
 */
protected Token jjFillToken()
{
   final Token t;
   final String curTokenImage;
   final int beginLine;
   final int endLine;
   final int beginColumn;
   final int endColumn;
   String im = jjstrLiteralImages[jjmatchedKind];
   curTokenImage = (im == null) ? input_stream.GetImage() : im;
   beginLine = input_stream.getBeginLine();
   beginColumn = input_stream.getBeginColumn();
   endLine = input_stream.getEndLine();
   endColumn = input_stream.getEndColumn();
   t = Token.newToken(jjmatchedKind, curTokenImage);

   t.beginLine = beginLine;
   t.endLine = endLine;
   t.beginColumn = beginColumn;
   t.endColumn = endColumn;

   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

/**
 * Get the next Token.
 *
 * @return a {@link psidev.psi.mi.jami.tab.io.parser.Token} object.
 */
public Token getNextToken() 
{
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {
   try
   {
      curChar = input_stream.BeginToken();
   }
   catch(java.io.IOException e)
   {
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

private void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}

private void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}

}
